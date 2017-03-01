package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Game {

	private int gameID = -1;
	private ArrayList<Player> playerList = null;
	private int gameSize = -1;
	private String name = null;
	private ArrayList<ICommand> commands = null;

	private boolean finished = false;

	private Game(){}

	public Game(Game game) {
		this.gameID = game.getGameID();
		this.gameSize = game.getGameSize();
		this.name = game.getName();
		this.playerList = new ArrayList<Player>(game.getPlayerList());
		this.finished = game.isFinished();
		this.commands = new ArrayList<>(game.getCommands());
	}

	public Game(String name, int size, int gameID) {
		this.name = name;
		if(size < 2) size = 2;
		if(size > 5) size = 5;
		this.gameSize = size;
		this.playerList = new ArrayList<Player>();
		this.gameID = gameID;
		this.commands = new ArrayList<>();
	}

	@JsonIgnore
	public GameInfo getGameInfo() {
		return new GameInfo(this);
	}

	public void setPlayerList(ArrayList<Player> newPlayers) {
		if(playerList == null)
			playerList = new ArrayList<>();
		this.playerList.clear();
		this.playerList.addAll(newPlayers);
	}

	public ArrayList<ICommand> getCommands() {
		return commands;
	}

	public void addCommand(ICommand command) {
		if(command.getCmdID() > getLatestCommandID())
			this.commands.add(command);
	}

	@JsonIgnore
	public int getLatestCommandID() {
		if(commands.size() <= 0)
			return -1;
		return commands.get(commands.size()-1).getCmdID();
	}

	/**
	 * adds a player to the game
	 * @param user full user object
	 * @param color color the user wants
	 * @return the player object just created
	 * @throws GameActionException
	 */
	public Player addPlayer(User user, PlayerColor color) throws GameActionException {
		if(user == null) throw new GameActionException("Cannot add null User to game!");
		if(color == null) throw new GameActionException("Color was not selected!");
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
	public boolean removePlayer(User user) throws GameActionException {
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
	public boolean userCanJoin(User user) throws BadUserException, GameActionException {
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
	public Player getPlayer(User user) {
		if(user == null) return null; // Don't throw BadUserException here.
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
		return playerList.size() == gameSize || isFinished();
	}

	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Game game = (Game) o;

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
