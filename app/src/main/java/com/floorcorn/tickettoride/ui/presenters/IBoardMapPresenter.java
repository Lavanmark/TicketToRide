package com.floorcorn.tickettoride.ui.presenters;

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
}
