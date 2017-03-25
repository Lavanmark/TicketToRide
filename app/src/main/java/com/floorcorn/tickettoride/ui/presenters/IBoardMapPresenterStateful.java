package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import java.util.List;

/**
 * Created by Lily on 3/24/17
 * Includes the methods that the states need from the IBoardMap Presenter
 */

public interface IBoardMapPresenterStateful {

    //enable and disable buttons
    public void enableDrawTrainCards();

    public void enableDrawDestinationCards();

    public void enableClaimRoute();

    public void disableDrawTrainCards();

    public void disableDrawDestinationCards();

    public void disableClaimRoute();

    //opening and closing drawers
    public void openDestinationDrawer();

    public void openClaimRouteDrawer();

    public void openDrawTrainDrawer();

    public void closeDestinationDrawer();

    public void closeClaimRouteDrawer();

    public void closeDrawTrainDrawer();

    public void lockDrawerClosed();

    //TODO: drawer listeners

    //state behavior
    public void setState(IState state);

    public IState getState();

    // toasts
    public void displayMessage_short(String message);

    public void displayMessage_long(String message);

    //pertaining to view
    public IBoardmapView getView();

    //pertaining to game
    public Game getGame();

    public User getUser();

    //pertaining to destinations
    public int[] getDiscardableDestinationCards() throws Exception;

    public void updateDestinationDrawer();

    public DestinationCard[] getDestCardsToDiscard();

    public void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard);

    public void discardDestinations(boolean[] shouldDiscard);

    //pertaining to train cards
    public boolean drawTrainCardFromDeck();

    public boolean drawFromFaceUp(int position);

    public int[] getFaceupCardColors() throws GameActionException;

    public void drawNewDestinationCards();

    //pertaining to routes
    public List<Route> getRoutes();

    public void claimButtonClicked(Route route);

    public boolean canClaim(Route route);
}
