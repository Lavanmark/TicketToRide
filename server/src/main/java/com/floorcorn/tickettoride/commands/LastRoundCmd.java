package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class LastRoundCmd extends LastRoundCmdData {
    public LastRoundCmd(Player player){
        this.lastPlayer = player;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return new LastRoundCmd(this.lastPlayer.getCensoredPlayer(user));
    }

    @Override
    public void execute(IClient client) {
        client.setLastPlayer(this.lastPlayer);
    }
}
