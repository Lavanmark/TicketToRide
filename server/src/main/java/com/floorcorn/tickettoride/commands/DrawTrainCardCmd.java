package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawTrainCardCmd extends DrawTrainCardCmdData {
    public DrawTrainCardCmd(Player player, boolean bool, int cardPosition, TrainCard trainCard){
        this.drawingPlayer = player;
        this.firstDraw = bool;
        this.cardDrawn = trainCard;
        this.cardPosition = cardPosition;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return null;
    }

    @Override
    public void execute(IClient client) {
        if(cardPosition == -1) {
            cardDrawn = client.drawTrainCard();
            client.addCardToPlayer(drawingPlayer, cardDrawn);
        } else {
            cardDrawn = client.drawTrainCard(cardPosition);
            client.addCardToPlayer(drawingPlayer, cardDrawn);
        }
    }
}
