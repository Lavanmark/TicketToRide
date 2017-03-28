package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

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
		ICommand cmd = new InitializeGameCmd(newPlayers);
		cmd.setCmdID(this.commandID);
		cmd.setGameID(this.gameID);
		return cmd;
	}

	@Override
	public boolean execute(Game game) {
		// Deal initial train cards
		for(int i = 0; i < Game.INITIAL_TRAIN_CARDS; i++) {
			for(Player p : players) {
				try {
					p.addTrainCard(game.getBoard().drawFromTrainCardDeck());
				} catch(GameActionException e) {
					e.printStackTrace();
					return false;
				}
			}
		}

		//Deal initial destination cards
		for(int i = 0; i < Game.INITIAL_DESTINATION_CARDS; i++) {
			for(Player p : players) {
				try {
					p.addDestinationCard(game.getBoard().drawFromDestinationCardDeck());
				} catch(GameActionException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		for(Player p : players) {
			if(p.isConductor())
				p.setTurn(true);
			else
				p.setTurn(false);
		}
		game.setPlayerList(players);
		return true;
	}
}
