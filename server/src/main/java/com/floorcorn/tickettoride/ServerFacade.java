package com.floorcorn.tickettoride;

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
	public IUser login(IUser user) {
		if(user != null)
			return model.authenticate(user.getUsername(), user.getPassword());
		return null;
	}

	@Override
	public IUser register(IUser user) throws UserCreationException {
		if(user != null)
			return model.addUser(user);
		throw new UserCreationException("User was null!");
	}

	@Override
	public Set<IGame> getGames(IUser user) {
		if(model.authenticate(user.getToken()) != null)
			return model.getGames();
		return null;
	}

	@Override
	public IGame createGame(IUser user, String name, int gameSize) {
		if(model.authenticate(user.getToken()) != null)
			return model.addGame(name, gameSize);
		return null;
	}

	@Override
	public IGame joinGame(IUser user, int gameID, Player.PlayerColor color) {
		if((user = model.authenticate(user.getToken())) != null)
			return model.joinGame(user, gameID, color);
		return null;
	}

	@Override
	public boolean leaveGame(IUser user, int gameID) {
		if((user = model.authenticate(user.getToken())) != null)
			return model.removePlayer(user, gameID);
		return false;
	}
}
