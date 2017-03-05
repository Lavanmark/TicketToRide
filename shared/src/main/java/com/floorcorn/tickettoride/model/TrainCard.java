package com.floorcorn.tickettoride.model;

/**
 * Created by Kaylee on 2/23/2017.
 */

public class TrainCard {
    private TrainCardColor color;

    private TrainCard(){}


    public TrainCard(TrainCardColor tcc){
        color = tcc;
    }

    public TrainCardColor getColor(){
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        TrainCard trainCard = (TrainCard) o;

        return color == trainCard.color;
    }

    @Override
    public int hashCode() {
        return color.hashCode();
    }
}
