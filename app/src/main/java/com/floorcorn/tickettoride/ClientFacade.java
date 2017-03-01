package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.List;

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

	/**
	 * Assigns a new list of players to be stored in the model
	 *
	 * @param players a list of players to be stored.
	 */
	@Override
	public void setPlayerList(List<Player> players) {

	}

	/**
	 * Assigns a new deck of TrainCards to be stored as the face up deck
	 *
	 * @param faceUpDeck the new list of TrainCards to be stored as the face up deck
	 */
	@Override
	public void setFaceUpDeck(List<TrainCard> faceUpDeck) {

	}
}
