package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.ClientModel;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.model.IGame;

import java.util.Observer;
import java.util.Set;

/**
 * Created by Tyler on 2/4/2017.
 *
 * @author Joseph Hansen
 */

public class UIFacade {

    // Instance variables and other class-specific things.

    private ClientModel clientModelRoot;

    public enum GameSortStyle { DESC, ASC };

    // Things relating to private constructor and singleton pattern.

    private UIFacade() {
        clientModelRoot = new ClientModel();
    }
    private static UIFacade instance = null;
    public static UIFacade getInstance() {
        if (instance == null)
            instance = new UIFacade();
        return instance;
    }

    // Login and register related.

    public void login(String username, String password) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public void register(String username, String password, String firstname, String lastname) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    // User and game related.

    public User getUser() {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public Set<IGame> getGames() {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public Set<IGame> getGames(User user) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public Set<IGame> getGames(String howToSort) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public Set<IGame> getGames(User user, String howToSort) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public IGame getGame(int gameID) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public IGame getCurrentGame() {
        // Not implemented yet.
        throw new UnsupportedOperationException();
    }

    public void setCurrentGame(IGame game) {
        // Not implemented yet.
        throw new UnsupportedOperationException();
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
