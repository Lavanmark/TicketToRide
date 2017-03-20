package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public abstract class GameOverCmdData extends ICommand {
    protected List<Player> playerList = null;

    @Override
    public boolean forPlayer(User user){
        return true; // nobody is limited to this command sooooo yeah
    }
}
