package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class GameOverCmd extends GameOverCmdData {
    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        game.endGame(); //must be here to set finished to true
        game.setPlayerList(playerList);
        return true;
    }
}
