package com.floorcorn.tickettoride.ui.views;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;

import java.util.Set;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class LobbyView {

    private IPresenter presenter;
    private Button createGameButton;
    private Button refreshListButton;
    private Button joinGameButton;
    private RecyclerView gameList;

    public void getGameNumber(){

    }

    public Color getPlayerColor(){
        return null;
    }

    public void createNewGameDialogue(){

    }

    public int getNewGamePlayerNumber(){
        return 0;
    }

    public Color getNewGameHostColor(){
        return null;
    }

    public String getNewGameName(){
        return null;
    }

    public void displayGameList(Set<Game> games){

    }

}
