package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

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
	public void setPlayerList(List<Player> players) {
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
	public void startTurn(Player player) {
		for(Player p : gameToModify.getPlayerList()) {
			p.setTurn(false);
			if(p.equals(player))
				p.setTurn(true);
		}
	}

	@Override
	public void setLastPlayer(Player player) {
		gameToModify.setLastPlayerID(player.getPlayerID());
	}

	@Override
	public void setGameOver() {
		gameToModify.endGame();
	}

	@Override
	public TrainCard drawTrainCard() {
		try {
			return gameToModify.getBoard().drawFromTrainCardDeck();
		} catch(GameActionException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public TrainCard drawTrainCard(int position) {
		return null;
	}

	@Override
	public List<DestinationCard> drawDestinationCards() {
		return null;
	}

	@Override
	public void addCardToPlayer(Player player, TrainCard card) {

	}

	@Override
	public void addDestinationCardsToPlayer(Player player, List<DestinationCard> cards) {

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
