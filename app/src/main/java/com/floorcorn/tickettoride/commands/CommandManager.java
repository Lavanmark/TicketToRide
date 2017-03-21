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

		//if(commands.get(0).getCmdID() <= clientFacade.getLastExecutedCommand())
		//	return;
		// ^^^^^^ these lines cause the commands to never be executed.
		//TODO no way to prevent repeat commands now...
		// Joseph comin' in late here. Shouldn't that be commands.get(commands.size()-1)?

		for(ICommand command : commands) {
			System.out.println("doing command");
			game.addCommand(command);
			command.execute(game);
			model.setLastCommandExecuted(command.getCmdID());
		}
		model.notifyGameChanged();
	}

	public void setPlayerList(ArrayList<Player> players) {
		model.getCurrentGame().setPlayerList(players);
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
