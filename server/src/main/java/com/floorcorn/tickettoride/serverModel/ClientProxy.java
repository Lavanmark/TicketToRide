package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.commands.ICommand;
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
		//Do nothing.
	}

	@Override
	public void setFaceUpDeck(List<TrainCard> faceUpDeck) {
		//Do nothing.
	}

	public int getLastExecutedCommand() {
		return gameToModify.getLatestCommandID();
	}

	public void addCommandToGame(ICommand command) {
		gameToModify.addCommand(command);
	}
}
