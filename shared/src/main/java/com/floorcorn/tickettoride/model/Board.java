package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floorcorn.tickettoride.exceptions.GameActionException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 2/24/2017.
 */

/**
 *  This class creates the Board object for Ticket to Ride.
 *  This class stores information that is related to the Board,such as route information,
 *  traincards, the different decks of cards, etc.
 */

public class Board {
	/**
	 * the max size of the faceup cards
	 */
	public static final int FACEUP_DECK_SIZE = 5;

	/**
	 *  List of all the routes in the game
	 */
    private List<Route> routeList;

	/**
	 * An Array of the cards that lay face up on the side of the board
	 */
    private TrainCard[] faceUpCards;

	/**
	 * manages the various deck types that are on the board
	 */
    private DeckManager deckManager;

	/**
	 * stores the length of the longest route currently on the board
	 */
	private int longestRoute;

	/**
	 * boolean enables double routes 3-5 players, disables double routes 2 players
	 */
	private boolean allowDoubles = false;

	/**
	 * Constructor - Create a new board with the specified routes in route list
	 * @param routeList list of routes on the boardmap.
	 * @param allowDoubles boolean en/disable double routes
	 *
	 * @pre the routeList is not null
	 * @pre the routeList contains routes and is not empty of routes
	 *
	 * @post a new Board is created with the given information
	 */
    public Board(List<Route> routeList, boolean allowDoubles) {
        this.routeList = routeList;
        this.faceUpCards = new TrainCard[FACEUP_DECK_SIZE];
        this.longestRoute = 0;
	    this.deckManager = null;
	    this.allowDoubles = allowDoubles;
        System.out.println("Board built from routeList");
    }

	/**
	 * Constructor - Create a new board with a pre-created board object
	 * @param board object that has been created in another class
	 *
	 * @pre board is not null
	 * @pre board's data members are not null and set with correct information
	 *
	 * @post a new Board object is created with the given board object
	 */
	public Board(Board board) {
        this.routeList = new ArrayList<>(board.getRoutes());
	    this.faceUpCards = new TrainCard[FACEUP_DECK_SIZE];
	    try {
		    setFaceUpCards(board.getFaceUpCards());
	    } catch(GameActionException e) {
		    System.err.println(e.getMessage());
	    }
	    this.longestRoute = board.getLongestRoute();
	    this.allowDoubles = board.areDoublesAllowed();
	    this.deckManager = board.deckManager;
        System.out.println("Board built from Board");
    }

	/**
	 * Setter - sets this.deckManager
	 * @param dm the incoming deck manager
	 *
	 * @pre dm is not null
	 * @post deckManager is set to dm
	 */
	public void setDeckManager(DeckManager dm) {
		deckManager = dm;
	}

	/**
	 * Getter - sees if double routes is enabled or not for the board
	 * @return allowDoubles boolean
	 *
	 * @post retval = this.allowDoubles
	 */
	public boolean areDoublesAllowed() {
		return allowDoubles;
	}

	/**
	 * Getter - acceses the this.routeList
	 * @return routeList - the list of routes that a board has
	 *
	 * @post retval = this.routeList
	 */
    public List<Route> getRoutes(){
        return routeList;
    }

	/**
	 * Setter - sets this.routeList
	 * @param routes the list of routes a board has
	 *
	 * @post routeList is set to routes
	 */
	private void setRoutes(List<Route> routes) {
		routeList = routes;
	}

	/**
	 * Getter (kinda) - calculates the list of routes that have not been claimed
	 * @return the list of routes that have not been claimed
	 *
	 * @post routes will return the routes available
	 */
    public List<Route> getAvailableRoutes(){
	    List<Route> routes = new ArrayList<>();
	    for(Route route : routeList)
	        if(!route.isClaimed())
		        routes.add(route);
        return routes;
    }

	/**
	 * Getter (kinda) calculates the routes that are attached to a city
	 * @param city - a city object that is connected to routes
	 * @return the list of routes that are attached to the given city
	 *
	 * @pre city is not null
	 * @pre city has at least two routes that it is attached to
	 * @post cityRoutes >= 2 routes in its list
	 */
	public List<Route> getRoutes(City city){
	    List<Route> cityRoutes = new ArrayList<>();
	    for(Route route : routeList)
	        if(route.hasCity(city))
		        cityRoutes.add(route);
        return cityRoutes;
    }

	/**
	 * Getter (kinda) - calculates the route with the given ID and returns it
	 * @param routeID the ID of the route in question
	 * @return the route that has the same routeID
	 *
	 * @pre routeID is an ID in the board
	 * @post retvalue = route with the ID || retvalue = null
	 */
	public Route getRoute(int routeID){
	    for(Route route : routeList)
	        if(route.getRouteID() == routeID)
		        return route;
        return null;
    }

	/**
	 * Draws a card from the face up cards using the deckManager
	 * @param position - an int that tells which card from the pile to pick up
	 * @return the train card that was drawn from the face up pile
	 * @throws GameActionException if the deck manager is null
	 *
	 * @pre position > 5
	 * @pre position <= 0
	 * @pre there are face up cards to pick from
	 * @pre the deckManager != null
	 * @post train card is drawn from the card pile and added to users hand
	 * @post train card is replaced from card pile with a new train card from train card deck
	 */
    public TrainCard drawFromFaceUp(int position) throws GameActionException {
	    if(faceUpCards.length <= position || position < 0)
		    throw new GameActionException("Position not accessible in Face Up Cards.");
	    TrainCard toReturn = faceUpCards[position];
	    faceUpCards[position] = null;
	    replaceFaceUpCard();
	    return toReturn;
    }

	/**
	 * Draws a card from the train card deck using the deckManager
	 * @return the train card that was drawn from the train card deck
	 * @throws GameActionException if the deck manager is null
	 *
	 * @pre deck manager is not null
	 * @post exception thrown if deckManager = null
	 * @post retval = trainCard from the train card deck
	 */
	public TrainCard drawFromTrainCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromTrainCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Discards a card into the TrainCard discard pile using the deckManager class
	 * @param card from a player's hand that is to be discarded
	 * @throws GameActionException deckManager is null
	 *
	 * @pre deckManager != null
	 * @post exception if deckManager =null
	 * @post the card is taken from the player's hand and placed into the traincard
	 * 		discard pile
	 */
	public void discard(TrainCard card) throws GameActionException {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Discards a card into the DestinationCard discard pile using the deckManager class
	 * @param card from a player's hand that is to be discarded
	 * @throws GameActionException deckManager is null
	 *
	 * @pre deckManager != null
	 * @post exception if deckManager =null
	 * @post the card is taken from the player's hand and placed into the destination
	 * 		card discard pile
	 */
	public void discard(DestinationCard card) throws GameActionException {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Draws a card from the destination card deck using the deckManager
	 * @return the destination card that was drawn from the destination card deck
	 * @throws GameActionException if the deck manager is null
	 *
	 * @pre deck manager is not null
	 * @post exception thrown if deckManager = null
	 * @post retval = destinationCard from the destination card deck
	 */
    public DestinationCard drawFromDestinationCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromDestinationCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Updates an existing Route in the board
	 * @param r - route object containing information to update a Route with
	 *
	 * @pre r is not null
	 * @pre r contains informaiton that differs from the existing Route it's updating
	 * @pre r has an ID that exists in the Board
	 * @post the Route is updated with information contained in r
	 */
	public void updateRoute(Route r){
	    for(Route route : routeList) {
		    if(route.getRouteID() == r.getRouteID()) {
			    if(!r.equals(route)) {
				    //copy each var
			    }
		    } else if(!allowDoubles && r.isDoubleRoute(route)) {
				route.markDoubleRoute(r);
		    }
	    }
    }

	/**
	 * Setter - sets this.longestRoute
	 * @param longest the new int that is the longest path
	 *
	 * @post longestRoute = longest
	 */
	protected void setLongestRoute(int longest){
        longestRoute = longest;
    }

	/**
	 * Getter - access to longestRoute variable
	 * @return the length of the longest route
	 *
	 * @post retval = this.longestRoute
	 */
	public int getLongestRoute(){ // simple getter
        return longestRoute;
    }

	/**
	 * Getter (kinda) - calculates the longest route that a player has
	 * @param player the player that you want to know their longest route
	 * @return the length of the longest route that player has
	 *
	 * @pre player is not null
	 * @pre player exists in the game
	 * @post retval = the players longest route length
	 */
    public int getLongestRoutePlayer(Player player) { //simple getter
	    return player.getLongestRoute();
    }

	/**
	 * replaces the face up card with a card from the train card deck
	 * 		when a face up card is taken from the pile
	 *
	 * @pre there is a card to be replaced
	 * @pre there is a card in the train card deck to replace it with
	 * @post the card is replaced with a card from the train card deck
	 * @post exception if there is no more cards in the train card deck
	 */
	private void replaceFaceUpCard(){
	    for(int i = 0; i < FACEUP_DECK_SIZE; i++) {
		    if(faceUpCards[i] == null) {
			    try {
				    faceUpCards[i] = drawFromTrainCardDeck();
			    } catch(GameActionException e) {
				    e.printStackTrace();
				    System.out.println("Out of Cards!");
				    break;
			    }
		    }
	    }
	    while(shouldResetFaceUp())
		    resetFaceUp();
    }

	/**
	 * Lets the game know when it is time to replace all the cards in the face up
	 * 		pile because there are too many wilds
	 * @return true if there are 3+ wilds, false otherwise
	 *
	 * @pre there are more than 3 cards in the face up pile
	 * @pre a card has just been replaced in the face up pile, so now we have to
	 * 		check if the new card throws over the balance of >3 or <3
	 * @post retval = false if <3 wilds, true if >3 wild cards
	 */
	private Boolean shouldResetFaceUp(){
	    int wildcount = 0;
	    for(int i = 0; i < FACEUP_DECK_SIZE; i++)
		    if(faceUpCards[i] != null && faceUpCards[i].getColor() == TrainCardColor.WILD)
			    wildcount++;
	    return wildcount >= 3;
    }

	/**
	 * resets all the face up cards because there were too many wilds
	 * 		discards the previous face up cards and draws 5 brand new cards from the
	 * 		train card deck and places them in the face up card spots
	 *
	 * @pre there are enough cards in the train card deck to replace the face up cards
	 * @pre shouldResetFaceUp() function returns true
	 * @post exception if there is not enough train card deck cards to replace
	 * @post the previous face up cards were discarded
	 * @post there are 5 brand new face up cards that were drawn from the train card deck
	 */
	private void resetFaceUp(){
	    for(int i = 0; i < FACEUP_DECK_SIZE; i++) {
		    try {
			    discard(faceUpCards[i]);
			    faceUpCards[i] = drawFromTrainCardDeck();
		    } catch(GameActionException e) {
			    e.printStackTrace();
			    System.out.println("Out of Cards!");
			    break;
		    }
	    }
    }

	/**
	 * Getter - access to this.faceUpCards
	 * @return the array of face up cards
	 *
	 * @post retval = this.faceUpCards
	 */
	public TrainCard[] getFaceUpCards() {
        return faceUpCards;
    }

	/**
	 * Setter (kinda) - sets the face up cards with an array of pre-chosen train cards
	 * @param cards - an array of pre-chosen train cards
	 * @throws GameActionException if list of cards had misinformation
	 *
	 * @pre cards is not null
	 * @pre cards are valid
	 * @post this.faceUpCards = cards
	 */
	public void setFaceUpCards(TrainCard[] cards) throws GameActionException {
	    if(faceUpCards == null)
		    faceUpCards = new TrainCard[FACEUP_DECK_SIZE];
        if(cards != null && cards.length == FACEUP_DECK_SIZE)
	        for(int i = 0; i < FACEUP_DECK_SIZE; i++)
		        faceUpCards[i] = new TrainCard((cards[i] != null ? cards[i].getColor() : null));
	    else
	        throw new GameActionException("List of cards was not correct");
	    if(shouldResetFaceUp())
		    resetFaceUp(); //TODO idk if I should put this here...
    }
 }
