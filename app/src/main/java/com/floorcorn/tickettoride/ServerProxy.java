package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;
import com.google.gson.reflect.TypeToken;

import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerProxy implements IServer {

	private ClientCommunicator clientComm = new ClientCommunicator();

	@Override
	public User login(User user) {
		Results res = clientComm.send(LOGIN, user, null);
		if(res.isSuccess()) {
			String reser = Serializer.getInstance().serialize(res.getResult());
			return Serializer.getInstance().deserializeUser(reser);
		} else {
			String reser = Serializer.getInstance().serialize(res.getResult());
			System.err.println(((BadUserException)Serializer.getInstance().deserialze(reser, BadUserException.class)).getMessage());
		}
		return null;
	}

	@Override
	public User register(User user) {
		Results res = clientComm.send(REGISTER, user, null);
		if(res.isSuccess()) {
			String reser = Serializer.getInstance().serialize(res.getResult());
			return Serializer.getInstance().deserializeUser(reser);
		} else {
			String reser = Serializer.getInstance().serialize(res.getResult());
			System.err.println(((BadUserException)Serializer.getInstance().deserialze(reser, BadUserException.class)).getMessage());
		}
		return null;
	}

	@Override
	public Set<IGame> getGames(User user) {
		Results res = clientComm.send(GET_GAMES, null, user);
		if(res.isSuccess()) {
			String reser = Serializer.getInstance().serialize(res.getResult());
			return Serializer.getInstance().deserializeGameList(reser);
		} else {
			String reser = Serializer.getInstance().serialize(res.getResult());
			System.err.println(((BadUserException)Serializer.getInstance().deserialze(reser, BadUserException.class)).getMessage());
		}
		return null;
	}

	@Override
	public IGame createGame(User user, String name, int gameSize) {
		Results res = clientComm.send(CREATE_GAME, new Game(name, gameSize), user);
		if(res.isSuccess()) {
			String reser = Serializer.getInstance().serialize(res.getResult());
			return Serializer.getInstance().deserializeIGame(reser);
		} else {
			String reser = Serializer.getInstance().serialize(res.getResult());
			System.err.println(((BadUserException)Serializer.getInstance().deserialze(reser, BadUserException.class)).getMessage());
		}
		return null;
	}

	@Override
	public IGame joinGame(User user, int gameID, Player.PlayerColor color) {
		Results res = clientComm.send(JOIN_GAME, new Player(user.getUserID(), gameID, color), user);
		if(res.isSuccess()) {
			String reser = Serializer.getInstance().serialize(res.getResult());
			return Serializer.getInstance().deserializeIGame(reser);
		} else {
			String reser = Serializer.getInstance().serialize(res.getResult());
			System.err.println(((BadUserException)Serializer.getInstance().deserialze(reser, BadUserException.class)).getMessage());
		}
		return null;
	}

	@Override
	public boolean leaveGame(User user, int gameID) {
		Results res = clientComm.send(LEAVE_GAME, new Game(gameID), user);
		return res.isSuccess();
	}

	public void setHost(String host) {
		clientComm.setHost(host);
	}

	public void setPort(String port) {
		clientComm.setPort(port);
	}
}
