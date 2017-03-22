package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public abstract class LastRoundCmdData extends ICommand{
    protected int lastPlayerID = Player.NO_PLAYER_ID;

    @Override
    public boolean forPlayer(User user){
        return true;
    }
}
