package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/18/17.
 */

public abstract class ClaimRouteCmdData extends ICommand{
    protected Player claimingPlayer = null;
    protected Route routeToClaim = null;

    @Override
    public boolean forPlayer(User user){
        if(user.getUserID()!=claimingPlayer.getUserID())
            return false;
        return true;
    }
}
