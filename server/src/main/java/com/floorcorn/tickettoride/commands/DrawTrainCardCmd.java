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

    public DrawTrainCardCmd(Player player, boolean first, int cardPosition, TrainCard trainCard){
        this.drawingPlayer = player;
        this.firstDraw = first;
        this.cardDrawn = trainCard;
        this.cardPosition = cardPosition;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(drawingPlayer.getUserID() == user.getUserID())
            return this;
        return new DrawTrainCardCmd(drawingPlayer.getCensoredPlayer(user), firstDraw, cardPosition, null);
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
