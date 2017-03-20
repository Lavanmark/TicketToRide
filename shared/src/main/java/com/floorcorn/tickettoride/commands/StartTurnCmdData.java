package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public abstract class StartTurnCmdData extends ICommand{
    protected Player player = null;

    @Override
    public boolean forPlayer(User user){
        if(user.getUserID()!=player.getUserID())
            return false;
        return true;
    }
}
