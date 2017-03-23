package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
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
        ICommand cmd = new StartTurnCmd(this.player.getCensoredPlayer(user));
        cmd.setCmdID(this.commandID);
        cmd.setGameID(this.gameID);
        return cmd;
    }

    @Override
    public void execute(Game game) {
        //TODO might need to check stuff here...
        game.setTurn(this.player);
    }
}
