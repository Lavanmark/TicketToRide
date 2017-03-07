package com.floorcorn.tickettoride.model;

import java.util.List;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class DestinationCard {
    private City city1;
    private City city2;
    private int value;
    private String resName;
    private Boolean canDiscard;

    private DestinationCard(){}
    public DestinationCard(City c1, City c2, int v, String res){
        city1 = c1;
        city2 = c2;
        value = v;
        canDiscard = true;
        resName = res;
        System.out.println("new Destination Card: "+resName);
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

    public String getResName() { return resName;}

    public Boolean canDiscard(){
        return canDiscard;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        DestinationCard that = (DestinationCard) o;

        if(value != that.value) return false;
        if(!city1.equals(that.city1)) return false;
        if(!city2.equals(that.city2)) return false;
        return canDiscard.equals(that.canDiscard);

    }

    @Override
    public int hashCode() {
        int result = city1.hashCode();
        result = 31 * result + city2.hashCode();
        result = 31 * result + value;
        result = 31 * result + canDiscard.hashCode();
        return result;
    }

    @Override
    public String toString(){
        String s = city1.getName() + " to " + city2.getName() +": " + value;
        return s;
    }


}
