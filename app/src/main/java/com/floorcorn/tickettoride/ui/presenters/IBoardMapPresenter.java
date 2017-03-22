package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.states.IState;

/**
 * Created by Michael on 3/15/2017.
 */

public interface IBoardMapPresenter {

    public void enableDrawTrainCards();
    public void enableDrawDestinationCards();
    public void enableClaimRoute();
    public void disableDrawTrainCards();
    public void disableDrawDestinationCards();
    public void disableClaimRoute();

    public void openDestinationDrawer();
    public void openClaimRouteDrawer();
    public void openDrawTrainDrawer();
    public void closeDestinationDrawer();
    public void closeClaimRouteDrawer();
    public void closeDrawTrainDrawer();
    public void setState(IState state);
    public void displayMessage_short(String message);
    public void displayMessage_long(String message);
}
