package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.log.Corn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class DeckManager {
    private List<TrainCard> trainCardDeck;
    private List<TrainCard> trainCardDiscard;
    private List<DestinationCard> destinationCardDeck;

	private DeckManager(){}
    public DeckManager(boolean shuffle) {
	    trainCardDeck = new ArrayList<>();
	    trainCardDiscard = new ArrayList<>();
	    destinationCardDeck = new ArrayList<>();

        for(TrainCardColor color : TrainCardColor.values()) {
            for(int i = 0; i < (color == TrainCardColor.WILD ? 14 : 12); i++) {
				trainCardDeck.add(new TrainCard(color));
            }
        }

	    destinationCardDeck = new MapFactory().getMarsDestinationCards();

	    if(shuffle)
	        shuffleAllCards();
    }

	public DeckManager(DeckManager dm) {
		this.trainCardDeck = new ArrayList<>(dm.trainCardDeck);
		this.trainCardDiscard = new ArrayList<>(dm.trainCardDiscard);
		this.destinationCardDeck = new ArrayList<>(dm.destinationCardDeck);
	}

    public void shuffleAllCards(){
	    shuffleList(trainCardDeck);
	    shuffleList(trainCardDiscard);
	    shuffleList(destinationCardDeck);
    }

    public TrainCard drawFromTrainCardDeck(){
        if(trainCardDeck.isEmpty())
	        reshuffleTrainCardDiscard();
	    if(!trainCardDeck.isEmpty())
	        return trainCardDeck.remove(0);
	    return null;
    }

    public void discard(TrainCard card){
        trainCardDiscard.add(card); //don't care where we add it to
    }

    public void discard(DestinationCard card){
        destinationCardDeck.add(card); // add to bottom of deck
    }

    public DestinationCard drawFromDestinationCardDeck(){
	    if(destinationCardDeck.size() > 0)
            return destinationCardDeck.remove(0);
	    return null;
    }

    private void reshuffleTrainCardDiscard(){
	    shuffleList(trainCardDiscard);
	    trainCardDeck.addAll(trainCardDiscard);
	    trainCardDiscard.clear();
    }

    private void shuffleList(List deck){
	    //Fisher-Yates shuffle
	    Random rn = new Random();
	    for(int times = 0; times < 3; times++) { //TODO probably too much...
		    for(int i = 0; i < deck.size() - 2; i++) {
			    int j = i + (rn.nextInt() % (deck.size() - i + 1));
			    swap(i, j, deck);
		    }
	    }
    }

	private void swap(int i, int j, List list) {
		if(list == null)
			return;
		if(i == j)
			return;
		if(list.isEmpty())
			return;
		if(list.size() <= i || list.size() <= j)
			return;
		if(i < 0 || j < 0)
			return;

		Object o = list.get(i);
		list.set(i, list.get(j));
		list.set(j, o);
	}
}
