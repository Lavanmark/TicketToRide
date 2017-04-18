package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class GameOverCmd extends GameOverCmdData {
    private GameOverCmd(){}
    
    public GameOverCmd(List<Player> playerList){
        this.playerList = new ArrayList<>(playerList);
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        game.endGame();
        this.playerList = game.getPlayerList();
        return true;
    }
}
