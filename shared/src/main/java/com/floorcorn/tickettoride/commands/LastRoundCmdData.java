package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class LastRoundCmdData {
    private Player lastPlayer;

    public Boolean forPlayer(User user){
        if(user.getUserID()!=lastPlayer.getUserID())
            return false;
        return true;
    }
}
