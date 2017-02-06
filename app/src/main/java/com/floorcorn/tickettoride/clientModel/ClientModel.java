package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.clientModel.Game;
import java.util.Set;
import java.util.SortedSet;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class ClientModel {

    private User currentUser;
    private Set<Game> gameList;
    private Game currentGame;

    public User getCurrentUser(){
        return currentUser;
    }

    public Set<Game> getGames(){
        return gameList;
    }

    public Set<Game> getGames(User user){
        if(user==null) return null;
        Set<Game> gameSet = null;
        for(Game g: gameList){
            if(g.getPlayer(user)!=null){ // found the player inside of the list
                gameSet.add(g);
            }
        }
        return gameSet;
    }

    public Set<Game> getGames(Set<Game> gList){
        //not entirely sure what this one does...

        return null;
    }

    public Game getCurrentGame(){
        return currentGame;
    }

    public void setGames(Set<Game> gList){
        gameList = gList;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public void setCurrentGame(Game game){
        currentGame = game;
    }
}
