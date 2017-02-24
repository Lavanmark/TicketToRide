package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;

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


}
