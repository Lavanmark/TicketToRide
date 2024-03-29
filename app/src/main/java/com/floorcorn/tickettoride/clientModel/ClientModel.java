package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class ClientModel extends Observable {

    private User currentUser;
    private Set<GameInfo> gameList;
    private Game currentGame;
    private GameChatLog chatLog;

	public ClientModel() {
		currentGame = null;
		currentUser = null;
		gameList = new HashSet<>();
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
        Set<GameInfo> gameSet = new HashSet<>();
        for(GameInfo gi : gameList) {
            if(gi.isPlayer(user.getUserID())) // found the player inside of the list
                gameSet.add(gi);
        }
        return gameSet;
    }

    public Game getCurrentGame() {
        return currentGame;
    }

    public void setGames(Set<GameInfo> gList) {
	    if(gList == null)
		    gameList = new HashSet<>();
	    else
            gameList = new HashSet<>(gList);

	    setChanged();
        notifyObservers(gameList);
    }

    public void setCurrentUser(User user) {
        currentUser = user;

        setChanged();
        notifyObservers(currentUser);
    }

    public void setCurrentGame(Game game) {
        //if(currentGame == null || currentGame.getGameID() != game.getGameID() || currentGame.getLatestCommandID() != game.getLatestCommandID())
        //    lastCommandExecuted = -1;
        currentGame = game;

        setChanged();
        notifyObservers(currentGame);
    }

    public int getLastCommandExecuted() {
        return currentGame.getLatestCommandID();
    }

    public GameChatLog getChatLog(){
        return this.chatLog;
    }

    public void setChatLog(GameChatLog chatLog){
        if(!chatLog.equals(this.chatLog)) {
            this.chatLog = chatLog;
            setChanged();
            notifyObservers(this.chatLog);
        }
    }

    /**
     * Returns the Player whose turn it is.
     * @return Player object whose turn it is
     */
    public Player whoseTurn() {
        int count = 0;
        Player retval = null;
        for (Player p : currentGame.getPlayerList()) {
            if (p.isTurn()) {
                retval = p;
                count++;
            }
        }

        assert count == 1 : "Number of players whose turn it is is not 1, it is: " + count;

        return retval;
    }

    public void notifyGameChanged() {
        setChanged();
        notifyObservers(this.currentGame);
    }
}
