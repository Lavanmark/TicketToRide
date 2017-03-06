package com.floorcorn.tickettoride.commands;


import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.ClientProxy;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/23/2017.
 * @author Michael
 */

public class CommandManager {

	private ClientProxy clientProxy = null;

	public CommandManager() {
		clientProxy = new ClientProxy();
	}

	public ArrayList<ICommand> getCommandsSince(User user, Game game, int lastCommand) throws GameActionException {
		if(game == null)
			throw new GameActionException("No game to get commands from!");
		if(!game.isPlayer(user.getUserID()))
			throw new GameActionException("User is not a player!");
		ArrayList<ICommand> commands = game.getCommands();

		if(lastCommand < 0)
			lastCommand = 0;
		if(lastCommand >= commands.size())
			return new ArrayList<>(); //TODO I don't know if this is the best solution...


		ListIterator<ICommand> li = commands.listIterator(lastCommand);

		ArrayList<ICommand> newList = new ArrayList<>();
		while(li.hasNext()) {
			ICommand cmd = li.next();
			if(cmd.forPlayer(user))
				newList.add(cmd);
			else
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
			//TODO if there are actions they can do not on their turn then allow them. such as discard destination cards
			throw new GameActionException("Not your turn!");
		}
		//TODO add chain reaction commands.
		clientProxy.setGameToModify(game);
		int lastCommandClient = command.getCmdID();
		command.setCmdID(clientProxy.getLastExecutedCommand());
		command.execute(clientProxy);
		clientProxy.addCommandToGame(command);

		return getCommandsSince(user, game, lastCommandClient);
	}

	public void startGame(Game game) {
		if(game == null)
			return;
		if(!game.hasStarted() || game.isFinished())
			return;

		clientProxy.setGameToModify(game);

		ICommand init = new InitializeGameCmd(game.getPlayerList());
		init.setCmdID(0);
		init.execute(clientProxy);
		clientProxy.addCommandToGame(init);

		ICommand faceUp = new SetFaceUpDeckCmd(game.getBoard().getFaceUpCards());
		faceUp.setCmdID(1);
		faceUp.execute(clientProxy);
		clientProxy.addCommandToGame(faceUp);
	}
}
