package com.floorcorn.tickettoride.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class DestinationCard {
    private City city1;
    private City city2;
    private int value;
    private String resName;
    private boolean canDiscard;
    private boolean complete;

    private DestinationCard(){}
    public DestinationCard(City c1, City c2, int v, String res){
        city1 = c1;
        city2 = c2;
        value = v;
        canDiscard = true;
        complete = false;
        resName = res;
    }

    public boolean isComplete(){
        return complete;
    }
    
    public boolean checkCompletion(List<Route> routes) {
        if(complete)
            return complete;
        boolean firstFound = false;
        boolean secondFound = false;
        //check that routes contain both cities
        for(Route r : routes) {
            if(city1.equals(r.getFirstCity()) || city1.equals(r.getSecondCity()))
                firstFound = true;
            if(city2.equals(r.getFirstCity()) || city2.equals(r.getSecondCity()))
                secondFound = true;
        }
        if(!firstFound || !secondFound)
            return (complete = false);
	
	    Map<City, List<Route>> map = Route.buildCityRouteMap(routes);
	    
	    return depthFirst(map, city1);
    }
    
    private boolean depthFirst(Map<City, List<Route>> map, City at) {
	    if(city2.equals(at))
	    	return true;
	    for(Route route : map.get(at)) {
		    if(route.visited)
			    continue;
		    route.visited = true;
		    boolean found = depthFirst(map, at.equals(route.getFirstCity())? route.getSecondCity() : route.getFirstCity());
		    route.visited = false;
		    if(found)
			    return true;
	    }
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
    public void setCanDiscard(boolean discard) {
        canDiscard = discard;
    }
    
    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        
        DestinationCard that = (DestinationCard) o;
        
        if(value != that.value) return false;
        if(canDiscard != that.canDiscard) return false;
        if(complete != that.complete) return false;
        if(city1 != null ? !city1.equals(that.city1) : that.city1 != null) return false;
        if(city2 != null ? !city2.equals(that.city2) : that.city2 != null) return false;
        return resName != null ? resName.equals(that.resName) : that.resName == null;
    
    }
    
    @Override
    public int hashCode() {
        int result = city1 != null ? city1.hashCode() : 0;
        result = 31 * result + (city2 != null ? city2.hashCode() : 0);
        result = 31 * result + value;
        result = 31 * result + (resName != null ? resName.hashCode() : 0);
        result = 31 * result + (canDiscard ? 1 : 0);
        result = 31 * result + (complete ? 1 : 0);
        return result;
    }
    
    @Override
    public String toString(){
        return city1.getName() + " to " + city2.getName() + ": " + value + (complete? " complete" : "");
    }


}
