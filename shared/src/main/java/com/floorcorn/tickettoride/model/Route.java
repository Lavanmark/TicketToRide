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

    private Route(){}
    public Route(int rID, City c1, City c2, int l, TrainCardColor tcc){
        routeID = rID;
        city1 = c1;
        city2 = c2;
        length = l;
        color = tcc;
        claimed = false;
        owner = Player.NO_PLAYER_ID;
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

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if(routeID != route.routeID) return false;
        if(length != route.length) return false;
        if(owner != route.owner) return false;
        if(!city1.equals(route.city1)) return false;
        if(!city2.equals(route.city2)) return false;
        if(color != route.color) return false;
        return claimed.equals(route.claimed);

    }

    @Override
    public int hashCode() {
        int result = routeID;
        result = 31 * result + city1.hashCode();
        result = 31 * result + city2.hashCode();
        result = 31 * result + length;
        result = 31 * result + color.hashCode();
        result = 31 * result + claimed.hashCode();
        result = 31 * result + owner;
        return result;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(city1 + " to " + city2);
        return sb.toString();
    }
}
