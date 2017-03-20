package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.exceptions.GameActionException;
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

	public void setGameToModify(Game game) {
		gameToModify = game;
	}



	@Override
	public void setPlayerList(ArrayList<Player> players) {
		gameToModify.setPlayerList(players);
	}

	@Override
	public void setFaceUpDeck(TrainCard[] faceUpDeck) {
		try {
			gameToModify.getBoard().setFaceUpCards(faceUpDeck);
		} catch(GameActionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Game getGame() {
		return gameToModify;
	}

	public int getLastExecutedCommand() {
		return gameToModify.getLatestCommandID();
	}

	public void addCommandToGame(ICommand command) {
		gameToModify.addCommand(command);
	}
}
