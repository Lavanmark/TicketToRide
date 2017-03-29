package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawDestinationCmd extends DrawDestinationCmdData{
    private DrawDestinationCmd(){}

    public DrawDestinationCmd(Player player, List<DestinationCard> destCardList){
        this.drawingPlayer = player;
        this.cardsDrawn = destCardList;
    }

    @Override
    public ICommand getCmdFor(User user) {
        if(user.getUserID() == drawingPlayer.getUserID())
            return this;
	    ICommand cmd = new DrawDestinationCmd(drawingPlayer.getCensoredPlayer(user), null);
	    cmd.setCmdID(this.commandID);
	    cmd.setGameID(this.gameID);
	    return cmd;
    }

    @Override
    public boolean execute(Game game) {
        if(cardsDrawn != null && cardsDrawn.size() > 0)
	        return false;
	    cardsDrawn = new ArrayList<>();
	    try {
		    for(int i = 0; i < 3; i++)
			    cardsDrawn.add(game.getBoard().drawFromDestinationCardDeck());
	    } catch(GameActionException e) {
		    e.printStackTrace();
		    return false;
	    }
	    drawingPlayer = game.getPlayer(drawingPlayer.getUserID());
	    game.addDestinationCardsToPlayer(drawingPlayer, cardsDrawn);
	    return true;
    }
}
