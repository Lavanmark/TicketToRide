package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerModel {
	private Set<Game> games; // Stores all games ever. If game is canceled or ends, it remains here with the players so users can get this info.
	private Set<User> users; // Stores all users ever.
	private SecureRandom random;
    private ChatManager chatManager;

	public ServerModel() {
		games = new HashSet<>();
		users = new HashSet<>();
		random = new SecureRandom();
        chatManager = new ChatManager();
	}

	private void generateToken(User u) {
		u.setToken(new BigInteger(130, random).toString(32));
	}

	/**
	 * authenticate a user using username and password of a given user and generates authentication token
	 * @param username valid username of a user
	 * @param password valid password of a user
	 * @return user who was logged in, or null if bad credentials
	 */
	public User authenticate(String username, String password) throws BadUserException {
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				if(u.getPassword().equals(password)) {
					generateToken(u);
					return u;
				}
				throw new BadUserException("Invalid username or password!");
			}
		}
		throw new BadUserException("Invalid username or password!");
	}

	/**
	 * authenticates a user using an authentication token.
	 * @param token token of specified user to log on
	 * @return user with corresponding token if login successful, null if not
	 */
	public User authenticate(String token) throws BadUserException {
		for(User u : users) {
			if(u.getToken().equals(token))
				return u;
		}
		throw new BadUserException("Invalid user token!");
	}

	/**
	 * create a new game with name and gamesize *games with non original names can still be made
	 * @param name string that should be the displayed name of the game
	 * @param gameSize number of players in the game
	 * @return the newly created game
	 */
	public GameInfo addGame(String name, int gameSize) {
		Game newGame = new Game(name, gameSize, IDManager.getNextGameID());
		games.add(newGame);
        GameChatLog gameChatLog = new GameChatLog();
		chatManager.addGameChatLog(newGame.getGameID(), gameChatLog);
		return newGame.getGameInfo();
	}

	/**
	 * create a new user and generates an authentication token
	 * @param user contains all parts of the user to be created
	 * @return newly created user
	 * @throws UserCreationException
	 */
	public User addUser(User user) throws UserCreationException {
		if(user.getUsername().length() < 4) throw new UserCreationException("Username too short!");
		if(user.getPassword().length() < 8) throw new UserCreationException("Password too short!");
		if(user.getFullName() == null || user.getFullName().length() < 1) user.setFullName(user.getUsername());

		for(User u : users) {
			if(u.getUsername().equals(user.getUsername()))
				throw new UserCreationException("User already exisits!");
		}

		User newUser = new User(user.getUsername(), user.getPassword(), user.getFullName(), IDManager.getNextUserID());
		generateToken(newUser);
		users.add(newUser);
		return newUser;
	}

	/**
	 * add a user to a game
	 * @param user user to join the game
	 * @param gameID ID of the game to be joined
	 * @param color color of the player
	 * @return updated game object
	 * @throws GameActionException
	 */
	public GameInfo joinGame(User user, int gameID, PlayerColor color) throws GameActionException {
		Game joinedGame = null;
		for(Game g : games) {
			if(g.getGameID() == gameID) {
				if(!g.canJoinWithColor(color))
					throw new GameActionException("Color not available!");
				g.addPlayer(user, color);
				joinedGame = g;
				break;
			}
		}

		if(joinedGame == null)
			throw new GameActionException("Could not join game!");
		return joinedGame.getGameInfo();
	}

	/**
	 * removes a player from the game.
	 * @param user contains user.id of user to remove
	 * @param gameID ID of the game to remove the user from
	 * @return true if removed, false otherwise
	 * @throws GameActionException
	 */
	public boolean removePlayer(User user, int gameID) throws GameActionException {
		for(Game g : games) {
			if(g.getGameID() == gameID) {
				return g.removePlayer(user);
			}
		}
		throw new GameActionException("Game does not exist!");
	}

	/**
	 * get a single game based on ID
	 * @param gameID ID of the game to be got
	 * @return game that done did get got
	 */
	public Game getGame(int gameID) {
		for(Game g : games) {
			if(g.getGameID() == gameID)
				return g;
		}
		return null;
	}

	public Set<GameInfo> getGames() {
		Set<GameInfo> gameInfos = new HashSet<GameInfo>();
		for(Game g : games) {
			gameInfos.add(g.getGameInfo());
		}
		return gameInfos;
	}

	public GameChatLog getChatLog(User user, int gameID) throws BadUserException {
		Game game = getGame(gameID);
		if(game.isPlayer(user.getUserID()))
			return chatManager.getMessages(gameID);
		throw new BadUserException("User not in game!");
	}

	public GameChatLog sendMessage(User user, Message message) throws BadUserException {
		Game game = getGame(message.getGameID());
		if(game.isPlayer(user.getUserID()))
			return chatManager.addMessage(message);
		throw new BadUserException("User not in game!");
	}
}
