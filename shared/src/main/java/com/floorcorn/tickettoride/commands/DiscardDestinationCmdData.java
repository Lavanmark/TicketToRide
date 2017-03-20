package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pokemaughan on 3/15/17.
 */

public abstract class DiscardDestinationCmdData extends ICommand {
    protected Player discardingPlayer = null;
    protected DestinationCard[] cards = null;

    @Override
    public boolean forPlayer(User user){
        if(user.getUserID()!=discardingPlayer.getUserID())
            return false;
        return true;
    }

    public List<DestinationCard> getCards(){
        return Arrays.asList(cards);
    }

    public Player getPlayer(){
        return discardingPlayer;
    }

    public void setPlayer(Player player){
        discardingPlayer = player;
    }

    public void addCard(DestinationCard destinationCard){
        //TODO a check to see if theres more than 2 being discarded? is that necessary?
        cards[cards.length-1] = destinationCard;
    }
}
