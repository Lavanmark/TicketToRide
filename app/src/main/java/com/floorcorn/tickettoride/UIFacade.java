package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.ui.views.IView;

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
    private Poller poller;

    // TODO: add all the types of sorting we may want
    public enum GameSortStyle { ASC_GAMEID, DESC_GAMEID };

    // Things relating to private constructor and singleton pattern.

    private UIFacade() {
        clientModelRoot = new ClientModel();
        serverProxy = new ServerProxy();
	    serverProxy.setPort("8080");
        serverProxy.setHost("10.24.69.49");
        poller = new Poller(serverProxy, clientModelRoot);
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
        User user = new User(username, password);
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
        User user = new User(username, password, firstname + " " + lastname);
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
    public User getUser() {
        return clientModelRoot.getCurrentUser();
    }

    /**
     * Returns the games from the ClientModel.
     * @return Set of Game objects from the ClientModel
     */
    public Set<GameInfo> getGames() {
        return clientModelRoot.getGames();
    }

    /**
     * Returns the games that the user has joined.
     * @param user User object
     * @return Set of Game objects from the ClientModel
     */
    public Set<GameInfo> getGames(User user) {
	    user = new User(user);
        return clientModelRoot.getGames(user);
    }

    /**
     * Sorts some games according to the sort style.
     * @param games Set of Game objects to sort
     * @param sortStyle GameSortStyle enum variable designates how to sort
     * @return Same Game objects, but sorted
     */
    private List<GameInfo> sortGames(Set<GameInfo> games, GameSortStyle sortStyle) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the games from ClientModel and sorts them before returning. See GameSortStyle enum.
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of Game objects from the ClientModel, sorted
     */
    public List<GameInfo> getGames(GameSortStyle sortStyle) {
        Set<GameInfo> gamesSet = getGames();
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Gets the games from the ClientModel that the user has joined. Sorts them before
     * returning. See GameSortStyle enum.
     * @param user User object
     * @param sortStyle GameSortStyle enum designates what order of sort to do
     * @return List of Game objects from the ClientModel, sorted
     */
    public List<GameInfo> getGames(User user, GameSortStyle sortStyle) {
	    user = new User(user);
        Set<GameInfo> gamesSet = getGames(user);
        return sortGames(gamesSet, sortStyle);
    }

    /**
     * Returns Game that matches the gameID parameter. Will return null if games returned from
     * ClientModel is null or if gameID not found in games.
     * @param gameID int ID
     * @return Game matching gameID from ClientModel games; can be null
     */
    public GameInfo getGameInfo(int gameID) {
        Set<GameInfo> games = getGames();
        if (games == null) return null;
        for (GameInfo gi : games) {
            if (gi.getGameID() == gameID) {
                return gi;
            }
        }
        return null;
    }

    /**
     * Returns the Game from ClientModel.getCurrentGame().
     * @return Game object
     */
    public Game getCurrentGame() {
        return clientModelRoot.getCurrentGame();
    }

	public void requestGame(GameInfo game) throws BadUserException{
		Game cgame = serverProxy.getGame(clientModelRoot.getCurrentUser(), game.getGameID());
        clientModelRoot.setCurrentGame(cgame);
	}

    /**
     * Gets games from the ServerProxy and updates the games in the ClientModel. Re-throws
     * BadUserException if authentication doesn't work on the server side.
     */
    public void requestGames() throws BadUserException {
        clientModelRoot.setGames(serverProxy.getGames(getUser()));
	    for(GameInfo gi : clientModelRoot.getGames())
		    System.out.println(gi.getGameID() + " " + gi.getName());
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
     * @return Game object representing the newly created game
     */
    public GameInfo createGame(String gameName, int playerCount, PlayerColor color) throws GameActionException, BadUserException {
        GameInfo createdGame = serverProxy.createGame(getUser(), gameName, playerCount);

        if(createdGame == null)
            return null;
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
    public GameInfo joinGame(int gameID, PlayerColor color) throws GameActionException, BadUserException {
        GameInfo ret = serverProxy.joinGame(getUser(), gameID, color);
	    requestGames();
	    return ret;
    }

    public void pollPlayerList(IView view) {
		resetPollerState();
	    poller.startPollingPlayerList(view);
    }

    public void pollCommands(IView view) {
        resetPollerState();
        poller.startPollingCommands(view);
    }

    public void stopPolling() {
        resetPollerState();
    }

	private void resetPollerState() {
		if(poller == null) {
			poller = new Poller(serverProxy, clientModelRoot);
			return;
		}
		poller.stopPolling();
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

    public void clearObservers() {
        clientModelRoot.deleteObservers();
    }

}
