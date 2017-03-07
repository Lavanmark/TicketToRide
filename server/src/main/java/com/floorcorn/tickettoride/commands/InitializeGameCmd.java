package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DeckManager;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.MapFactory;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.ClientProxy;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/23/2017.
 */

public class InitializeGameCmd extends InitializeGameCmdData {

	public InitializeGameCmd(ArrayList<Player> players) {
		this.players = new ArrayList<>(players);
	}

	@Override
	public ICommand getCmdFor(User user) {
		ArrayList<Player> newPlayers = new ArrayList<>();
		for(Player p : players)
			newPlayers.add(p.getCensoredPlayer(user));
		return new InitializeGameCmd(newPlayers);
	}

	@Override
	public void execute(IClient client) {
		Board board = new Board(new MapFactory().getMarsRoutes());
		board.setDeckManager(new DeckManager(true));

		// Deal initial train cards
		for(int i = 0; i < Game.INITIAL_TRAIN_CARDS; i++) {
			for(Player p : players) {
				try {
					p.addTrainCard(board.drawFromTrainCardDeck(), 1);
				} catch(GameActionException e) {
					e.printStackTrace();
				}
			}
		}

		//Deal initial destination cards
		for(int i = 0; i < Game.INITIAL_DESTINATION_CARDS; i++) {
			for(Player p : players) {
				try {
					p.addDestinationCard(board.drawFromDestinationCardDeck());
				} catch(GameActionException e) {
					e.printStackTrace();
				}
			}
		}
		for(Player p : players)
			Corn.log(p.getTotalTrainCards());
		//TODO make setboard a method on client?
		client.getGame().setBoard(board);
		client.setPlayerList(players);
	}
}
