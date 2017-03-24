package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Lily on 3/24/17
 * Includes the methods that the states need from the IBoardMap Presenter
 */

public interface IBoardMapPresenterStateful {

    // Incomplete
    public void enableDrawTrainCards();
    public void enableDrawDestinationCards();
    public void enableClaimRoute();
    public void disableDrawTrainCards();
    public void disableDrawDestinationCards();
    public void disableClaimRoute();

    //TODO: should we pass the whole drawer to the state instead of having all these??
    public void openDestinationDrawer();
    public void openClaimRouteDrawer();
    public void openDrawTrainDrawer();
    public void closeDestinationDrawer();
    public void closeClaimRouteDrawer();
    public void closeDrawTrainDrawer();
    public void setState(IState state);
    public void displayMessage_short(String message);
    public void displayMessage_long(String message);

    public int[] getDiscardableDestinationCards() throws Exception;
    public void updateDestinationDrawer();

    void tryOpenDestinationDrawer();

    void tryOpenDrawTrainDrawer();

    void tryOpenClaimRouteDrawer();

    public IBoardmapView getView();
    public void setView(IBoardmapView view);
    public Game getGame();
    public User getUser();
    public IState getState();
    public DestinationCard[] getDestCardsToDiscard();
    public void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard);
}
