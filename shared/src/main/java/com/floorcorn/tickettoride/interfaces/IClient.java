package com.floorcorn.tickettoride.interfaces;


import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.TrainCard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 2/24/2017.
 * @author Michael
 */

public interface IClient {
    /**
     *  Assigns a new list of players to be stored in the model
     * @param players a list of players to be stored.
     */
    void setPlayerList(List<Player> players);

    /**
     * Assigns a new deck of TrainCards to be stored as the face up deck
     * @param faceUpDeck the new list of TrainCards to be stored as the face up deck
     */
    void setFaceUpDeck(TrainCard[] faceUpDeck);

    void startTurn(Player player);

    void setLastPlayer(Player player);

    void setGameOver();

	TrainCard drawTrainCard();

	TrainCard drawTrainCard(int position);

	List<DestinationCard> drawDestinationCards();

    void addCardToPlayer(Player player, TrainCard card);

	void addDestinationCardsToPlayer(Player player, List<DestinationCard> cards);

    Game getGame();
}
