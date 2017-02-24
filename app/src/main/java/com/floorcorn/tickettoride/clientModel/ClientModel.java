package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class ClientModel extends Observable {

    private User currentUser;
    private Set<GameInfo> gameList;
    private Game currentGame;
    private int lastCommandExecuted = -1;

	public ClientModel() {
		currentGame = null;
		currentUser = null;
		gameList = new HashSet<GameInfo>();
	}

    public User getCurrentUser() {
        return currentUser;
    }

    public Set<GameInfo> getGames() {
        return gameList;
    }

    public Set<GameInfo> getGames(User user) {
        if(user == null)
	        return null;
        Set<GameInfo> gameSet = new HashSet<GameInfo>();
        for(GameInfo gi : gameList) {
            if(gi.isPlayer(user.getUserID())) // found the player inside of the list
                gameSet.add(gi);
        }
        return gameSet;
    }

    public List<Game> getGames(UIFacade.GameSortStyle sortStyle) {
        // Probably don't need this because UIFacade can return sorted games lists after getting
        // the games from this ClientModel
        throw new UnsupportedOperationException();
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setGames(Set<GameInfo> gList) {
        gameList = gList;

        setChanged();
        notifyObservers(gameList);
    }

    public void setCurrentUser(User user) {
        currentUser = user;

        setChanged();
        notifyObservers(currentUser);
    }

    public void setCurrentGame(Game game) {
        if(currentGame == null || currentGame.getGameID() != game.getGameID())
            lastCommandExecuted = -1;
        currentGame = game;

        setChanged();
        notifyObservers(currentGame);
    }

    public void setLastCommandExecuted(int commandID) {
        lastCommandExecuted = commandID;
    }

    public int getLastCommandExecuted() {
        return lastCommandExecuted;
    }
}
