package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.clientModel.Game;
import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class ClientModel {

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

    public Set<IGame> getGames(Set<IGame> gList){
        //not entirely sure what this one does...

        return null;
    }

    public IGame getCurrentGame(){
        return currentGame;
    }

    public void setGames(Set<IGame> gList){
        gameList = gList;
    }

    public void setCurrentUser(User user){
        currentUser = user;
    }

    public void setCurrentGame(IGame game){
        currentGame = game;
    }
}
