package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 2/27/2017.
 */

public class CommandManager {

	private ClientModel model = null;

	public CommandManager(ClientModel cm) {
		model = cm;
	}

	public void addCommands(List<ICommand> commands) throws GameActionException {
		Game game = model.getCurrentGame();
		if(game == null) {
			throw new GameActionException("Not in this game anymore!");
		}

		if(commands == null || commands.size() == 0)
			return;

		for(ICommand command : commands) {
			System.out.println(game.getLatestCommandID()  + " cmd " + command.getCmdID());
			if(game.getLatestCommandID() > command.getCmdID())
				continue;
			System.out.println("doing command");
			
			if(!command.execute(game))
				System.out.println("FAILED TO EXECUTE COMMAND");
			game.addCommand(command);
		}
		model.notifyGameChanged();
	}

	public void setPlayerList(ArrayList<Player> players) {
		model.getCurrentGame().setPlayerList(players);
		model.notifyGameChanged();
	}
	
	public void updateGame(Game game) {
		model.setCurrentGame(game); //Don't need to call notify.
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
