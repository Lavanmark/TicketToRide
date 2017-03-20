package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sun.security.krb5.internal.crypto.Des;

/**
 * Created by pokemaughan on 3/15/17.
 */

public class DiscardDestinationCmdData {
    private Player discardingPlayer;
    private DestinationCard[] cards;

    public Boolean forPlayer(User user){
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
