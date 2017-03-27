package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class StartTurnCmd extends StartTurnCmdData {
    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        game.setTurn(this.player);
        return true;
    }
}
