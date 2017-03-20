package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class GameOverCmdData {
    private List<Player> playerList;

    public Boolean forPlayer(User user){
        return true; // nobody is limited to this command sooooo yeah
    }
}
