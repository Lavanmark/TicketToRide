package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.commands.CommandManager;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.CommandRequestException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.ServerModel;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerFacade implements IServer {

	private ServerModel model;
	private CommandManager commandManager;
	
	public static IDAOFactory daoFactory = null;
	
	//If max_commands == -1 then store unlimited commands
	public static int max_commands = -1;
	
	private static ServerFacade instance = null;
	public static ServerFacade getInstance() {
		if(instance == null)
			instance = new ServerFacade();
		return instance;
	}
	private ServerFacade() {
		model = new ServerModel(daoFactory);
		commandManager = new CommandManager(daoFactory);
	}

	@Override
	public User login(User user) throws BadUserException {
		if(user != null)
			return model.authenticate(user.getUsername(), user.getPassword());
		throw new BadUserException("User was null!");
	}

	@Override
	public User register(User user) throws UserCreationException {
		if(user != null)
			return model.addUser(user);
		throw new UserCreationException("User was null!");
	}

	@Override
	public Game getGame(User user, int gameID) throws BadUserException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.getGame(gameID).getCensoredGame(user);
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public ArrayList<ICommand> getCommandsSince(User user, int gameID, int lastCommand) throws BadUserException, GameActionException, CommandRequestException {
		if((user = model.authenticate(user.getToken())) != null) {
			return commandManager.getCommandsSince(user, model.getGame(gameID), lastCommand);
		}
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public ArrayList<ICommand> doCommand(User user, ICommand command) throws BadUserException, GameActionException, CommandRequestException {
		if((user = model.authenticate(user.getToken())) != null) {
			return commandManager.doCommand(user, model.getGame(command.getGameID()), command);
		}
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public Set<GameInfo> getGames(User user) throws BadUserException {
		if(model.authenticate(user.getToken()) != null)
			return model.getGames();
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public GameInfo createGame(User user, String name, int gameSize) throws BadUserException, GameCreationException {
		if(model.authenticate(user.getToken()) != null)
			return model.addGame(name, gameSize);
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public GameInfo joinGame(User user, int gameID, PlayerColor color) throws BadUserException, GameActionException {
		if((user = model.authenticate(user.getToken())) != null) {
			GameInfo game = model.joinGame(user, gameID, color);
			if(game.hasStarted() && !game.isFinished())
				commandManager.startGame(model.getGame(game.getGameID()));
			return game;
		}
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public boolean leaveGame(User user, int gameID) throws BadUserException, GameActionException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.removePlayer(user, gameID);
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public GameChatLog getChatLog(User user, GameInfo gameInfo) throws BadUserException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.getChatLog(user, gameInfo.getGameID());
		throw new BadUserException("Could not Authenticate User!");
	}

	@Override
	public GameChatLog sendChatMessage(User user, Message message) throws BadUserException {
		if((user = model.authenticate(user.getToken())) != null)
			return model.sendMessage(user, message);
		throw new BadUserException("Could not Authenticate User!");
	}
}
