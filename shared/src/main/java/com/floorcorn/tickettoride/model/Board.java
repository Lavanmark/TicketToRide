package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by Kaylee on 2/24/2017.
 * @author Kaylee Jones
 * @author Joseph Hansen
 */

/**
 * This class represents the game board for Ticket to Ride, especially the visible elements of
 * the game such as the routes and the cards.
 *
 * @invariant user is logged in
 * @invariant each board belongs to a game; to initialize a board, the game must exist
 * @invariant game rules are honored (such as 0 < numPlayers <= 5)
 */
public class Board {
	/**
	 * Constant value number of face up cards.
	 */
	public static final int FACEUP_DECK_SIZE = 5;

	/**
	 * List of Route objects containing all of the routes for this game's board.
	 */
    private List<Route> routeList;
	/**
	 * Array of TrainCard objects representing the face up cards.
	 */
    private TrainCard[] faceUpCards;

	/**
	 * DeckManager object that manages the cards and drawing card stuff.
	 */
	@JsonIgnore
    private DeckManager deckManager;

	/**
	 * The length of the current longest route in this game.
	 */
	private int longestRoute;
	/**
	 * The boolean for whether to allow double routes (allow only for games with more than 3
	 * players).
	 */
	private boolean allowDoubles = false;

	/**
	 * Empty constructor. Constructor with no parameters present for Jackson to work.
	 *
	 * @pre (none)
	 * @post Board object initialized
	 */
	private Board() {}

	/**
	 * Create a new board with the specified routes in route list.
	 *
	 * @pre routeList != null
	 * @post this routeList set to param routeList
	 * @post this allowDoubles set to param allowDoubles
	 * @post this faceUpCards initialized
	 * @post longestRoute == 0
	 * @post deckManager == null
	 * @param routeList List of Route objects on the Boardmap.
	 */
    public Board(List<Route> routeList, boolean allowDoubles) {
        this.routeList = routeList;
        this.faceUpCards = new TrainCard[FACEUP_DECK_SIZE];
        this.longestRoute = 0;
        //this.longestRoutePlayer = -1;
	    this.deckManager = null;
	    this.allowDoubles = allowDoubles;
		Corn.log("Board built from routeList");
    }

	/**
	 * Create a new board with a Board parameter.
	 *
	 * @pre param board != null
	 * @pre param board.getFaceUpCards() != null
	 * @post routeList == new ArrayList<>(param board.getRoutes())
	 * @post faceUpCards initialized anew
	 * @post longestRoute == param board.getLongestroute()
	 * @post allowDoubles == param board.areDoublesAllowed()
	 * @post deckManager == param board.deckManager
	 * @param board Board object to "copy"
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
	    //this.longestRoutePlayer = board.getLongestRoutePlayer(player);
	    this.allowDoubles = board.areDoublesAllowed();
	    this.deckManager = board.deckManager;
        Corn.log("Board built from Board");
    }

	/**
	 * Sets the deck manager.
	 *
	 * @pre param DeckManager dm != null
	 * @post deckManager == param dm
	 * @param dm DeckManager object
	 */
	public void setDeckManager(DeckManager dm) {
		deckManager = dm;
	}

	/**
	 * Tells whether this game allows double routes.
	 *
	 * @pre (none)
	 * @post returned allowDoubles
	 * @return
	 */
	public boolean areDoublesAllowed() {
		return allowDoubles;
	}

	/**
	 * Returns the route list from this board.
	 *
	 * @pre (none)
	 * @post returned routeList
	 * @return List of Route objects
	 */
    public List<Route> getRoutes(){
        return routeList;
    }

	/**
	 * Sets the route list for this board.
	 *
	 * @pre routes != null
	 * @post routeList == param routes
	 * @param routes
	 */
	private void setRoutes(List<Route> routes) {
		routeList = routes;
	}

	/**
	 * Returns the list of available routes from this board.
	 *
	 * @pre routeList is initialized
	 * @post returned list of routes that are not claimed from routeList
	 * @post routeList unchanged
	 * @return List of Route objects available routes
	 */
    public List<Route> getAvailableRoutes() {
	    List<Route> routes = new ArrayList<>();
	    for (Route route : routeList)
	        if (!route.isClaimed())
		        routes.add(route);
        return routes;
    }

	/**
	 * Returns the list of routes that have the city "city" as an endpoint.
	 *
	 * @pre city != null
	 * @post returned sublist of routeList where each route r has one endpoint == param city
	 * @post routeList unchanged
	 * @param city City object
	 * @return List of Route objects routes with city as an endpoint
	 */
	public List<Route> getRoutes(City city){
	    List<Route> cityRoutes = new ArrayList<>();
	    for (Route route : routeList)
	        if (route.hasCity(city))
		        cityRoutes.add(route);
        return cityRoutes;
    }

	/**
	 * Gets route with ID that matches routeID.
	 *
	 * @pre routeID is a valid integer
	 * @post if routeID does not match any route in routeList: returned null
	 * @post if routeID matches a route r in routeList: returned route r
	 * @param routeID int ID of the route to find
	 * @return Route object who has route ID or null
	 */
	public Route getRoute(int routeID) {
	    for (Route route : routeList)
	        if (route.getRouteID() == routeID)
		        return route;
        return null;
    }

	/**
	 * Draws the card at position "position" (probably because the player selected that position).
	 *
	 * @pre 0 <= position < faceUpCards.length
	 * @post returned TrainCard at position "position"
	 * @post face up card spot at position "position" has a new card from deck
	 * @post deck has one less card in it
	 * @param position
	 * @return TrainCard object card drawn
	 * @throws GameActionException
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
	 * Draws the card from the top of the deck.
	 *
	 * @pre deckManager != null
	 * @post returned the train card from the top of the deck
	 * @post size(train card deck) == size(old(train card deck)) - 1
	 * @return TrainCard object card drawn
	 * @throws GameActionException
	 */
	public TrainCard drawFromTrainCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromTrainCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Discards the TrainCard "card."
	 *
	 * @pre deckManager != null
	 * @pre card != null)
	 * @post no one owns TrainCard "card"
	 * @post deck does not own TrainCard "card"
	 * @post game has one less train car card
	 * @param card TrainCard object to discard
	 */
	public boolean discard(TrainCard card) {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        return false;
		return true;
    }

	/**
	 * Discards the DestinationCard "card."
	 *
	 * @pre deckManager != null
	 * @pre card != null
	 * @post no one owns DestinationCard "card"
	 * @post deck does not own DestinationCard "card"
	 * @game has one less destination ticket
	 * @param card DestinationCard object to discard
	 */
	public boolean discard(DestinationCard card) {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        return false;
		return true;
    }

	/**
	 * Draw top card from destination card deck.
	 *
	 * @pre deckManager != null
	 * @post returned top card on the deck of destination tickets
	 * @post size(deck of destination tickets) == size(old(deck of destination tickets)) - 1
	 * @return DestinationCard object card drawn
	 * @throws GameActionException
	 */
	public DestinationCard drawFromDestinationCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromDestinationCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

	/**
	 * Updates route "r."
	 *
	 * @pre r != null
	 * @pre r matches a route in the routeList
	 * @post the route in the routeList that matches r is updated so routeList[position] == r
	 * @param r Route to update
	 */
	public void updateRoute(Route r) {
		if(r == null)
			return;
	    for (Route route : routeList) {
		    if (route.getRouteID() == r.getRouteID()) {
			    if (!route.equals(r)) {
					route.update(r);
			    }
		    } else if (!allowDoubles && r.isDoubleRoute(route)) {
				route.markDoubleRoute(r);
		    }
	    }
    }

	/**
	 * Sets longest route score.
	 *
	 * @pre longest is a valid int
	 * @post longestRoute == param longest
	 * @param longest int length of the longest route
	 */
	protected void setLongestRoute(int longest) {
        longestRoute = longest;
    }

	/**
	 * Returns length of the longest route.
	 *
	 * @pre longestRoute is initialized
	 * @post returned length of longest route
	 * @return int length of longest route
	 */
    public int getLongestRoute() {
        return longestRoute;
    }

	/**
	 * Returns length of longest route for player "player."
	 *
	 * @pre player != null
	 * @post returned length of longest route of this player
	 * @param player Player object
	 * @return int length of longest route for this player
	 */
	public int getLongestRoutePlayer(Player player) {
	    return player.getLongestRoute();
    }

	/**
	 * Replaces the face up card spot that was vacated.
	 *
	 * @pre num cards in train card deck > 0
	 * @pre one or more of the face up card spots was just vacated (card drawn)
	 * @post vacated face up spot(s) filled by drawing top card from train card deck
	 */
	private void replaceFaceUpCard() {
	    for (int i = 0; i < FACEUP_DECK_SIZE; i++) {
		    if (faceUpCards[i] == null) {
			    try {
				    faceUpCards[i] = drawFromTrainCardDeck();
			    } catch (GameActionException e) {
				    e.printStackTrace();
				    Corn.log(Level.SEVERE, "Out of Cards!");
				    break;
			    }
		    }
	    }
	    while (shouldResetFaceUp())
		    resetFaceUp();
    }

	/**
	 * Tells whether there is a need to reset face up cards.
	 *
	 * @pre face up cards initialized
	 * @post if number of face up wild cards > 2: returned true, else: returned false
	 * @return
	 */
	private Boolean shouldResetFaceUp() {
	    int wildcount = 0;
	    for (int i = 0; i < FACEUP_DECK_SIZE; i++)
		    if (faceUpCards[i] != null && faceUpCards[i].getColor() == TrainCardColor.WILD)
			    wildcount++;
	    return wildcount >= 3;
    }

	/**
	 * Resets up the face up cards.
	 *
	 * @pre face up cards initialized
	 * @pre one of the game rules regarding face up cards is violated (probably there are too many
	 * 		face up wild cards (greater than 2))
	 * @post old(face up cards) are discarded
	 * @post face up cards are replaced by drawing top card from train card deck until face up
	 * 		card spots are filled
	 */
	private void resetFaceUp() {
	    for (int i = 0; i < FACEUP_DECK_SIZE; i++) {
		    try {
			    discard(faceUpCards[i]);
			    faceUpCards[i] = drawFromTrainCardDeck();
		    } catch(GameActionException e) {
			    e.printStackTrace();
			    Corn.log(Level.SEVERE, "Out of Cards!");
			    break;
		    }
	    }
    }

	/**
	 * Returns the face up cards.
	 *
	 * @pre face up cards are initialized
	 * @post returned face up cards
	 * @return TrainCard[] array of TrainCard objects that are the face up cards
	 */
	public TrainCard[] getFaceUpCards() {
        return faceUpCards;
    }

	/**
	 * Sets the face up cards.
	 *
	 * @pre face up cards != null
	 * @pre param cards != null
	 * @pre size(param cards) == faceup deck size (constant)
	 * @pre param cards isn't violating a game rule (probably the only applicable rule is that
	 * 		there cannot be more than 2 wild cards in the face up cards)
	 * @post for i = 0 through face up deck size - 1: faceUpCards[i] == new card using
	 * 		param cards[i]
	 * @param cards array of TrainCard to set as face up cards
	 * @throws GameActionException
	 */
    public void setFaceUpCards(TrainCard[] cards) throws GameActionException {
	    if (faceUpCards == null)
		    faceUpCards = new TrainCard[FACEUP_DECK_SIZE];
        if (cards != null && cards.length == FACEUP_DECK_SIZE)
	        for(int i = 0; i < FACEUP_DECK_SIZE; i++)
		        faceUpCards[i] = new TrainCard((cards[i] != null ? cards[i].getColor() : null));
	    else
	        throw new GameActionException("List of cards was not correct");
	    if (shouldResetFaceUp())
		    resetFaceUp(); //TODO idk if I should put this here...
    }
 }
