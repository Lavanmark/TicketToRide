package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler on 2/2/2017.
 */

public abstract class IGame {

	protected int gameID = -1;
	protected ArrayList<Player> playerList = null;
	protected int gameSize = -1;
	protected String name = null;

	protected boolean finished = false;

	public Player addPlayer(IUser user, Player.PlayerColor color) throws GameActionException {
		if(user == null) throw new GameActionException("Cannot add null User to game!");
		Player np = getPlayer(user);
		if(np == null && !hasStarted()) {
			np = new Player(user.getUserID(), gameID, color);
			np.setPlayerID(playerList.size());
			playerList.add(np);
		}
		return np;
	}

	public boolean removePlayer(IUser user) throws GameActionException {
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
			} else
			throw new GameActionException("Game has already started!");
		} else
			throw new GameActionException("User is not a player of the game!");
	}

	public boolean userCanJoin(IUser user) throws BadUserException, GameActionException {
		if(user == null) throw new BadUserException("A null user cannot join!");
		if(hasStarted()) throw new GameActionException("Game has already started!");
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				throw new GameActionException("User already in the game!");
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

	public Player getPlayer(IUser user) {
		if(user == null) return null; // Don't throw BadUserException here.
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				return p;
		}
		return null;
	}

	public List<Player.PlayerColor> getAvailableColors() {
		List<Player.PlayerColor> avail = Arrays.asList(Player.PlayerColor.BLUE,
				Player.PlayerColor.BLACK, Player.PlayerColor.GREEN,
				Player.PlayerColor.RED, Player.PlayerColor.YELLOW);
		for(Player p : playerList) {
			avail.remove(p.getColor());
		}
		return avail;
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
