package com.floorcorn.tickettoride.ui.views;

import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.activities.CreateGameActivity;

import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class LobbyView implements IView{

    private CreateGameActivity gameActivity;
    private IPresenter presenter;
    private Button createGameButton;
    private Button refreshListButton;
    private Button joinGameButton;
    private RecyclerView gameList;

    public void getGameNumber(){
        //game number?
    }

    public String getPlayerColor(){ // do we want these as colors or strings?
        return gameActivity.getColor();
    }

    public void createNewGameDialogue(){
        //does this need to be in the activity?
    }

    public int getNewGamePlayerNumber(){
        return Integer.parseInt(gameActivity.getPlayerNumber());
    }

    public String getNewGameHostColor(){ // there need to be separate getters for the host and the rest?
        return gameActivity.getColor();
    }

    public String getNewGameName(){
        return gameActivity.getGameName();
    }

    public void displayGameList(Set<Game> games){
        //does this need to be in the activity?
    }

    @Override
    public void setPresenter(IPresenter p) {
        //presenter = () p;
        //presenter = () p;
    }
}
