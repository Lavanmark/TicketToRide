package com.floorcorn.tickettoride.model;

import com.floorcorn.tickettoride.log.Corn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Kaylee on 2/23/2017.
 *
 * @author Kaylee
 * @author Lily
 *
 * This class manages the behavior of the train deck and the destination card deck.
 * It has a list of train cards, a list of discarded train cards, and a list of destination cards.
 *
 * This class is modified in the server when a player takes a turn, and used by the client model to
 * display the cards correctly.
 *
 */

public class DeckManager {
	/** List of train cards available to draw from **/
    private List<TrainCard> trainCardDeck;
	/** List of train cards that have been discarded **/
    private List<TrainCard> trainCardDiscard;
	/** List of destination cards **/
    private List<DestinationCard> destinationCardDeck;

	/**
	 * default constructor for deserialization
	 */
	private DeckManager(){}

	/**
	 * This method initializes all the card decks.
	 *
	 * The trainCardDeck is filled with 12 each of Box, Passenger, Tanker, Reefer, Freight,
	 * Hopper, Coal, and Caboose cars, plus 14 Locomotives
	 *
	 * The destination deck is filled from the .csv file in the MapFactory
	 *
	 * @param shuffle boolean indicates whether the decks should shuffled on initializiation
	 *
	 * @pre shuffle != null
	 *
	 * @post trainCardDeck != null
	 * @post trainCardDeck.length() = 110
	 * @post destinationCardDeck != null
	 * @post destinationCardDeck.length() = number of entries in
	 * 			shared/model/maps/MarsDestinationCards.csv
	 * @post trainCardDiscard != null
	 * @post trainCardDiscard.length(null)
	 * @post if shuffle = true, all decks are shuffled
	 * @post if shuffle = true, all decks are ordered
     */
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

	/**
	 * Builds a new deckManager from dm
	 * This constructor may be called during the game state,
	 * so some of the cards will be in the player's hand, not the decks.
	 *
	 * @param dm old Deck Manager
	 *
	 *  @pre dm != null
	 *  @pre dm.trainCardDeck != null
	 *  @pre dm.trainCardDiscard != null
	 *  @pre dm.destinationDeck != null
	 *  @pre dm.trainCardDeck.length() + dm.trainCardDiscard().length() <= 110
	 *  		(keep in mind the players may have some of the cards)
	 *  @pre dm.destinationDeck.length() <= number of entries in
	 * 			shared/model/maps/MarsDestinationCards.csv
	 *
	 *  @post trainCardDeck == dm.trainCardDeck
	 *  @post trainCardDiscard == dm.trainCardDiscard
	 *  @post destinationCardDeck == dm.destinationCardDeck
     */
	public DeckManager(DeckManager dm) {
		this.trainCardDeck = new ArrayList<>(dm.trainCardDeck);
		this.trainCardDiscard = new ArrayList<>(dm.trainCardDiscard);
		this.destinationCardDeck = new ArrayList<>(dm.destinationCardDeck);
	}

	/**
	 * This method calls shuffle on each deck of cards
	 *
	 * 	@pre dm.trainCardDeck != null
	 *  @pre dm.trainCardDiscard != null
	 *  @pre dm.destinationDeck != null
	 *
	 *  @post dm.trainCardDeck is shuffled
	 *  @post dm.trainCardDiscard is shuffled
	 *  @post dm.destinationDeck is shuffled
	 *
	 *  @post no deck changed in size
	 *  @post no deck changed the number of each type of card it contains (i.e. Locomotives)
	 */
    public void shuffleAllCards(){
	    shuffleList(trainCardDeck);
	    shuffleList(trainCardDiscard);
	    shuffleList(destinationCardDeck);
    }

	/**
	 * This method lets a player draw a card from the trainCardDeck
	 *
	 * @return trainCard that was on top of the trainCardDeck.
	 * @return null if the preCondition trainCardDeck != null was violated
	 * @return null if the trainCardDeck was empty, and the reshuffle did not change the size of the trainCardDeck
	 *
	 * @pre trainCardDeck != null
	 * @pre trainCardDiscard != null
	 * @pre 0 <= trainCardDeck.length() < 110
	 *
	 * @post if the trainCardDeck was not empty, trainCardDeck.length() = OLD trainCardDeck.length() -1
	 * @post if the trainCardDeck was not empty, for every card in the train card deck, new position in deck = old position in deck - 1
	 * @post if the trainCardDeck was empty, the trainCardDeck was replaced with the shuffled discard deck, and returns the top card of the new deck
	 * @post if the trainCardDeck was empty, then 0 <= trainCardDeck.length() < 110
	 *
	 *
     */
    public TrainCard drawFromTrainCardDeck(){
		// if the deck is empty, reshuffle the discard deck
        if(trainCardDeck.isEmpty())
	        reshuffleTrainCardDiscard(); // now the deck should not be empty

		// if the deck is not empty, return the card from the top of the deck
	    if(!trainCardDeck.isEmpty())
	        return trainCardDeck.remove(0);
		return null;
    }

	/**
	 * This method allows the user to discard a train Card
	 *
	 * The discarded card is added to the end of the trainCardDiscardDeck.
	 * The position is unimportant in the discarded deck.
	 *
	 * @param card TrainCard to be discarded
	 *
	 * @pre card != null
	 * @pre trainCardDiscard != null
	 * @post trainCardDiscard.length = OLD trainCardDiscard.length() + 1
	 *
     */
    public void discard(TrainCard card){
        trainCardDiscard.add(card); //don't care where we add it to
    }

	/**
	 * This method allows the user to discard a destination card
	 * The discarded card is added to the end of the destinationDeck.
	 *
	 * @param card DestinationCard to be discarded
	 *
	 * @pre card != null
	 * @pre destinationCardDeck != null
	 * @post destinationCardDeck.length() = OLD destinationCardDeck.length() + 1
     */
    public void discard(DestinationCard card){
        destinationCardDeck.add(card); // add to bottom of deck
    }

	/**
	 * This method allows a user to draw a single card from the Destination Card Deck
	 *
	 * @return if destinationDeck is not empty, returns destination card at the top of the deck
	 * @return if destinationDeck is empty, returns null
	 *
	 * @pre destinationCardDeck != null
	 * @post if destinationDeck is empty, the deck stays empty.
	 * @post if destinationDeck is not empty, destinationDeck.len() = OLD destinationDeck.len() -1
	 * @post the order of the destinationDeck did not change
	 *
     */
    public DestinationCard drawFromDestinationCardDeck(){
	    if(destinationCardDeck.size() > 0)
            return destinationCardDeck.remove(0);
	    return null;
    }

	/**
	 * This method shuffles the train cards in the discard pile and moves them
	 * to the trainCardDeck
	 *
	 * @pre trainCardDiscard != null
	 * @pre trainCardDeck != null
	 * @pre trainCardDeck is empty
	 * @pre trainDiscardDeck is not empty
	 *
	 * @post trainCardDiscard is empty
	 * @post 0 < trainCardDeck.length() <= 110
	 * @post trainCardDeck is in random order
	 * @post trainCardDeck.length = OLD trainCardDiscard.length
	 */
    private void reshuffleTrainCardDiscard(){
	    shuffleList(trainCardDiscard);
	    trainCardDeck.addAll(trainCardDiscard);
	    trainCardDiscard.clear();
    }

	/**
	 * This method shuffles a list of cards using the Fisher-Yates shuffle
	 *
	 * @param deck is a list.
	 *
	 * @post the list stays the same size
	 * @post the original deck is not the same as the deck at the end
     */
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

	/**
	 * This method swaps two cards in the list of cards
	 *
	 * @param i position to be swapped
	 * @param j position to be swapped
	 * @param list list of cards
	 *
	 * @pre list != null
	 * @pre i != j
	 * @pre list is not empty
	 * @pre i is a valid index within list
	 * @pre j is a valid index within list
	 *
	 * @post the object that started at position i will now be in position j
	 * @post the object that started at position j will now be in position i
	 *
     */
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
