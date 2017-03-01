package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Tyler on 2/21/17.
 */

public class GameInfo {

	private int gameID = Game.NO_GAME_ID;
	private ArrayList<PlayerInfo> playerList = null;
	private int gameSize = -1;
	private String name = null;

	private boolean finished = false;

	private GameInfo(){}

	public GameInfo(String name, int gameSize) {
		this.gameSize = gameSize;
		this.name = name;
	}

	public GameInfo(int gameID) {
		this.gameID = gameID;
	}

	public GameInfo(Game game) {
		gameID = game.getGameID();
		gameSize = game.getGameSize();
		name = game.getName();
		playerList = new ArrayList<PlayerInfo>();
		for(Player p : game.getPlayerList()) {
			playerList.add(p.getPlayerInfo());
		}
		finished = game.isFinished();
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
		return playerList != null && (playerList.size() == gameSize || isFinished());
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
		if(playerList == null)
			return false;
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
		if(playerList != null) {
			for(PlayerInfo p : playerList) {
				if(p.getUserID() == user.getUserID())
					throw new GameActionException("User already in the game!");
			}
		}
		return !hasStarted();
	}

	/**
	 * get a list of available PlayerColors
	 * @return List of PlayerColors not already in use
	 */
	@JsonIgnore
	public List<PlayerColor> getAvailableColors() {
		List<PlayerColor> all = Arrays.asList(PlayerColor.BLUE,
				PlayerColor.BLACK, PlayerColor.GREEN,
				PlayerColor.RED, PlayerColor.YELLOW);
		List<PlayerColor> taken = new ArrayList<PlayerColor>();
		if(playerList != null)
			for(PlayerInfo p : playerList)
				taken.add(p.getColor());
		List<PlayerColor> avail = new ArrayList<PlayerColor>();
		for(PlayerColor a : all)
			if(! taken.contains(a)) {
				avail.add(a);
			}
		return avail;
	}

	/**
	 * get the player object for the specified user
	 * @param user contains at least user.id
	 * @return Player object of the user, null if user not in game
	 */
	public PlayerInfo getPlayer(User user) {
		if(user == null) return null; // Don't throw BadUserException here.
		if(playerList != null){
			for(PlayerInfo p : playerList) {
				if(p.getUserID() == user.getUserID())
					return p;
			}
		}
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		GameInfo gameInfo = (GameInfo) o;

		if(gameID != gameInfo.gameID) return false;
		if(gameSize != gameInfo.gameSize) return false;
		if(finished != gameInfo.finished) return false;
		if(playerList != null ? !playerList.equals(gameInfo.playerList) : gameInfo.playerList != null)
			return false;
		return name != null ? name.equals(gameInfo.name) : gameInfo.name == null;

	}

	@Override
	public int hashCode() {
		return gameID;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: ").append(name).append('\n');
		sb.append("Size: ").append(gameSize).append('\n');
		sb.append("Players: (").append(playerList.size()).append("/").append(gameSize).append(")\n");
		for(PlayerInfo p : playerList)
			sb.append("    ").append(p.getName()).append("    ").append(p.getColor()).append("\n");
		return sb.toString();
	}
}
