package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public abstract class LastRoundCmdData extends ICommand{
    protected Player lastPlayer = null;

    @Override
    public boolean forPlayer(User user){
        if(user.getUserID()!=lastPlayer.getUserID())
            return false;
        return true;
    }
}
