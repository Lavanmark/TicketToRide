package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawDestinationCmd extends DrawDestinationCmdData {
    public DrawDestinationCmd(Player player, List<DestinationCard> destCardList){
        this.drawingPlayer = player;
        this.cardsDrawn = destCardList;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public void execute(IClient client) {
        //TODO
    }
}
