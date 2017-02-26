package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

import java.util.List;

/**
 * Created by Michael on 2/24/2017.
 */

public class ClientProxy implements IClient {

    private Game gameToModify;
    @Override
    public void setPlayerList(List<Player> players) {

    }

    @Override
    public void setFaceUpDeck(List<TrainCard> faceUpDeck) {

    }

    public Game getGame(){return this.gameToModify;}

    public void setGame(Game g){this.gameToModify = g;}
}
