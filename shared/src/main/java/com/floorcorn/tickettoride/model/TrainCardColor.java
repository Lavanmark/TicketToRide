package com.floorcorn.tickettoride.model;

/**
 * Created by Kaylee on 2/23/2017.
 */

public enum TrainCardColor {
    RED, GREEN, BLUE, YELLOW, PURPLE, BLACK, WHITE, ORANGE, WILD;
    public static TrainCardColor convertString(String colorString){
        colorString = colorString.toLowerCase();
        switch(colorString) {
            case "black":
                return TrainCardColor.BLACK;
            case "blue":
                return TrainCardColor.BLUE;
            case "red":
                return TrainCardColor.RED;
            case "green":
                return TrainCardColor.GREEN;
            case "yellow":
                return TrainCardColor.YELLOW;
            case "orange":
                return TrainCardColor.ORANGE;
            case "purple":
                return TrainCardColor.PURPLE;
            case "white":
                return TrainCardColor.WHITE;
            case "grey":
            case "gray":
            case "wild":
	        case "rainbow":
                return TrainCardColor.WILD;
            default:
                return null;
        }
    }
}
