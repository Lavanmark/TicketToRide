package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.Arrays;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DiscardDestinationCmd extends DiscardDestinationCmdData {
    private DiscardDestinationCmd(){}

    public DiscardDestinationCmd(Player player, DestinationCard[] cards){
        this.discardingPlayer = player;
        this.cards = cards;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        if(discardingPlayer.isCensoredPlayer())
            game.updatePlayer(discardingPlayer);
        else
            return game.discardDestinationCards(discardingPlayer, Arrays.asList(this.cards));
        return true;
    }
}
