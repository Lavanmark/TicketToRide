package com.floorcorn.tickettoride.model;

import java.util.List;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class DestinationCard {
    private City city1;
    private City city2;
    private int value;
    private Boolean canDiscard;

    public DestinationCard(City c1, City c2, int v){
        city1 = c1;
        city2 = c2;
        value = v;
        canDiscard = true;
    }

    public Boolean isComplete(List<Route> routes){
        //do dijksra's on this
        return false;
    }

    public int getValue(){
        return value;
    }

    public City getFirstCity(){
        return city1;
    }

    public City getSecondCity(){
        return city2;
    }

    public Boolean canDiscard(){
        return canDiscard;
    }
}
