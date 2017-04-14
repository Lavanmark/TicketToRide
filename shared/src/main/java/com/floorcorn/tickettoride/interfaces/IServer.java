package com.floorcorn.tickettoride.interfaces;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.CommandRequestException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public interface IServer {
	// Contexts
	String LOGIN = "/login";
	String REGISTER = "/register";
	String GET_GAME = "/getAGame";
	String GET_GAMES = "/getGames";
	String CREATE_GAME = "/createGame";
	String LEAVE_GAME = "/leaveGame";
	String JOIN_GAME = "/joinGame";
	String SEND_COMMAND = "/command";
	String GET_COMMANDS = "/getCommands";
	String SEND_CHAT = "/sendChat";
	String GET_CHAT = "/getChat";

	/**
	 * method to log a user in.
	 * @pre user.username exists
	 * @pre user.password corresponds to user.username
	 *
	 * @param user only should consist of a username and password
	 * @return the user who was logged in if the username and password were correct, null otherwise
	 * @throws BadUserException contains a message about the failure (only if user is not formatted correctly)
	 */
	User login(User user) throws BadUserException;

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
	User register(User user) throws UserCreationException;

	/**
	 * retrieves the set of Games from the server
	 * @pre user.token is a valid authentication token
	 *
	 * @param user contains token that is valid
	 * @return set of all games that exist
	 * @throws BadUserException
	 */
	Set<GameInfo> getGames(User user) throws BadUserException;

	/**
	 * retrives a single game based on gameID
	 * @pre user.token is a valid authentication token
	 * @pre gameID is a valid Game's ID
	 *
	 * @param user contains token that is valid
	 * @param gameID ID number of game to be retrieved
	 * @return Game object corresponding to the gameID, null if game does not exist
	 * @throws BadUserException
	 */
	Game getGame(User user, int gameID) throws BadUserException;


	ArrayList<ICommand> getCommandsSince(User user, int gameID, int lastCommand) throws BadUserException, GameActionException, CommandRequestException;

	ArrayList<ICommand> doCommand(User user, ICommand command) throws BadUserException, GameActionException, CommandRequestException;

	/**
	 * will create a new game
	 * @pre user.token is valid authentication token
	 * @pre gameSize >= 2 and gameSize <= 5
	 *
	 * @param user contains valid authentication token
	 * @param gameName name of the game being created
	 * @param gameSize number of players in the game (2-5)
	 * @return set of gameinfo objects with the new game
	 * @throws BadUserException
	 */
	GameInfo createGame(User user, String gameName, int gameSize) throws BadUserException, GameCreationException;

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
	GameInfo joinGame(User user, int gameID, PlayerColor color) throws GameActionException, BadUserException;

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
	boolean leaveGame(User user, int gameID) throws BadUserException, GameActionException;

	GameChatLog getChatLog(User user, GameInfo gameInfo) throws BadUserException;

	GameChatLog sendChatMessage(User user, Message message) throws BadUserException;
}
