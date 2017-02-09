package com.floorcorn.tickettoride.interfaces;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public interface IServer {
	// Contexts
	String LOGIN = "/login";
	String REGISTER = "/register";
	String GET_GAMES = "/getGames";
	String CREATE_GAME = "/createGame";
	String LEAVE_GAME = "/leaveGame";
	String JOIN_GAME = "/joinGame";

	/**
	 * method to log a user in.
	 * @pre user.username exists
	 * @pre user.password corresponds to user.username
	 *
	 * @param user only should consist of a username and password
	 * @return the user who was logged in if the username and password were correct
	 * @throws BadUserException contains a message about the failure
	 */
	public IUser login(IUser user) throws BadUserException;

	/**
	 * method to register a new user
	 * @pre user.username is unique
	 * @pre user.username.length > 4
	 * @pre user.password.length > 8
	 * @pre user.fullname set to user.username if user.fullname is null
	 *
	 * @param user contains username, password, and (optionally) fullname
	 *
	 * @return completed user that was registered
	 * @throws UserCreationException
	 */
	public IUser register(IUser user) throws UserCreationException;

	/**
	 * retrieves the set of IGames from the server
	 * @pre user.token is a valid authentication token
	 *
	 * @param user contains token that is valid
	 * @return set of all games that exist
	 * @throws BadUserException
	 */
	public Set<IGame> getGames(IUser user) throws BadUserException;

	/**
	 * will create a new game
	 * @pre user.token is valid authentication token
	 * @pre gameSize >= 2 and gameSize <= 5
	 *
	 * @param user contains valid authentication token
	 * @param name name of the game being created
	 * @param gameSize number of players in the game (2-5)
	 * @return newly created game object
	 * @throws BadUserException
	 */
	public IGame createGame(IUser user, String name, int gameSize) throws BadUserException;

	/**
	 * attempts to add the user to the specified game with the specified color.
	 * @pre user.token is valid authentication token
	 * @pre gameID is a game
	 *
	 * @param user contains valid authentication token
	 * @param gameID ID of the game the user wants to join
	 * @param color PlayerColor the user wants to be
	 * @return the updated game object containing the users player is returned.
	 * @throws GameActionException
	 * @throws BadUserException
	 */
	public IGame joinGame(IUser user, int gameID, Player.PlayerColor color) throws GameActionException, BadUserException;

	/**
	 * will remove the player from the game or cancel it if the player made the game.
	 * @pre user.token is valid authentication token
	 * @pre user is a player in the game with gameID
	 * @pre gameID is a game
	 * @pre the game has not started/finished
	 *
	 * @param user contains valid authentication token
	 * @param gameID ID of the game the player wants to leave/end
	 * @return true if player successfully left/ended the game, false if player could not leave.
	 * @throws BadUserException
	 * @throws GameActionException
	 */
	public boolean leaveGame(IUser user, int gameID) throws BadUserException, GameActionException;
	//TODO could return the game so the model is updated?
}
