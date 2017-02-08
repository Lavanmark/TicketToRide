package com.floorcorn.tickettoride.model;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/2/2017.
 */

public abstract class IGame {

	protected int gameID = -1;
	protected ArrayList<Player> playerList = null;
	protected int gameSize = -1;
	protected String name = null;

	protected boolean finished = false;

	public Player addPlayer(User user, Player.PlayerColor color) {
		if(user == null) return null;
		Player np = getPlayer(user);
		if(np == null && !hasStarted()) {
			np = new Player(user.getUserID(), gameID, color);
			np.setPlayerID(playerList.size());
			playerList.add(np);
		}
		return np;
	}

	public boolean removePlayer(User user) {
		Player player = getPlayer(user);
		if(player != null) {
			if(!hasStarted()) {
				if(player.isConductor()) {
					playerList.clear();
					this.finished = true;
					return true;
				}
				ArrayList<Player> newlist = new ArrayList<Player>();
				for(int i = 0; i < playerList.size(); i++) {
					if(playerList.get(i).getUserID() != user.getUserID()) {
						playerList.get(i).setPlayerID(newlist.size());
						newlist.add(playerList.get(i));
					}
				}
				playerList = newlist;
				return true;
			}
		}
		return false;
	}

	public boolean userCanJoin(User user) {
		if(user == null) return false;
		if(hasStarted()) return false;
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				return false;
		}
		return true;
	}

	public boolean isPlayer(int userID) {
		for(Player p : playerList) {
			if(p.getUserID() == userID)
				return true;
		}
		return false;
	}

	public Player getPlayer(User user) {
		if(user == null) return null;
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				return p;
		}
		return null;
	}

	public int getGameID() {
		return gameID;
	}

	public ArrayList<Player> getPlayerList() {
		return playerList;
	}

	public int getGameSize() {
		return gameSize;
	}

	public String getName() {
		return name;
	}

	public boolean hasStarted() {
		return playerList.size() == gameSize || isFinsihed();
	}

	public boolean isFinsihed() {
		return finished;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		IGame game = (IGame) o;

		if(gameID != game.gameID) return false;
		if(gameSize != game.gameSize) return false;
		if(!playerList.equals(game.playerList)) return false;
		return name.equals(game.name);
	}

	@Override
	public int hashCode() {
		return gameID;
	}
}
