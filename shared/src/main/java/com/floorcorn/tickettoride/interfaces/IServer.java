package com.floorcorn.tickettoride.interfaces;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public interface IServer {
	public User login(User user);
	public User register(User user);
	public Set<IGame> getGames(User user);
	public IGame createGame(User user, String name, int gameSize);
	public IGame joinGame(User user, int gameID, Player.PlayerColor color);
	public boolean leaveGame(User user, int gameID);
}
