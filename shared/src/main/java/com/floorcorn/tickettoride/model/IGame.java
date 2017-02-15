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

	/**
	 * adds a player to the game
	 * @param user full user object
	 * @param color color the user wants
	 * @return the player object just created
	 * @throws GameActionException
	 */
	public Player addPlayer(IUser user, Player.PlayerColor color) throws GameActionException {
		if(user == null) throw new GameActionException("Cannot add null User to game!");
		Player np = getPlayer(user);
		if(np == null && !hasStarted()) {
			np = new Player(user.getUserID(), user.getFullName(), gameID, color);
			np.setPlayerID(playerList.size());
			playerList.add(np);
		}
		return np;
	}

	/**
	 * removes a player from the game or ends the game if the player was the conductor
	 * @param user user containing at least user.id
	 * @return true if player was removed, false if not
	 * @throws GameActionException
	 */
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

	/**
	 * tests if the user could join
	 * @param user user with user.id
	 * @return true if player can join, false otherwise
	 * @throws BadUserException
	 * @throws GameActionException
	 */
	public boolean userCanJoin(IUser user) throws BadUserException, GameActionException {
		if(user == null) throw new BadUserException("A null user cannot join!");
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				throw new GameActionException("User already in the game!");
		}
		if(hasStarted()) return false;
		return true;
	}

	/**
	 * tests if the user is a player in the game
	 * @param userID id of the user that will be checked for
	 * @return true if user is in the game, false otherwise
	 */
	public boolean isPlayer(int userID) {
		for(Player p : playerList) {
			if(p.getUserID() == userID)
				return true;
		}
		return false;
	}

	/**
	 * get the player object for the specified user
	 * @param user contains at least user.id
	 * @return Player object of the user, null if user not in game
	 */
	public Player getPlayer(IUser user) {
		if(user == null) return null; // Don't throw BadUserException here.
		for(Player p : playerList) {
			if(p.getUserID() == user.getUserID())
				return p;
		}
		return null;
	}

	/**
	 * get a list of available Player.PlayerColors
	 * @return List of Player.PlayerColors not already in use
	 */
	public List<Player.PlayerColor> getAvailableColors() {


		List<Player.PlayerColor> all = Arrays.asList(Player.PlayerColor.BLUE,
				Player.PlayerColor.BLACK, Player.PlayerColor.GREEN,
				Player.PlayerColor.RED, Player.PlayerColor.YELLOW);
		List<Player.PlayerColor> taken = new ArrayList<Player.PlayerColor>();
		for(Player p : playerList){
			taken.add(p.getColor());
		}
		List<Player.PlayerColor> avail = new ArrayList<Player.PlayerColor>();
		for(Player.PlayerColor a: all){
			for(Player.PlayerColor b: taken){
				if(!a.equals(b)){
					avail.add(a);
				}
			}
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
		return playerList.size() == gameSize || isFinished();
	}

	public boolean isFinished() {
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Name: " + name + '\n');
		sb.append("Size: " + gameSize + '\n');
		sb.append("Players: (" + playerList.size() + "/" + gameSize + ")\n");
		for(Player p : playerList)
			sb.append("    " + p.getName() + "\n");
		return sb.toString();
	}
}
