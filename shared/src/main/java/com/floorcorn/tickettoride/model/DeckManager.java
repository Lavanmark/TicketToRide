package com.floorcorn.tickettoride.model;

import java.util.List;
import java.util.Random;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class DeckManager {
    private List<TrainCard> trainCardDeck;
    private List<TrainCard> trainCardDiscard;
    private List<DestinationCard> destinationCardDeck;

    public void shuffleAllCards(){
        shuffleTrain(trainCardDeck);
        shuffleTrain(trainCardDiscard);
        shuffleDest(destinationCardDeck);
    }

    public TrainCard drawFromTrainCardDeck(){
        return null;
    }

    public void discard(TrainCard card){
        trainCardDiscard.add(card);
        //not sure if this adds to the bottom or top of list
    }

    public void discard(DestinationCard card){
        destinationCardDeck.add(new Random().nextInt(),card);
        // adds the card randomly into the deck. do we want a discard pile for this? or is this ok?
    }

    public DestinationCard drawFromDestinationCardDeck(){
        return destinationCardDeck.get(0); //what side are we designating the top and bottom?
    }

    private void reshuffleTrainCardDiscard(){
        shuffleTrain(trainCardDiscard);
    }

    private void shuffleTrain(List<TrainCard> deck){
        //algorithm for shuffling our objects
    }

    private void shuffleDest(List<DestinationCard> deck){

    }

}
