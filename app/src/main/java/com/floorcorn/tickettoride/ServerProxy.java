package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerProxy implements IServer {

	@Override
	public User login(User user) {
		return null;
	}

	@Override
	public User register(User user) {
		return null;
	}

	@Override
	public Set<IGame> getGames(User user) {
		return null;
	}

	@Override
	public IGame createGame(User user, String name, int gameSize) {
		return null;
	}

	@Override
	public IGame joinGame(User user, int gameID, Player.PlayerColor color) {
		return null;
	}

	@Override
	public boolean leaveGame(User user, int gameID) {
		return false;
	}
}
