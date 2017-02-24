package com.floorcorn.tickettoride.model;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class Route {
    private int routeID;
    private City city1;
    private City city2;
    private int length;
    private TrainCardColor color;
    private Boolean claimed;
    private int owner;

    public Route(int rID, City c1, City c2, int l, TrainCardColor tcc, Boolean claim, int o){
        routeID = rID;
        city1 = c1;
        city2 = c2;
        length = l;
        color = tcc;
        claimed = claim;
        owner = o;
    }

    public int getRouteID(){
        return routeID;
    }

    public City getFirstCity(){
        return city1;
    }

    public City getSecondCity(){
        return city2;
    }

    public Boolean hasCity(City city){
        return (city.equals(city1)||city.equals(city2));
    }

    public int getLength(){
        return length;
    }

    public TrainCardColor getColor(){
        return color;
    }

    public Boolean isClaimed(){
        return claimed;
    }

    public int getOwner(){
        return owner;
    }

    public void claim(Player p){
        //claim route
        //take route out of list
        //add route to the players list of routes
    }

    public Boolean canClaim(Player p){
        return false;
        //checks if the player has enough of the right cards to play the route they want to claim.
        //if they do, then call claim(Player p) to caim the route
        // if not, then return false
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(city1 + " to " + city2);
        return sb.toString();
    }
}
