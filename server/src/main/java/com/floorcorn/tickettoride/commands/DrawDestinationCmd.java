package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawDestinationCmd extends DrawDestinationCmdData{
    private DrawDestinationCmd(){}

    public DrawDestinationCmd(Player player, List<DestinationCard> destCardList){
        this.drawingPlayer = player;
        this.cardsDrawn = destCardList;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(user.getUserID() == drawingPlayer.getUserID())
            return this;
        return new DrawDestinationCmd(drawingPlayer.getCensoredPlayer(user), null);
    }

    @Override
    public void execute(IClient client) {
        cardsDrawn = client.drawDestinationCards();
        client.addDestinationCardsToPlayer(drawingPlayer, cardsDrawn);
    }
}
