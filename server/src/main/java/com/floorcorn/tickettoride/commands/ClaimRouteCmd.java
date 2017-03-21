package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class ClaimRouteCmd extends ClaimRouteCmdData {
    public ClaimRouteCmd(Player player, Route route){
        claimingPlayer = player;
        routeToClaim = route;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(user.getUserID() == claimingPlayer.getUserID())
            return this;
        return new ClaimRouteCmd(claimingPlayer.getCensoredPlayer(user), routeToClaim);
    }

    @Override
    public void execute(Game game) {
        game.claimRoute(routeToClaim, claimingPlayer);
    }
}
