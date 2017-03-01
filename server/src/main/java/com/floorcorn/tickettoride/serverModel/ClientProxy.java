package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

import java.util.List;

/**
 * Created by Tyler on 2/23/2017.
 */

public class ClientProxy implements IClient {
	private Game gameToModify = null;
	public Game getGameToModify() {
		return gameToModify;
	}
	public void setGameToModify(Game game) {
		gameToModify = game;
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
