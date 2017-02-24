package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.ClientProxy;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/23/2017.
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

		return null;
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

		return null;
	}
}
