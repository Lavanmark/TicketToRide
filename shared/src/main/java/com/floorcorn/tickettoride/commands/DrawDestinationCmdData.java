package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/18/17.
 */

public abstract class DrawDestinationCmdData extends ICommand{
    protected Player drawingPlayer = null;
    protected List<DestinationCard> cardsDrawn = null;

    @Override
    public boolean forPlayer(User user){
        if(user.getUserID()!=drawingPlayer.getUserID())
            return false;
        return true;
    }
}
