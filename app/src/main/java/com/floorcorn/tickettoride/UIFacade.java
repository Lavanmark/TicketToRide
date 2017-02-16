package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.IGame;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Observer;
import java.util.Set;

/**
 * @author Created by Tyler on 2/4/2017.
 *
 * @author Joseph Hansen
 */

public class UIFacade {

    // Instance variables and other class-specific things.

    private ClientModel clientModelRoot;
    private ServerProxy serverProxy;

    // TODO: add all the types of sorting we may want
    public enum GameSortStyle { ASC_GAMEID, DESC_GAMEID };

    // Things relating to private constructor and singleton pattern.

    private UIFacade() {
        clientModelRoot = new ClientModel();
        serverProxy = new ServerProxy();
	    serverProxy.setPort("8080");
        serverProxy.setHost("10.24.64.162");
    }
    private static UIFacade instance = null;
    public static UIFacade getInstance() {
        if (instance == null)
            instance = new UIFacade();
        return instance;
    }

    // Login and register related.

    /**
     * Prepares a User object for parameter to login(...) in ServerProxy. Calls login(...).
     * Throws a BadUserException if server side cannot authenticate given the parameters.
     * @param username String username
     * @param password String password
     */
    public void login(String username, String password) throws BadUserException {
        IUser user = new User(username, password);
        user = serverProxy.login(user);
	    clientModelRoot.setCurrentUser(user);
        requestGames();
    }

    /**
     * Prepares a User object for parameter to register(...) in ServerProxy. Calls register(...).
     * Throws UserCreationException if server side cannot register the user with the given
     * parameters.
     * @param username String username
     * @param password String password
     * @param firstname String firstname
     * @param lastname String lastname
     */
    public void register(String username, String password, String firstname, String lastname) throws UserCreationException {
        IUser user = new User(username, password, firstname + " " + lastname);
        user = serverProxy.register(user);
	    clientModelRoot.setCurrentUser(user);
        try {
            requestGames();
        } catch(BadUserException e) {
            e.printStackTrace();
        }
    }

    // User and game related.

    /**
     * Gets the current user from the ClientModel.
     * @return User object from ClientModel
     */
    public IUser getUser() {
        return clientModelRoot.getCurrentUser();
    }

    /**
     * Returns the games from the ClientModel.
     * @return Set of IGame objects from the ClientModel
     */
    public Set<IGame> getGames() {
        return clientModelRoot.getGames();
    }

    /**
     * Returns the games that the user has joined.
     * @param user User object
     * @return Set of IGame objects from the ClientModel
     */
    public Set<IGame> getGames(IUser user) {
	    user = new User(user);
        return clientModelRoot.getGames(user);
    }

    /**
     * Sorts some games according to the sort style.
     * @param games Set of IGame objects to sort
     * @param sortStyle GameSortStyle enum variable designates how to sort
     * @return Same IGame objects, but sorted
     */
    private List<IGame> sortGames(Set<IGame> games, GameSortStyle sortStyle) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the games from ClientModel and sorts them before returning. See GameSortStyle enum.
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of IGame objects from the ClientModel, sorted
     */
    public List<IGame> getGames(GameSortStyle sortStyle) {
        Set<IGame> gamesSet = getGames();
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Gets the games from the ClientModel that the user has joined. Sorts them before
     * returning. See GameSortStyle enum.
     * @param user User object
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of IGame objects from the ClientModel, sorted
     */
    public List<IGame> getGames(IUser user, GameSortStyle sortStyle) {
	    user = new User(user);
        Set<IGame> gamesSet = getGames(user);
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Returns IGame that matches the gameID parameter. Will return null if games returned from
     * ClientModel is null or if gameID not found in games.
     * @param gameID int ID
     * @return IGame matching gameID from ClientModel games; can be null
     */
    public IGame getGame(int gameID) {
        Set<IGame> games = getGames();
        if (games == null) return null;
        for (IGame game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    /**
     * Returns the IGame from ClientModel.getCurrentGame().
     * @return IGame object
     */
    public IGame getCurrentGame() {
        return clientModelRoot.getCurrentGame();
    }

    /**
     * Updates the current game in the ClientModel. Throws an exception if parameter is null.
     * @param game IGame object
     */
    public void setCurrentGame(IGame game) {
        if (game == null) {
            throw new IllegalArgumentException("The parameter passed to setCurrentGame was null.");
        }
        clientModelRoot.setCurrentGame(game);
    }

	public void requestCurrentGame() throws BadUserException, InvalidParameterException {
		if(getCurrentGame() == null)
			throw new InvalidParameterException("No game currently selected!");
		IGame cgame = serverProxy.getGame(clientModelRoot.getCurrentUser(), getCurrentGame().getGameID());
        System.out.println("clientModelRoot current game in request current game: " + clientModelRoot.getCurrentGame());
		if(cgame != null)
			clientModelRoot.setCurrentGame(cgame);
		else
			throw new InvalidParameterException("Current game could not be found!");
        System.out.println("clientModelRoot current game in request current game: " + clientModelRoot.getCurrentGame());
	}

    /**
     * Gets games from the ServerProxy and updates the games in the ClientModel. Re-throws
     * BadUserException if authentication doesn't work on the server side.
     */
    public void requestGames() throws BadUserException {
        clientModelRoot.setGames(serverProxy.getGames(getUser()));
    }

    /**
     * The current user leaves the game that matches gameID. Throws
     * BadUserException if user cannot be authenticated by server. Throws GameActionException if
     * user can't leave the game (not in the game, etc).
     * @param gameID int game ID
     */
    public boolean leaveGame(int gameID) throws GameActionException, BadUserException {
        boolean left = serverProxy.leaveGame(getUser(), gameID);
	    requestGames();
	    return left;
    }

    /**
     * Creates a game with current user as the conductor (creator). Throws a GameActionException
     * if user can't join the newly created game (already part of game, the game is full, etc).
     * Throws a BadUserException if getUser() cannot authenticate on the server side during game
     * creation.
     * @param gameName String game name
     * @param playerCount int number of players (this should be between 2 and 5)
     * @param color Player.PlayerColor color desired by the creating user
     * @return IGame object representing the newly created game
     */
    public IGame createGame(String gameName, int playerCount, Player.PlayerColor color) throws GameActionException, BadUserException {
        IGame createdGame = serverProxy.createGame(getUser(), gameName, playerCount);
        System.out.println("UIFarquad says to create game: " + gameName + " " + playerCount);
        System.out.println(createdGame);
        createdGame = joinGame(createdGame.getGameID(), color);
        return createdGame;
    }

    /**
     * Joins the game that matches the gameID and selects the specified color for the user. Throws
     * BadUserException if user cannot be authenticated by server. Throws GameActionException if
     * user can't join the game (already part of the game, the game is full, etc).
     * @param gameID int game ID
     * @param color PlayerColor color
     */
    public IGame joinGame(int gameID, Player.PlayerColor color) throws GameActionException, BadUserException {
        IGame ret = serverProxy.joinGame(getUser(), gameID, color);
	    requestGames();
	    return ret;
    }

	public void logout() {
		clientModelRoot.setCurrentUser(null);
	}

    // Observer things.

    /**
     * Register an observer for the ClientModel. Your class (a Presenter, probably) should
     * implement Observer and have the required update(...) method. Also, see this for useful
     * visual: http://stackoverflow.com/a/15810174.
     * @param obs Observer object
     */
    public void registerObserver(Observer obs) {
        clientModelRoot.addObserver(obs);
    }

    /**
     * Unregister an observer from the ClientModel.
     * @param obs Observer object
     */
    public void unregisterObserver(Observer obs) {
        clientModelRoot.deleteObserver(obs);
    }

}
