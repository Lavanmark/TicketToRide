package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.Arrays;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DiscardDestinationCmd extends DiscardDestinationCmdData{
    
    private DiscardDestinationCmd() {}
    
    public DiscardDestinationCmd(Player player, DestinationCard[] cards){
        this.discardingPlayer = player;
        this.cards = cards;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(user.getUserID() == discardingPlayer.getUserID())
            return this;
        ICommand cmd = new DiscardDestinationCmd(discardingPlayer.getCensoredPlayer(user), null);
        cmd.setCmdID(this.commandID);
        cmd.setGameID(this.gameID);
        return cmd;
    }

    @Override
    public boolean execute(Game game) {
        if(!game.discardDestinationCards(discardingPlayer, Arrays.asList(cards)))
            return false;
        discardingPlayer = new Player(game.getPlayer(discardingPlayer.getUserID()));
        return true;
    }
}
