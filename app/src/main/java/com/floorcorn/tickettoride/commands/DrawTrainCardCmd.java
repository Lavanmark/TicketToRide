package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DrawTrainCardCmd extends DrawTrainCardCmdData {
    private DrawTrainCardCmd(){}

    public DrawTrainCardCmd(Player player, boolean bool, int cardPosition){
        this.drawingPlayer = player;
        this.firstDraw = bool;
        this.cardDrawn = null;
        this.cardPosition = cardPosition;
    }

    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public boolean execute(Game game) {
        if(drawingPlayer.isCensoredPlayer())
            game.updatePlayer(drawingPlayer);
        else
            return game.addCard(drawingPlayer, cardDrawn);
        return true;
    }
}
