package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class ClaimRouteCmd extends ClaimRouteCmdData {
    
    private ClaimRouteCmd() {}
    
    public ClaimRouteCmd(Player player, Route route){
        claimingPlayer = player;
        routeToClaim = route;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(user.getUserID() == claimingPlayer.getUserID())
            return this;
        ICommand cmd = new ClaimRouteCmd(claimingPlayer.getCensoredPlayer(user), routeToClaim);
        cmd.setCmdID(this.commandID);
        cmd.setGameID(this.gameID);
        return cmd;
    }

    @Override
    public boolean execute(Game game) {
        claimingPlayer = game.getPlayer(claimingPlayer);
        if(game.claimRoute(routeToClaim, claimingPlayer)) {
            routeToClaim = game.getBoard().getRoute(routeToClaim.getRouteID());
            return true;
        }
        return false;
    }
}
