package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;

import java.util.ArrayList;
import java.util.List;

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
	 * @param routeList list of routes on the boardmap.
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

	public void setDeckManager(DeckManager dm) {
		deckManager = dm;
	}

	public boolean areDoublesAllowed() {
		return allowDoubles;
	}

    public List<Route> getRoutes(){
        return routeList;
    }
	private void setRoutes(List<Route> routes) {
		routeList = routes;
	}

    public List<Route> getAvailableRoutes(){
	    List<Route> routes = new ArrayList<>();
	    for(Route route : routeList)
	        if(!route.isClaimed())
		        routes.add(route);
        return routes;
    }

    public List<Route> getRoutes(City city){
	    List<Route> cityRoutes = new ArrayList<>();
	    for(Route route : routeList)
	        if(route.hasCity(city))
		        cityRoutes.add(route);
        return cityRoutes;
    }

    public Route getRoute(int routeID){
	    for(Route route : routeList)
	        if(route.getRouteID() == routeID)
		        return route;
        return null;
    }

    public TrainCard drawFromFaceUp(int position) throws GameActionException {
	    if(faceUpCards.length <= position || position < 0)
		    throw new GameActionException("Position not accessible in Face Up Cards.");
	    TrainCard toReturn = faceUpCards[position];
	    faceUpCards[position] = null;
	    replaceFaceUpCard();
	    return toReturn;
    }

    public TrainCard drawFromTrainCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromTrainCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

    public void discard(TrainCard card) throws GameActionException {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        throw new GameActionException("No Deck Manager!");
    }

    public void discard(DestinationCard card) throws GameActionException {
        if(deckManager != null)
	        deckManager.discard(card);
	    else
	        throw new GameActionException("No Deck Manager!");
    }

    public DestinationCard drawFromDestinationCardDeck() throws GameActionException {
	    if(deckManager != null)
            return deckManager.drawFromDestinationCardDeck();
	    throw new GameActionException("No Deck Manager!");
    }

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

    protected void setLongestRoute(int longest){
        longestRoute = longest;
    }

    public int getLongestRoute(){ // simple getter
        return longestRoute;
    }

    public int getLongestRoutePlayer(Player player) { //simple getter
	    return player.getLongestRoute();
    }

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

    private Boolean shouldResetFaceUp(){
	    int wildcount = 0;
	    for(int i = 0; i < FACEUP_DECK_SIZE; i++)
		    if(faceUpCards[i] != null && faceUpCards[i].getColor() == TrainCardColor.WILD)
			    wildcount++;
	    return wildcount >= 3;
    }

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

    public TrainCard[] getFaceUpCards() {
        return faceUpCards;
    }
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
