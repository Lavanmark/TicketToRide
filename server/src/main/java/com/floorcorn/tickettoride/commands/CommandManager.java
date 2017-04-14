package com.floorcorn.tickettoride.commands;


import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;
import com.floorcorn.tickettoride.IDAOFactory;
import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IGameDTO;
import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.exceptions.CommandRequestException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/23/2017.
 * @author Michael
 */

public class CommandManager {

	private ICommandDAO commandDAO;
	private IGameDAO gameDAO;
	
	public CommandManager(IDAOFactory factory) {
		if(factory != null) {
			commandDAO = factory.getCommandDAOInstance();
			gameDAO = factory.getGameDAOInstance();
		}
	}
	
	public ArrayList<ICommand> getCommandsSince(User user, Game game, int lastCommand) throws GameActionException, CommandRequestException {
		if(game == null)
			throw new GameActionException("No game to get commands from!");
		if(!game.isPlayer(user.getUserID()))
			throw new GameActionException("User is not a player!");
		if(!game.getCommands().isEmpty()) {
			if(game.getCommands().get(0).getCmdID() > lastCommand + 1) {//TODO might need to be just lastcommand
				throw new CommandRequestException("Commands reset! Request game instead.");
			}
		}
		
		ArrayList<ICommand> commands = game.getCommands();

		if(lastCommand >= game.getLatestCommandID())
			return new ArrayList<>();


		ListIterator<ICommand> li = commands.listIterator(lastCommand + 1);

		ArrayList<ICommand> newList = new ArrayList<>();
		while(li.hasNext()) {
			ICommand cmd = li.next();
			newList.add(cmd.getCmdFor(user));
		}
		
		//Update commands and game if needed.
		checkGameUpdate(game);
		
		return newList;
	}

	public ArrayList<ICommand> doCommand(User user, Game game, ICommand command) throws GameActionException, CommandRequestException {
		if(game == null)
			throw new GameActionException("No game to get commands from!");
		if(!game.isPlayer(user.getUserID()))
			throw new GameActionException("User is not a player!");
		if(!game.getPlayer(user.getUserID()).isTurn()) {
			if(!(command instanceof DiscardDestinationCmd))
				throw new GameActionException("Not your turn!");
		}
		
		List<ICommand> reactions = new ArrayList<>();
		
		if(command instanceof DrawTrainCardCmd){
			if(((DrawTrainCardCmd)command).cardPosition != -1) {
				reactions.add(new SetFaceUpDeckCmd());
				if(!((DrawTrainCardCmd)command).firstDraw)
					reactions.add(new StartTurnCmd(game.getNextPlayer()));
			} else if(game.getBoard().isDeckEmpty() && (game.getBoard().isFaceUpEmpty() || game.getBoard().isAllWildFaceUp()) && !((DrawTrainCardCmd)command).firstDraw) {
				command = new StartTurnCmd(game.getNextPlayer());
				reactions.clear();
				Corn.log(Level.SEVERE, "No cards left.");
			} else if(!((DrawTrainCardCmd)command).firstDraw) {
				reactions.add(new StartTurnCmd(game.getNextPlayer()));
			}
		}
		
		if(command instanceof ClaimRouteCmd) {
			reactions.add(new StartTurnCmd(game.getNextPlayer()));
			if(game.getBoard().isDeckEmpty() || game.getBoard().shouldResetFaceUp())
				reactions.add(new SetFaceUpDeckCmd());
		}
		
		if(command instanceof DrawDestinationCmd) {
			reactions.add(new StartTurnCmd(game.getNextPlayer()));
		}

		int lastCommandClient = command.getCmdID();
		command.setCmdID(game.getLatestCommandID() + 1);
		command.setGameID(game.getGameID());
		if(!command.execute(game))
			throw new GameActionException("Could not execute command!");
		game.addCommand(command);
		
		if(command instanceof ClaimRouteCmd) {
			if(game.getLastPlayer() == null)
				if(game.getPlayer(((ClaimRouteCmd) command).claimingPlayer.getUserID()).getTrainCarsLeft() < 3)
					reactions.add(new LastRoundCmd(game.getPlayer(((ClaimRouteCmd) command).claimingPlayer.getUserID())));
		}
		
		if(game.getLastPlayer() != null) {
			if(command instanceof DrawTrainCardCmd)
				if(((DrawTrainCardCmd)command).drawingPlayer.getPlayerID() == game.getLastPlayer().getPlayerID())
					if(!((DrawTrainCardCmd)command).firstDraw)
						reactions.add(new GameOverCmd(game.getPlayerList()));
			if(command instanceof DrawDestinationCmd)
				if(((DrawDestinationCmd)command).drawingPlayer.getPlayerID() == game.getLastPlayer().getPlayerID())
					reactions.add(new GameOverCmd(game.getPlayerList()));
			if(command instanceof ClaimRouteCmd)
				if(((ClaimRouteCmd)command).claimingPlayer.getPlayerID() == game.getLastPlayer().getPlayerID())
					reactions.add(new GameOverCmd(game.getPlayerList()));
		}
		
		for(ICommand reaction : reactions){
			reaction.setCmdID(game.getLatestCommandID() + 1);
			reaction.setGameID(game.getGameID());
			if(reaction.execute(game))
				game.addCommand(reaction);
		}
		
		//Store new commands in database
		reactions.add(command);
		addCommandsToDB(reactions);

		return getCommandsSince(user, game, lastCommandClient);
	}

	public void startGame(Game game) {
		if(game == null)
			return;
		if(!game.hasStarted() || game.isFinished())
			return;

		ICommand init = new InitializeGameCmd(game.getPlayerList());
		init.setCmdID(0);
		init.setGameID(game.getGameID());
		init.execute(game);
		game.addCommand(init);

		ICommand faceUp = new SetFaceUpDeckCmd();
		faceUp.setCmdID(1);
		faceUp.setGameID(game.getGameID());
		faceUp.execute(game);
		game.addCommand(faceUp);
		
		ICommand startTurn = new StartTurnCmd(game.getPlayerList().get(0));
		startTurn.setCmdID(2);
		startTurn.setGameID(game.getGameID());
		startTurn.execute(game);
		game.addCommand(startTurn);
		
		List<ICommand> commands = new ArrayList<>();
		commands.add(init);
		commands.add(faceUp);
		commands.add(startTurn);
		
		addCommandsToDB(commands);
		checkGameUpdate(game);
	}
	
	private void addCommandsToDB(List<ICommand> commands) {
		if(commandDAO != null) {
			commandDAO.startTransaction();
			for(ICommand cmd : commands) {
				ICommandDTO cmdDTO = ServerFacade.daoFactory.getCommandDTOInstance();
				cmdDTO.setGameID(cmd.getGameID());
				cmdDTO.setID(cmd.getCmdID());
				cmdDTO.setData(Serializer.getInstance().serialize(cmd));
				commandDAO.create(cmdDTO);
			}
			commandDAO.endTransaction(true);
		}
	}
	
	private void checkGameUpdate(Game game) {
		if(gameDAO != null && commandDAO != null && game != null && ServerFacade.max_commands > -1
				&& game.getCommands().size() > ServerFacade.max_commands) {
			
			//Store game before so that it is saved before we delete the commands.
			gameDAO.startTransaction();
			IGameDTO gameDTO = ServerFacade.daoFactory.getGameDTOInstance();
			gameDTO.setID(game.getGameID());
			gameDTO.setData(Serializer.getInstance().serialize(game));
			gameDAO.update(gameDTO);
			gameDAO.endTransaction(true);
			
			commandDAO.startTransaction();
			if(commandDAO.deleteAllForGame(game.getGameID()))
				commandDAO.endTransaction(true);
			else
				commandDAO.endTransaction(false);
			
			//Do this last
			game.clearCommands();
		}
	}
	
}
