package com.floorcorn.tickettoride.ui.presenters;

import android.graphics.Color;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.ui.views.IView;
import com.floorcorn.tickettoride.ui.views.LobbyView;

import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class LobbyPresenter implements IPresenter{

    private LobbyView view;

    @Override
    public void setView(IView view) {

    }

    public void createGame(){
        String color = view.getPlayerColor();
        int numPlayers = view.getNewGamePlayerNumber();
        String gameName = view.getNewGameName();
        //color, num players, game name
        //asks the view, the view asks the activity
    }

    public void joinGame(){

    }

    public Set<Game> getGameList(){
        return null;
    }

    public Set<Game> getGameList(User user){
        return null;
    }

    public Set<Game> getGameList(Set<Game> gameList){
        //TODO not exactly sure what this does
        return null;
    }

    public Game getGameInfo(int gameID){
        return null;
    }

    private void getNewGameInfo(){
        
    }


}
