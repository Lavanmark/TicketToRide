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
        return this;
    }

    @Override
    public boolean execute(Game game) {
        //if(claimingPlayer.isCensoredPlayer()) {
            System.out.println("THEY WERE CENSORED " + claimingPlayer.getName());
            game.updatePlayer(claimingPlayer);
            return game.getBoard().updateRoute(routeToClaim);
//        } else {
//            if(game.claimRoute(routeToClaim, claimingPlayer)) {
//                game.calculateLongestRoute(claimingPlayer);
//                return true;
//            }
//        }
//        return false;
    }
}
