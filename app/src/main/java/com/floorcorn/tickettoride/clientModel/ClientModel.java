package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.clientModel.Game;

import java.util.List;
import java.util.Observable;
import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class ClientModel extends Observable {

    private User currentUser;
    private Set<IGame> gameList;
    private IGame currentGame;

    public User getCurrentUser(){
        return currentUser;
    }

    public Set<IGame> getGames(){
        return gameList;
    }

    public Set<IGame> getGames(User user){
        if(user==null) return null;
        Set<IGame> gameSet = null;
        for(IGame g: gameList){
            if(g.getPlayer(user)!=null){ // found the player inside of the list
                gameSet.add(g);
            }
        }
        return gameSet;
    }

    public List<IGame> getGames(UIFacade.GameSortStyle sortStyle) {
        // Probably don't need this because UIFacade can return sorted games lists after getting
        // the games from this ClientModel
        throw new UnsupportedOperationException();
    }

    public IGame getCurrentGame(){
        return currentGame;
    }

    public void setGames(Set<IGame> gList){
        gameList = gList;

        setChanged();
        notifyObservers();
    }

    public void setCurrentUser(User user){
        currentUser = user;

        setChanged();
        notifyObservers();
    }

    public void setCurrentGame(IGame game){
        currentGame = game;

        setChanged();
        notifyObservers();
    }
}
