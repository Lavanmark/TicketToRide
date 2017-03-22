package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class LastRoundCmd extends LastRoundCmdData{
    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public void execute(Game game) {
        game.setLastPlayerID(this.lastPlayerID);
    }
}
