package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawDestinationCmd extends DrawDestinationCmdData {

    private DrawDestinationCmd() {}
    public DrawDestinationCmd(Player player){
        this.drawingPlayer = player;
        this.cardsDrawn = null; //This is because we won't know what cards were drawn initially
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        if(drawingPlayer.isCensoredPlayer())
            game.updatePlayer(drawingPlayer);
        else
            return game.addDestinationCardsToPlayer(drawingPlayer, cardsDrawn);
        return true;
    }
}
