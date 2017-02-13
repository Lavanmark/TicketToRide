package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.serverModel.User;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerFacade implements IServer {

	private ServerModel model;

	private static ServerFacade instance = null;
	public static ServerFacade getInstance() {
		if(instance == null)
			instance = new ServerFacade();
		return instance;
	}
	private ServerFacade() { model = new ServerModel(); }

	@Override
	public IUser login(IUser user) throws BadUserException {
		if(user != null)
			return model.authenticate(user.getUsername(), user.getPassword());
		throw new BadUserException("User was null!");
	}

	@Override
	public IUser register(IUser user) throws UserCreationException {
		if(user != null)
			return model.addUser(user);
		throw new UserCreationException("User was null!");
	}

	@Override
	public IGame getGame(IUser user, int gameID) throws BadUserException {
		if(user != null)
			return model.getGame(gameID);
		throw new BadUserException("User was null!");
	}

	@Override
	public Set<IGame> getGames(IUser user) throws BadUserException {
		if(model.authenticate(user.getToken()) != null)
			return model.getGames();
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public IGame createGame(IUser user, String name, int gameSize) throws BadUserException {
		if(model.authenticate(user.getToken()) != null)
			return model.addGame(name, gameSize);
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public IGame joinGame(IUser user, int gameID, Player.PlayerColor color) throws BadUserException, GameActionException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.joinGame(user, gameID, color);
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public boolean leaveGame(IUser user, int gameID) throws BadUserException, GameActionException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.removePlayer(user, gameID);
		throw new BadUserException("Could not Authenticate User!");
	}
}
