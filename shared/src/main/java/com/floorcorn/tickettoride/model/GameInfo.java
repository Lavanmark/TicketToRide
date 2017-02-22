package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler on 2/21/17.
 */

public class GameInfo {

	protected int gameID = -1;
	protected ArrayList<PlayerInfo> playerList = null;
	protected int gameSize = -1;
	protected String name = null;

	protected boolean finished = false;

	public GameInfo(int gameID, String name, int gameSize, ArrayList<PlayerInfo> players) {

	}

	public GameInfo(Game game) {
		gameID = game.getGameID();
		gameSize = game.getGameSize();
		name = game.getName();
		for(Player p : game.getPlayerList()) {
			playerList.add(p.getPlayerInfo());
		}
	}

	public int getGameID() {
		return gameID;
	}

	public ArrayList<PlayerInfo> getPlayerList() {
		return playerList;
	}

	public int getGameSize() {
		return gameSize;
	}

	public String getName() {
		return name;
	}

	public boolean hasStarted() {
		return playerList.size() == gameSize || isFinished();
	}

	public boolean isFinished() {
		return finished;
	}

	/**
	 * tests if the user is a player in the game
	 * @param userID id of the user that will be checked for
	 * @return true if user is in the game, false otherwise
	 */
	public boolean isPlayer(int userID) {
		for(PlayerInfo p : playerList) {
			if(p.getUserID() == userID)
				return true;
		}
		return false;
	}

	/**
	 * tests if the user could join
	 * @param user user with user.id
	 * @return true if player can join, false otherwise
	 * @throws BadUserException
	 * @throws GameActionException
	 */
	public boolean userCanJoin(User user) throws BadUserException, GameActionException {
		if(user == null) throw new BadUserException("A null user cannot join!");
		for(PlayerInfo p : playerList) {
			if(p.getUserID() == user.getUserID())
				throw new GameActionException("User already in the game!");
		}
		if(hasStarted()) return false;
		return true;
	}

	/**
	 * get a list of available PlayerColors
	 * @return List of PlayerColors not already in use
	 */
	public List<PlayerColor> getAvailableColors() {
		List<PlayerColor> all = Arrays.asList(PlayerColor.BLUE,
				PlayerColor.BLACK, PlayerColor.GREEN,
				PlayerColor.RED, PlayerColor.YELLOW);
		List<PlayerColor> taken = new ArrayList<PlayerColor>();
		for(PlayerInfo p : playerList)
			taken.add(p.getColor());
		List<PlayerColor> avail = new ArrayList<PlayerColor>();
		for(PlayerColor a : all)
			if(! taken.contains(a)) {
				avail.add(a);
			}
		return avail;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		GameInfo game = (GameInfo) o;

		if(gameID != game.gameID) return false;
		if(gameSize != game.gameSize) return false;
		if(!playerList.equals(game.playerList)) return false;
		return name.equals(game.name);
	}

	@Override
	public int hashCode() {
		return gameID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + '\n');
		sb.append("Size: " + gameSize + '\n');
		sb.append("Players: (" + playerList.size() + "/" + gameSize + ")\n");
		for(PlayerInfo p : playerList)
			sb.append("    " + p.getName() + "    " + p.getColor() +  "\n");
		return sb.toString();
	}
}
