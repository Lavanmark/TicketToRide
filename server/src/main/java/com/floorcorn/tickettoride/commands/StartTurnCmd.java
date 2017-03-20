package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class StartTurnCmd extends StartTurnCmdData {
    public StartTurnCmd(Player player){
        this.player = player;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return null;
    }

    @Override
    public void execute(IClient client) {

    }
}
