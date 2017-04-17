package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.CommandRequest;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.CommandRequestException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerProxy implements IServer {

	private ClientCommunicator clientComm = new ClientCommunicator();

	@Override
	public User login(User user) throws BadUserException {
		Results res = clientComm.send(LOGIN, user, null);
		if(res.isSuccess())
			return (User) res.getResult();
		else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		return null;
	}

	@Override
	public User register(User user) throws UserCreationException {
		Results res = clientComm.send(REGISTER, user, null);
		if(res.isSuccess()) {
			return (User) res.getResult();
		} else if(res.getException(UserCreationException.class.getSimpleName()) != null)
			throw new UserCreationException(res.getException(UserCreationException.class.getSimpleName()));
		return null;
	}

	@Override
	public Game getGame(User user, int gameID) throws BadUserException {
		Results res = clientComm.send(GET_GAME, new GameInfo(gameID), user);
		if(res.isSuccess()) {
			return (Game) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		return null;
	}

	@Override
	public ArrayList<ICommand> getCommandsSince(User user, int gameID, int lastCommand) throws BadUserException, GameActionException, CommandRequestException {
		Results res = clientComm.send(GET_COMMANDS, new CommandRequest(gameID, lastCommand), user);
		if(res.isSuccess()){
			return (ArrayList<ICommand>) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()));
		else if(res.getException(CommandRequestException.class.getSimpleName()) != null)
			throw new CommandRequestException(res.getException(CommandRequestException.class.getSimpleName()));
		return null;
	}

	@Override
	public ArrayList<ICommand> doCommand(User user, ICommand command) throws BadUserException, GameActionException, CommandRequestException {
		Results res = clientComm.send(SEND_COMMAND, command, user);
		if(res.isSuccess()){
			return (ArrayList<ICommand>) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()));
		else if(res.getException(CommandRequestException.class.getSimpleName()) != null)
			throw new CommandRequestException(res.getException(CommandRequestException.class.getSimpleName()));
		return null;
	}

	@Override
	public Set<GameInfo> getGames(User user) throws BadUserException {
		Results res = clientComm.send(GET_GAMES, null, user);
		if(res.isSuccess()) {
			return (Set<GameInfo>) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		return null;
	}

	@Override
	public GameInfo createGame(User user, String name, int gameSize) throws BadUserException, GameCreationException {
		Results res = clientComm.send(CREATE_GAME, new GameInfo(name, gameSize), user);
		if(res.isSuccess()) {
			return (GameInfo) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		else if(res.getException(GameCreationException.class.getSimpleName()) != null)
			throw new GameCreationException(res.getException(GameCreationException.class.getSimpleName()));
		return null;
	}

	@Override
	public GameInfo joinGame(User user, int gameID, PlayerColor color) throws BadUserException, GameActionException {
		Results res = clientComm.send(JOIN_GAME, new PlayerInfo(user.getUserID(), user.getFullName(), gameID, color), user);
		if(res.isSuccess()) {
			return (GameInfo) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()));
		return null;
	}

	@Override
	public boolean leaveGame(User user, int gameID) throws BadUserException, GameActionException {
		Results res = clientComm.send(LEAVE_GAME, new GameInfo(gameID), user);
		if(res.isSuccess())
			return true;
		else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		else if(res.getException(GameActionException.class.getSimpleName()) != null)
			throw new GameActionException(res.getException(GameActionException.class.getSimpleName()));
		return false;
	}

	@Override
	public GameChatLog getChatLog(User user, GameInfo gameInfo) throws BadUserException {
		Results res = clientComm.send(GET_CHAT, gameInfo, user);
		if(res.isSuccess()) {
			return (GameChatLog) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		return null;
	}

	@Override
	public GameChatLog sendChatMessage(User user, Message message) throws BadUserException {
		Results res = clientComm.send(SEND_CHAT, message, user);
		if(res.isSuccess()) {
			return (GameChatLog) res.getResult();
		} else if(res.getException(BadUserException.class.getSimpleName()) != null)
			throw new BadUserException(res.getException(BadUserException.class.getSimpleName()));
		return null;
	}

	public void setHost(String host) {
		clientComm.setHost(host);
	}

	public void setPort(String port) {
		clientComm.setPort(port);
	}
}
