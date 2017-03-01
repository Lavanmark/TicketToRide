package com.floorcorn.tickettoride.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 2/24/2017.
 */

public class Board {
    private List<Route> routeList;
    private List<TrainCard> faceUpCards;

	@JsonIgnore
    private DeckManager deckManager;

	private int longestPath;
    private int longestPathPlayer;


	private Board(){}
	/**
	 * Create a new board with the specified routes in route list
	 * @param routeList list of routes on the boardmap. Remove double routes prior to call.
	 */
    public Board(List<Route> routeList) {
        this.routeList = routeList;
        this.deckManager = new DeckManager();
        this.faceUpCards = new ArrayList<>();
        this.longestPath = 0;
        this.longestPathPlayer = -1;
    }

    public Board(Board board) {
        this.routeList = board.getRoutes();
        this.faceUpCards = board.getFaceUpCards();
	    this.deckManager = board.deckManager;
	    this.longestPath = board.getLongestPath();
	    this.longestPathPlayer = board.getLongestPathPlayer();
    }

    public List<Route> getRoutes(){
        return routeList;
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

    public TrainCard drawFromFaceUp(int position){
        return faceUpCards.get(position);
    }

    public TrainCard drawFromTrainCardDeck(){
        return deckManager.drawFromTrainCardDeck();
    }

    public void discard(TrainCard card){
        deckManager.discard(card);
    }

    public void discard(DestinationCard card){
        deckManager.discard(card);
    }

    public DestinationCard drawFromDestinationCardDeck(){
        return deckManager.drawFromDestinationCardDeck();
    }

    public void updateRoute(Route r){
	    for(Route route : routeList) {
		    if(route.getRouteID() == r.getRouteID()) {
			    //TODO what happens here?
		    }
	    }
    }

    public int getLongestPath(){
        return longestPath;
    }

    public int getLongestPathPlayer() {
        //calculates the longestPath of any player and sets the longestPath variable
	    return longestPathPlayer;
    }

    private void replaceFaceUpCard(){
        //this replaces a card that was drawn from teh face up pile
    }

    private Boolean shouldResetFaceUp(){
        return false;
        // if there is a card missing, set the bool to true
    }

    private void resetFaceUp(){
        //if there are >3 wild cards then trash all the face up and replace them with new ones.repeat if necessary
    }
    public List<TrainCard> getFaceUpCards() {
        return faceUpCards;
    }
    public void setFaceUpCards(List<TrainCard> cards) {
        faceUpCards = cards;
    }
 }
