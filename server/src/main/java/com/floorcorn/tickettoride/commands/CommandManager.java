package com.floorcorn.tickettoride.commands;


import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.ClientProxy;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * Created by Tyler on 2/23/2017.
 * @author Michael
 */

public class CommandManager {

	private ClientProxy clientProxy = null;

	public CommandManager() {
		clientProxy = new ClientProxy();
	}

	public ArrayList<ICommand> getCommandsSince(User user, Game game, int lastCommand) {
		if(game == null)
			return null;
		if(!game.isPlayer(user.getUserID()))
			return null;

		ArrayList<ICommand> commands = game.getCommands();
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

	public ArrayList<ICommand> doCommand(User user, Game game, ICommand command) {
		if(game == null)
			return null;
		if(!game.isPlayer(user.getUserID()))
			return null;
		if(!game.getPlayer(user).isTurn()) {
			//TODO if there are actions they can do not on their turn then allow them. such as discard destination cards
			return null;
		}
		//TODO add chain reaction commands.
		clientProxy.setGameToModify(game);
		int lastCommandClient = command.getCmdID();
		command.setCmdID(clientProxy.getLastExecutedCommand());
		command.execute(clientProxy);
		clientProxy.addCommandToGame(command);


		return getCommandsSince(user, game, lastCommandClient);
	}
}
