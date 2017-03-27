package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.drawers.BMDrawer;

import java.util.List;

/**
 * Created by Lily on 3/24/17
 * Includes the methods that the states need from the IBoardMap Presenter
 */

public interface IBoardMapPresenterStateful {
    
    //opening and closing drawers
    void openDestinationDrawer();
    
    BMDrawer getOpenDrawer();

    //state behavior
    void setState(IState state);
    
    // toasts
    void displayMessage_short(String message);

	void displayMessage_long(String message);

    //pertaining to view
    IBoardmapView getView();

    //pertaining to game
    Game getGame();

	User getUser();

    //pertaining to destinations
    int[] getDiscardableDestinationCards() throws Exception;

	void updateDestinationDrawer();
    
    void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard);
    
    int getDiscardableCount();
}
