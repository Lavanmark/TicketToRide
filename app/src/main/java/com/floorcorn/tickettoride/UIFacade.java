package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.IGame;

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
     * @param username String username
     * @param password String password
     */
    public void login(String username, String password) {
        User user = new User(username, password);
        serverProxy.login(user);
    }

    /**
     * Prepares a User object for parameter to register(...) in ServerProxy. Calls register(...).
     * @param username String username
     * @param password String password
     * @param firstname String firstname
     * @param lastname String lastname
     */
    public void register(String username, String password, String firstname, String lastname) {
        User user = new User(username, password, firstname + " " + lastname);
        serverProxy.register(user);
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
        User user2 = new User(user);
        return clientModelRoot.getGames(user2);
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
        User user2 = new User(user);
        Set<IGame> gamesSet = getGames(user2);
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
        IGame retval = null;
        for (IGame game : games) {
            if (game.getGameID() == gameID) {
                retval = game;
                break;
            }
        }
        // Can be null if gameID is not in games.
        return retval;
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

    public void requestGames() {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public void cancelGame(int gameID) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public IGame createGame(String gameName, int playerCount) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public void joinGame(int userID, int gameID, Player.PlayerColor color) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    // Observer things.

    public void registerObserver(Observer obs) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public void unregisterObserver(Observer obs) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

}
