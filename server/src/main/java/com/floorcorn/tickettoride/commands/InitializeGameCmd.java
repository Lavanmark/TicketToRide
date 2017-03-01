package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/23/2017.
 */

public class InitializeGameCmd extends InitializeGameCmdData {

	public InitializeGameCmd(ArrayList<Player> players) {
		this.players = players;
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
		client.setPlayerList(players);
	}
}
