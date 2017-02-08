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

	public IUser login(IUser user) throws BadUserException;
	public IUser register(IUser user) throws UserCreationException;
	public Set<IGame> getGames(IUser user) throws BadUserException;
	public IGame createGame(IUser user, String name, int gameSize) throws GameActionException, BadUserException;
	public IGame joinGame(IUser user, int gameID, Player.PlayerColor color) throws GameActionException, BadUserException;
	public boolean leaveGame(IUser user, int gameID) throws BadUserException;
}
