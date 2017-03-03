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
}
