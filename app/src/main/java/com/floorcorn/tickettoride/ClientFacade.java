package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/23/2017.
 */

public class ClientFacade implements IClient {
	private ClientModel clientModel = null;

	public ClientFacade(ClientModel cm) {
		clientModel = cm;
	}

	public void setClientModel(ClientModel cm) {
		clientModel = cm;
	}
	public Game getGame() {
		return clientModel.getCurrentGame();
	}
	public void updateGame(Game game) {
		clientModel.setCurrentGame(game);
	}
	public User getUser() {
		return clientModel.getCurrentUser();
	}

	public int getLastExecutedCommand() {
		return clientModel.getLastCommandExecuted();
	}

	public void setPlayerList(ArrayList<Player> newPlayers) {
		Game game = clientModel.getCurrentGame();
		if(game == null)
			return;
		game.setPlayerList(newPlayers);
		clientModel.setCurrentGame(game);
	}
}
