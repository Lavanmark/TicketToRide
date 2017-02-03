package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerProxy implements IServer {

	private ClientCommunicator clientComm = new ClientCommunicator();

	@Override
	public User login(User user) {
		Results res = clientComm.send("/login", user);
		if(res.isSuccess()) {
			Object obj = res.getResults().get(0);
			if(obj instanceof User)
				return (User)obj;
		}
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

	public void setHost(String host) {
		clientComm.setHost(host);
	}

	public void setPort(String port) {
		clientComm.setPort(port);
	}
}
