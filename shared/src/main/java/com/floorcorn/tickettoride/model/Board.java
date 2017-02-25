package com.floorcorn.tickettoride.model;

import java.util.List;

/**
 * Created by Kaylee on 2/24/2017.
 */

public class Board {
    private List<Route> routeList;
    private List<TrainCard> faceUpCards;
    private DeckManager deckMan;
    private int longestPath;
    private int longestPathPlayer;

    public Board(List<Route> routeList, List<TrainCard> faceUpCards, DeckManager deckMan, int longestPath, int longestPathPlayer) {
        this.routeList = routeList;
        this.faceUpCards = faceUpCards;
        this.deckMan = deckMan;
        this.longestPath = longestPath;
        this.longestPathPlayer = longestPathPlayer;
    }

    public List<Route> getRoutes(){
        return routeList;
    }

    public List<Route> getAvailableRoutes(){
        return null; //return the routes that are available
        // how do we tell theyre available?
    }

    public List<Route> getRoutes(City city){
        return null;
        //returns the routes connected to the city
        //probably have to add that data type to the city class
    }

    public Route getRoute(int routeID){
        return null;
        //make a getRoute(routeID) on the Route class and call it in this funciton and then return the result
    }

    public TrainCard drawFromFaceUp(int pos){
        return faceUpCards.get(pos); // what the heck is pos?
    }

    public TrainCard drawFromTrainCardDeck(){
        return null;
        //where is this?
    }

    public void discard(TrainCard card){
        //discards - calls from other class's fxn
    }

    public void discard(DestinationCard card){
        //calls from diff class
    }

    public DestinationCard drawFromDestinationCardDeck(){
        return null;
        //calls from diferent class
    }

    public void updateRoute(Route r){
        //what is this? updating to claimed or something?
    }

    public void removeDoubleRoutes(){
        //for 2 players
    }

    public int getLongestPath(){
        return longestPath;
    }

    public void getLongestPathPlayer() {
        //calculates the longestPath of any player and sets the longestPath variable
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
 }
