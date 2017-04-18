package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;
import com.floorcorn.tickettoride.IDAOFactory;
import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IGameDTO;
import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.IUserDTO;
import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.commands.ClaimRouteCmd;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tyler on 2/2/2017.
 */

public class ServerModel {
	private Set<Game> games; // Stores all games ever. If game is canceled or ends, it remains here with the players so users can get this info.
	private Set<User> users; // Stores all users ever.
	private SecureRandom random;
    private ChatManager chatManager;
	private IUserDAO userDAO;
	private ICommandDAO commandDAO;
	private IGameDAO gameDAO;

	public ServerModel(IDAOFactory factory) {
		games = new HashSet<>();
		users = new HashSet<>();
		random = new SecureRandom();
        chatManager = new ChatManager();
		if(factory != null) {
			userDAO = factory.getUserDAOInstance();
			commandDAO = factory.getCommandDAOInstance();
			gameDAO = factory.getGameDAOInstance();
			loadFromDatabase();
		}
	}
	
	private void loadFromDatabase() {
		if(gameDAO != null && userDAO != null && commandDAO != null) {
			ServerFacade.daoFactory.startTransaction();
			List<IUserDTO> userDTOList = userDAO.getAll();
			for(IUserDTO userDTO : userDTOList) {
				User user = new User(userDTO.getUserName(), userDTO.getPassword(), userDTO.getFullName());
				user.setUserID(userDTO.getID());
				users.add(user);
			}
			
			List<IGameDTO> gameDTOList = gameDAO.getAll();

			for(IGameDTO gameDTO : gameDTOList) {
				Game game = Serializer.getInstance().deserializeGame(gameDTO.getData());
				if(game != null) {
					List<ICommandDTO> cmdDTOs = commandDAO.getAllForGame(game.getGameID());
					if(cmdDTOs == null)
						continue;
					List<ICommand> unorderedCmds = new ArrayList<>();
					for(ICommandDTO cmdDTO : cmdDTOs)
						unorderedCmds.add(Serializer.getInstance().deserializeCommand(cmdDTO.getData()));
					
					//Insertion sort from Wikipedia
					for(int i = 1; i < unorderedCmds.size(); i++) {
						ICommand cmd = unorderedCmds.get(i);
						int j = i - 1;
						while( j >= 0 && unorderedCmds.get(j).getCmdID() > cmd.getCmdID()) {
							unorderedCmds.set(j+1, unorderedCmds.get(j));
							j = j - 1;
						}
						unorderedCmds.set(j+1, cmd);
					}
					
					for(ICommand cmd : unorderedCmds) {
						if(cmd.getCmdID() <= game.getLatestCommandID())
							continue;
						if(cmd instanceof ClaimRouteCmd)
							((ClaimRouteCmd)cmd).reloadExecute(game);
						else
							cmd.execute(game);
						game.addCommand(cmd);
					}
					games.add(game);
					GameChatLog gameChatLog = new GameChatLog();
					chatManager.addGameChatLog(game.getGameID(), gameChatLog);
				}
			}
			ServerFacade.daoFactory.endTransaction(false);
		}
	}

	private void generateToken(User u) {
		u.setToken(new BigInteger(130, random).toString(32));
	}

	/**
	 * authenticate a user using username and password of a given user and generates authentication token
	 * @param username valid username of a user
	 * @param password valid password of a user
	 * @return user who was logged in, or null if bad credentials
	 */
	public User authenticate(String username, String password) throws BadUserException {
		for(User u : users) {
			if(u.getUsername().equals(username)) {
				if(u.getPassword().equals(password)) {
					generateToken(u);
					return u;
				}
				throw new BadUserException("Invalid username or password!");
			}
		}
		throw new BadUserException("Invalid username or password!");
	}

	/**
	 * authenticates a user using an authentication token.
	 * @param token token of specified user to log on
	 * @return user with corresponding token if login successful, null if not
	 */
	public User authenticate(String token) throws BadUserException {
		for(User u : users) {
			if(token.equals(u.getToken()))
				return u;
		}
		throw new BadUserException("Invalid user token!");
	}

	/**
	 * create a new game with name and gamesize *games with non original names can still be made
	 * @param name string that should be the displayed name of the game
	 * @param gameSize number of players in the game
	 * @return the newly created game
	 */
	public GameInfo addGame(String name, int gameSize) throws GameCreationException {
		Game newGame = new Game(name, gameSize);
		if(gameDAO != null) {
			ServerFacade.daoFactory.startTransaction();
			IGameDTO gameDTO = ServerFacade.daoFactory.getGameDTOInstance();
			gameDTO.setID(newGame.getGameID());
			gameDTO.setData(Serializer.getInstance().serialize(newGame));
			if(gameDAO.create(gameDTO)) {
				ServerFacade.daoFactory.endTransaction(true);
			} else {
				ServerFacade.daoFactory.endTransaction(false);
				throw new GameCreationException("Could not create game in Database.");
			}
			newGame.setGameID(gameDTO.getID());
		} else {
			newGame.setGameID(IDManager.getNextGameID());
		}
		
		games.add(newGame);
		
		GameChatLog gameChatLog = new GameChatLog();
		chatManager.addGameChatLog(newGame.getGameID(), gameChatLog);
		
		return newGame.getGameInfo();
	}

	/**
	 * create a new user and generates an authentication token
	 * @param user contains all parts of the user to be created
	 * @return newly created user
	 * @throws UserCreationException
	 */
	public User addUser(User user) throws UserCreationException {
		if(user.getUsername().length() < 4) throw new UserCreationException("Username too short!");
		if(user.getPassword().length() < 8) throw new UserCreationException("Password too short!");
		if(user.getFullName() == null || user.getFullName().length() < 1) user.setFullName(user.getUsername());

		for(User u : users) {
			if(u.getUsername().equals(user.getUsername()))
				throw new UserCreationException("User already exisits!");
		}

		User newUser = new User(user.getUsername(), user.getPassword(), user.getFullName());
		
		if(userDAO != null) {
			ServerFacade.daoFactory.startTransaction();
			IUserDTO userDTO = ServerFacade.daoFactory.getUserDTOInstance();
			userDTO.setUserName(user.getUsername());
			userDTO.setPassword(user.getPassword());
			userDTO.setFullName(user.getFullName());
			if(userDAO.create(userDTO)) {
				ServerFacade.daoFactory.endTransaction(true);
				newUser.setUserID(userDTO.getID());
			} else {
				ServerFacade.daoFactory.endTransaction(false);
				throw new UserCreationException("Could not create User in Database.");
			}
		} else {
			newUser.setUserID(IDManager.getNextUserID());
		}
		generateToken(newUser);
		users.add(newUser);
		return newUser;
	}

	/**
	 * add a user to a game
	 * @param user user to join the game
	 * @param gameID ID of the game to be joined
	 * @param color color of the player
	 * @return updated game object
	 * @throws GameActionException
	 */
	public GameInfo joinGame(User user, int gameID, PlayerColor color) throws GameActionException {
		Game joinedGame = null;
		for(Game g : games) {
			if(g.getGameID() == gameID) {
				if(!g.canJoinWithColor(color))
					throw new GameActionException("Color not available!");
				g.addPlayer(user, color);
				joinedGame = g;
				break;
			}
		}

		if(joinedGame == null)
			throw new GameActionException("Could not join game!");
		
		if(gameDAO != null) {
			ServerFacade.daoFactory.startTransaction();
			IGameDTO gameDTO = ServerFacade.daoFactory.getGameDTOInstance();
			gameDTO.setID(joinedGame.getGameID());
			gameDTO.setData(Serializer.getInstance().serialize(joinedGame));
			if(gameDAO.update(gameDTO))
				ServerFacade.daoFactory.endTransaction(true);
			else
				ServerFacade.daoFactory.endTransaction(false);
		}
		
		return joinedGame.getGameInfo();
	}

	/**
	 * removes a player from the game.
	 * @param user contains user.id of user to remove
	 * @param gameID ID of the game to remove the user from
	 * @return true if removed, false otherwise
	 * @throws GameActionException
	 */
	public boolean removePlayer(User user, int gameID) throws GameActionException {
		for(Game g : games) {
			if(g.getGameID() == gameID) {
				boolean success = g.removePlayer(user);
				if(success && gameDAO != null) {
					ServerFacade.daoFactory.startTransaction();
					IGameDTO gameDTO = ServerFacade.daoFactory.getGameDTOInstance();
					gameDTO.setID(g.getGameID());
					gameDTO.setData(Serializer.getInstance().serialize(g));
					if(gameDAO.update(gameDTO))
						ServerFacade.daoFactory.endTransaction(true);
					else
						ServerFacade.daoFactory.endTransaction(false);
				}
				return success;
			}
		}
		throw new GameActionException("Game does not exist!");
	}

	/**
	 * get a single game based on ID
	 * @param gameID ID of the game to be got
	 * @return game that done did get got
	 */
	public Game getGame(int gameID) {
		for(Game g : games) {
			if(g.getGameID() == gameID)
				return g;
		}
		return null;
	}

	public Set<GameInfo> getGames() {
		Set<GameInfo> gameInfos = new HashSet<>();
		for(Game g : games) {
			gameInfos.add(g.getGameInfo());
		}
		return gameInfos;
	}

	public GameChatLog getChatLog(User user, int gameID) throws BadUserException {
		Game game = getGame(gameID);
		if(game.isPlayer(user.getUserID()))
			return chatManager.getMessages(gameID);
		throw new BadUserException("User not in game!");
	}

	public GameChatLog sendMessage(User user, Message message) throws BadUserException {
		Game game = getGame(message.getGameID());
		if(game.isPlayer(user.getUserID()))
			return chatManager.addMessage(message);
		throw new BadUserException("User not in game!");
	}
}
