package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.exceptions.GameActionException;
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

	public void setChatLog(GameChatLog chatLog) {
		clientModel.setChatLog(chatLog);
	}

	public int getLastExecutedCommand() {
		return clientModel.getLastCommandExecuted();
	}
	public void setLastExecutedCommand(int lastCmdID) {
		if(clientModel.getLastCommandExecuted() < lastCmdID)
			clientModel.setLastCommandExecuted(lastCmdID);
	}

	@Override
	public void setPlayerList(ArrayList<Player> players) {
		Game game = clientModel.getCurrentGame();
		if(game == null)
			return;
		game.setPlayerList(players);
		clientModel.setCurrentGame(game);
	}

	@Override
	public void setFaceUpDeck(TrainCard[] faceUpDeck) {
		Game game = clientModel.getCurrentGame();
		for(TrainCard t : faceUpDeck)
			System.out.println(t.getColor());
		if(game == null)
			return;
		try {
			game.getBoard().setFaceUpCards(faceUpDeck);
		} catch(GameActionException e) {
			e.printStackTrace();
		}
	}
}
