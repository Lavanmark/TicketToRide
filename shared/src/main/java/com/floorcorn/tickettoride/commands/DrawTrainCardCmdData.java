package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/18/17.
 */

public abstract class DrawTrainCardCmdData extends ICommand{
    protected Player drawingPlayer = null;
    protected boolean firstDraw = false;
    protected TrainCard cardDrawn = null;

    @Override
    public boolean forPlayer(User user){
        if (user.getUserID()!=drawingPlayer.getUserID())
            return false;
        return true;
    }

}
