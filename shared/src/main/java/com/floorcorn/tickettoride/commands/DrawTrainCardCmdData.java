package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/18/17.
 */

public class DrawTrainCardCmdData {
    private Player drawingPlayer;
    private Boolean firstDraw;
    private TrainCard cardDrawn;

    public Boolean forPlayer(User user){
        if (user.getUserID()!=drawingPlayer.getUserID())
            return false;
        return true;
    }

}
