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

	public static final int NO_GAME_ID = -1;
	public static final int INITIAL_TRAIN_CARS = 45;
	public static final int INITIAL_DESTINATION_CARDS = 3;
	public static final int INITIAL_TRAIN_CARDS = 4;

	private int gameID = NO_GAME_ID;
	private ArrayList<Player> playerList = null;
	private int gameSize = -1;
	private String name = null;
	private boolean finished = false;
	private int longestRoute = 0;

	@JsonIgnore
	private ArrayList<ICommand> commands = new ArrayList<>();

	private Board board = null;


	private Game(){} //<--- what is this doing here? doesnt look right (Joseph: who wrote this original comment?)
	//Tyler says: it is here for deserializing.

	public Game(Game game) {
		this.gameID = game.getGameID();
		this.gameSize = game.getGameSize();
		this.name = game.getName();
		this.playerList = new ArrayList<>(game.getPlayerList());
		this.finished = game.isFinished();
		this.commands = new ArrayList<>(game.getCommands());
		this.board = new Board(game.getBoard());
	}

	public Game(String name, int size, int gameID) {
		this.name = name;
		if(size < 2) size = 2;
		if(size > 5) size = 5;
		this.gameSize = size;
		this.playerList = new ArrayList<Player>();
		this.gameID = gameID;
		this.commands = new ArrayList<>();
		this.board = new Board(new MapFactory().getMarsRoutes());
		this.board.setDeckManager(new DeckManager(true));
	}

	
	public GameInfo getGameInfo() {
		return new GameInfo(this);
	}

	
	public Game getCensoredGame(User user) {
		Game game = new Game(this);
		ArrayList<Player> censoredPlayers = new ArrayList<>();
		for(Player p : playerList)
			censoredPlayers.add(p.getCensoredPlayer(user));
		game.setPlayerList(censoredPlayers);
		game.commands = new ArrayList<>();
		return game;
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


	/**
	 * Returns the ID of the game's last command.
	 * @return int ID of the last command
	 */
	public int getLatestCommandID() {
		if(commands.size() <= 0)
			return ICommand.NO_CMD_ID;
		return commands.get(commands.size() - 1).getCmdID();
	}

	/**
	 * Returns this game's last command.
	 * @return object that implements ICommand
	 */
	public ICommand getLatestCommand() {
		if(!commands.isEmpty())
			return commands.get(commands.size() - 1);
		return null;
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

	public void drawTrainCardFromDeck(User user) throws GameActionException {
		Player player = getPlayer(user);
		if(player == null)
			return;
		if(player.isTurn())
			player.addTrainCard(board.drawFromTrainCardDeck(), 1);
	}

	public void drawFaceUpCard(User user, int postition) throws GameActionException {
		Player player = getPlayer(user);
		if(player == null)
			return;
		if(player.isTurn())
			player.addTrainCard(board.drawFromFaceUp(postition), 1);
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

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public List<Player> getPlayerLongestRoute(){ // returns 1..* players with the longest route. 10 points goes to all those who are tied for the longest route
		List<Player> playerLongestRoute = new ArrayList<Player>();
		for(Player player: playerList){
			if(player.getLongestRoute() == longestRoute)
				playerLongestRoute.add(player);
		}
		return playerLongestRoute;
	}

	public void calculateLongestRoute(){ // gets the longest route from each player to determine the longest route in the entire game.
		for(Player player: playerList){
			if(player.getLongestRoute() > longestRoute)
				longestRoute = player.getLongestRoute();
				board.setLongestRoute(longestRoute); // sets the child
		}
	}

	public int getLongestRoute(){ // just a simple getter
		return longestRoute;
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
