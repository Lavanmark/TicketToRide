package com.floorcorn.tickettoride.commands;


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

	public ArrayList<ICommand> getCommandsSince(User user, Game game, int lastCommand) throws GameActionException {
		if(game == null)
			throw new GameActionException("No game to get commands from!");
		if(!game.isPlayer(user.getUserID()))
			throw new GameActionException("User is not a player!");
		ArrayList<ICommand> commands = game.getCommands();

		if(lastCommand >= game.getLatestCommandID())
			return new ArrayList<>();


		ListIterator<ICommand> li = commands.listIterator(lastCommand + 1);

		ArrayList<ICommand> newList = new ArrayList<>();
		while(li.hasNext()) {
			ICommand cmd = li.next();
			newList.add(cmd.getCmdFor(user));
		}
		return newList;
	}

	public ArrayList<ICommand> doCommand(User user, Game game, ICommand command) throws GameActionException {
		if(game == null)
			throw new GameActionException("No game to get commands from!");
		if(!game.isPlayer(user.getUserID()))
			throw new GameActionException("User is not a player!");
		if(!game.getPlayer(user).isTurn()) {
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
				if(game.getPlayer(((ClaimRouteCmd) command).claimingPlayer).getTrainCarsLeft() < 3)
					reactions.add(new LastRoundCmd(game.getPlayer(((ClaimRouteCmd) command).claimingPlayer)));
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
	}
	
	private void cando() {
//		boolean cando = false;
//		for(int i = game.getCommands().size() - 1; i >= 0; i--) {
//			if(game.getCommands().get(i) instanceof StartTurnCmd) {
//				cando = true;
//				break;
//			}
//			if(game.getCommands().get(i) instanceof ClaimRouteCmd) {
//				cando = false;
//				break;
//			}
//			if(game.getCommands().get(i) instanceof DrawDestinationCmd) {
//				cando = false;
//				break;
//			}
//		}
//
//		if(!cando) //TODO maybe not?
//			return new ArrayList<>();
	}
}
