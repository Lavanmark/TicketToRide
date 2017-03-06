package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.ClientFacade;
import com.floorcorn.tickettoride.clientModel.ClientModel;
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

	private ClientFacade clientFacade = null;

	public CommandManager(ClientModel cm) {
		clientFacade = new ClientFacade(cm);
	}

	public void addCommands(ArrayList<ICommand> commands) throws GameActionException {
		Game game = clientFacade.getGame();
		if(game == null) {
			throw new GameActionException("Not in this game anymore!");
		}

		if(commands == null || commands.size() == 0)
			return;

		//if(commands.get(0).getCmdID() <= clientFacade.getLastExecutedCommand())
		//	return;
		// ^^^^^^ these lines cause the commands to never be executed.
		//TODO no way to prevent repeat commands now...

		for(ICommand command : commands) {
			System.out.println("doing command");
			game.addCommand(command);
			command.execute(clientFacade);
			clientFacade.setLastExecutedCommand(command.getCmdID());
		}
		clientFacade.updateGame(game);
	}

	public void setPlayerList(ArrayList<Player> players) {
		clientFacade.setPlayerList(players);
	}

	public int currentGameID() {
		if(clientFacade.getGame() == null)
			return Game.NO_GAME_ID;
		return clientFacade.getGame().getGameID();
	}

	public int getLastCommandExecuted() {
		return clientFacade.getLastExecutedCommand();
	}

	public User getUser() {
		return clientFacade.getUser();
	}

	public void setClientModel(ClientModel cm) {
		clientFacade.setClientModel(cm);
	}

	public Game getGame() {
		return clientFacade.getGame();
	}

	public ClientFacade getClientFacade() {
		return clientFacade;
	}
}
