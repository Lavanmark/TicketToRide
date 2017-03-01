package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.CommandRequest;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerProxy implements IServer {

	private ClientCommunicator clientComm = new ClientCommunicator();

	@Override
	public User login(User user) throws BadUserException {
		Results res = clientComm.send(LOGIN, user, null);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (User) res.getResult();
			//return Serializer.getInstance().deserializeUser(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public User register(User user) throws UserCreationException {
		Results res = clientComm.send(REGISTER, user, null);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (User) res.getResult();
			//return Serializer.getInstance().deserializeUser(reser);
		} else if(res.getException(UserCreationException.class.getSimpleName()) != null)
			throw new UserCreationException(res.getException(UserCreationException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public Game getGame(User user, int gameID) throws BadUserException {
		Results res = clientComm.send(GET_GAME, new GameInfo(gameID), user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (Game) res.getResult();
			//return Serializer.getInstance().deserializeGame(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public ArrayList<ICommand> getCommandsSince(User user, int gameID, int lastCommand) throws BadUserException, GameActionException {
		Results res = clientComm.send(GET_COMMANDS, new CommandRequest(gameID, lastCommand), user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()){
			return (ArrayList<ICommand>) res.getResult();
			//return Serializer.getInstance().deserializeCommandList(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public ArrayList<ICommand> sendCommand(User user, ICommand command) throws BadUserException, GameActionException {
		Results res = clientComm.send(SEND_COMMAND, command, user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()){
			return (ArrayList<ICommand>) res.getResult();
			//return Serializer.getInstance().deserializeCommandList(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public Set<GameInfo> getGames(User user) throws BadUserException {
		Results res = clientComm.send(GET_GAMES, null, user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (Set<GameInfo>) res.getResult();
			//return Serializer.getInstance().deserializeGameInfoSet(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public GameInfo createGame(User user, String name, int gameSize) throws BadUserException {
		Results res = clientComm.send(CREATE_GAME, new GameInfo(name, gameSize), user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (GameInfo) res.getResult();
			//return Serializer.getInstance().deserializeGameInfo(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public GameInfo joinGame(User user, int gameID, PlayerColor color) throws BadUserException, GameActionException {
		Results res = clientComm.send(JOIN_GAME, new PlayerInfo(user.getUserID(), user.getFullName(), gameID, color), user);
		//String reser = Serializer.getInstance().serialize(res.getResult());
		if(res.isSuccess()) {
			return (GameInfo) res.getResult();
			//return Serializer.getInstance().deserializeGameInfo(reser);
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()).getMessage());
		return null;
	}

	@Override
	public boolean leaveGame(User user, int gameID) throws BadUserException, GameActionException {
		Results res = clientComm.send(LEAVE_GAME, new GameInfo(gameID), user);
		if(res.isSuccess())
			return true;
		else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()).getMessage());
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()).getMessage());
		return false;
	}

	public void setHost(String host) {
		clientComm.setHost(host);
	}

	public void setPort(String port) {
		clientComm.setPort(port);
	}
}
