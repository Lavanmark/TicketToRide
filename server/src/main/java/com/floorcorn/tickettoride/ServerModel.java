package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.serverModel.Game;
import com.floorcorn.tickettoride.serverModel.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerModel {
	private Set<IGame> games; // Stores all games ever. If game is canceled or ends, it remains here with the players so users can get this info.
	private Set<User> users; // Stores all users ever.
	private SecureRandom random;

	public ServerModel() {
		games = new HashSet<IGame>();
		users = new HashSet<User>();
		random = new SecureRandom();
	}

	private void generateToken(IUser u) {
		u.setToken(new BigInteger(130, random).toString(32));
	}

	/**
	 * authenticate a user using username and password of a given user and generates authentication token
	 * @param username valid username of a user
	 * @param password valid password of a user
	 * @return user who was logged in, or null if bad credentials
	 */
	public User authenticate(String username, String password) {
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				if(u.getPassword().equals(password)) {
					generateToken(u);
					return u;
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * authenticates a user using an authentication token.
	 * @param token token of specified user to log on
	 * @return user with corresponding token if login successful, null if not
	 */
	public User authenticate(String token) {
		for(User u : users) {
			if(u.getToken().equals(token))
				return u;
		}
		return null;
	}

	/**
	 * create a new game with name and gamesize *games with non original names can still be made
	 * @param name string that should be the displayed name of the game
	 * @param gameSize number of players in the game
	 * @return the newly created game
	 */
	public IGame addGame(String name, int gameSize) {
		IGame newGame = new Game(name, gameSize);
		games.add(newGame);
		return newGame;
	}

	/**
	 * create a new user and generates an authentication token
	 * @param user contains all parts of the user to be created
	 * @return newly created user
	 * @throws UserCreationException
	 */
	public User addUser(IUser user) throws UserCreationException {
		if(user.getUsername().length() < 4) throw new UserCreationException("Username too short!");
		if(user.getPassword().length() < 8) throw new UserCreationException("Password too short!");
		if(user.getFullName() == null || user.getFullName().length() < 1) user.setFullName(user.getUsername());

		for(User u : users) {
			if(u.getUsername().equals(user.getUsername()))
				throw new UserCreationException("User already exisits!");
		}

		User newUser = new User(user.getUsername(), user.getPassword(), user.getFullName());
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
	public IGame joinGame(IUser user, int gameID, Player.PlayerColor color) throws GameActionException {
		for(IGame g : games) {
			if(g.getGameID() == gameID) {
				g.addPlayer(user, color);
				return g;
			}
		}
		throw new GameActionException("Could not join game!");
	}

	/**
	 * removes a player from the game.
	 * @param user contains user.id of user to remove
	 * @param gameID ID of the game to remove the user from
	 * @return true if removed, false otherwise
	 * @throws GameActionException
	 */
	public boolean removePlayer(IUser user, int gameID) throws GameActionException {
		for(IGame g : games) {
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
	public IGame getGame(int gameID) {
		for(IGame g : games) {
			if(g.getGameID() == gameID)
				return g;
		}
		return null;
	}

	public Set<IGame> getGames() {
		return games;
	}
}
