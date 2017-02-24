package com.floorcorn.tickettoride.interfaces;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

import java.util.List;

/**
 * Created by Michael on 2/24/2017.
 */

public interface IClient {

    /**
     *  Assigns a new list of players to be stored in the model
     * @param players a list of players to be stored.
     */
    public void setPlayerList(List<Player> players);

    /**
     * Assigns a new deck of TrainCards to be stored as the face up deck
     * @param faceUpDeck the new list of TrainCards to be stored as the face up deck
     */
    public void setFaceUpDeck(List<TrainCard> faceUpDeck);
}
