package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;

import java.util.ArrayList;

/**
 * Created by Kaylee on 2/4/2017.
 */

public class Game extends IGame {

    public Game(IGame game) {
        this.gameID = game.getGameID();
        this.gameSize = game.getGameSize();
        this.name = game.getName();
        this.playerList = new ArrayList<Player>(game.getPlayerList());
        this.finished = game.isFinsihed();
    }

    public Game(String name, int size) {
        this.name = name;
        if(size < 2) size = 2;
        if(size > 5) size = 5;
        this.gameSize = size;
        this.playerList = new ArrayList<Player>();
    }

    public Game(int gameID) {
        this.gameID = gameID;
    }
}
