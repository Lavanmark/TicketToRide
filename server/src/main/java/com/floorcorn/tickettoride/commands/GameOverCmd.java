package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class GameOverCmd extends GameOverCmdData {
    public GameOverCmd(List<Player> playerList){
        this.playerList = playerList;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public void execute(IClient client) {
        client.setGameOver();
        client.setPlayerList(this.playerList);
    }
}
