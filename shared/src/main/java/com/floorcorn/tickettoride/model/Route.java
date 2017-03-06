package com.floorcorn.tickettoride.model;

import java.util.Map;

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
	    if(claimed)
		    return;
	    if(!canClaim(p))
		    return;

	    Map<TrainCardColor, Integer> pCards = p.getTrainCards();
	    if(color != TrainCardColor.WILD) {
			int colornum = pCards.get(color);
		    if(colornum >= length) {
			    for(int i = 0; i < length; i++)
				    p.removeTrainCard(new TrainCard(color));
		    } else {
			    for(int i = 0; i < colornum; i++)
				    p.removeTrainCard(new TrainCard(color));
			    for(int i = 0; i < length - colornum; i++)
				    p.removeTrainCard(new TrainCard(TrainCardColor.WILD));
		    }
		} else {
		    //TODO optimize card removal
		    int most = 0;
		    TrainCardColor mostColor = TrainCardColor.WILD;
		    int wild = 0;
		    for(TrainCardColor tcc : pCards.keySet()) {
			    if(tcc == TrainCardColor.WILD) {
				    wild = pCards.get(tcc);
				    continue;
			    }
				int num = pCards.get(tcc);
			    if(num > most) {
				    most = num;
				    mostColor = tcc;
			    }
			    if(num >= length) {
				    for(int i = 0; i < length; i++)
					    p.removeTrainCard(new TrainCard(tcc));
				    most = 0;
				    break;
			    }
		    }
		    if(most > 0) {
			    if(most + wild >= length)
				    for(int i = 0; i < length; i++)
					    p.removeTrainCard(new TrainCard(mostColor));
		    }
	    }

	    p.removeTrainCars(length);
        claimed = true;
	    owner = p.getPlayerID();
	    p.claimRoute(this);
    }

    public Boolean canClaim(Player p){
	    if(p.getTotalTrainCards() < length) //Route longer than the amount of cards player has
		    return false;

        Map<TrainCardColor, Integer> pCards = p.getTrainCards();
	    int wildnum = 0;
	    if(pCards.containsKey(TrainCardColor.WILD)) //Save num wilds
		    wildnum = pCards.get(TrainCardColor.WILD);
	    if(wildnum >= length) //Have enough wilds?
		    return true;

	    if(color == TrainCardColor.WILD) { //If path is wild, check all color types.
		    for(TrainCardColor tcc : pCards.keySet()) {
			    if(tcc != TrainCardColor.WILD && pCards.get(tcc) + wildnum >= length)
				    return true;
		    }
		    return false;
	    }

	    int colornum = 0;
	    if(pCards.containsKey(color)) //Check the specific color
		    colornum = pCards.get(color);
	    return colornum + wildnum >= length;
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
