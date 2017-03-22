package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/27/2017.
 */

public class CommandManager {

	private ClientModel model = null;

	public CommandManager(ClientModel cm) {
		model = cm;
	}

	public void addCommands(ArrayList<ICommand> commands) throws GameActionException {
		Game game = model.getCurrentGame();
		if(game == null) {
			throw new GameActionException("Not in this game anymore!");
		}

		if(commands == null || commands.size() == 0)
			return;

		for(ICommand command : commands) {
			if(game.getLatestCommandID() >= command.getCmdID())
				continue;
			System.out.println("doing command");
			game.addCommand(command);
			command.execute(game);
			model.setLastCommandExecuted(command.getCmdID());
		}
		model.notifyGameChanged();
	}

	public void setPlayerList(ArrayList<Player> players) {
		model.getCurrentGame().setPlayerList(players);
		model.notifyGameChanged();
	}

	public int currentGameID() {
		if(model.getCurrentGame() == null)
			return Game.NO_GAME_ID;
		return model.getCurrentGame().getGameID();
	}

	public int getLastCommandExecuted() {
		return model.getLastCommandExecuted();
	}

	public User getUser() {
		return model.getCurrentUser();
	}

	public Game getGame() {
		return model.getCurrentGame();
	}

	public void setChatLog(GameChatLog log) {
		model.setChatLog(log);
	}
}
