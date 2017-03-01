package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

import java.util.ArrayList;
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

	@Override
	public void setPlayerList(ArrayList<Player> players) {

	}

	@Override
	public void setFaceUpDeck(List<TrainCard> faceUpDeck) {

	}
}
