package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawTrainCardCmd extends DrawTrainCardCmdData {
    private DrawTrainCardCmd(){}

    public DrawTrainCardCmd(Player player, boolean bool, int cardPosition, TrainCard trainCard){
        this.drawingPlayer = player;
        this.firstDraw = bool;
        this.cardDrawn = trainCard;
        this.cardPosition = cardPosition;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public void execute(IClient client) {
        if(cardPosition>-1 && cardPosition<5)
            client.drawTrainCard(cardPosition);
        else
            client.drawTrainCard();
    }
}
