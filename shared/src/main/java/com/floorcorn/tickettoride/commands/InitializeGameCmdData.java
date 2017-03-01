package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;

/**
 * Created by Tyler on 3/1/17.
 */

public abstract class InitializeGameCmdData extends ICommand {
	protected ArrayList<Player> players = null;

	public ArrayList<Player> getPlayers() {
		return players;
	}
	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	@Override
	public boolean forPlayer(User user) {
		for(Player p : players) {
			if(p.getTotalTrainCards() > 0 && p.getTrainCards().isEmpty()) {
				if(p.getUserID() == user.getUserID())
					return false;
			} else if(p.getTotalTrainCards() > 0 && !p.getTrainCards().isEmpty()) {
				if(p.getUserID() != user.getUserID())
					return false;
			}
		}
		return true;
	}
}
