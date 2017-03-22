package com.floorcorn.tickettoride.ui.presenters.TestPresenters;

import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/22/2017.
 */

public class MockBoardMapPresenter implements IBoardMapPresenter {

    public boolean drawTrainCardsEnabled;
    public boolean drawDestinationCardsEnabled;
    public boolean claimRoutesEnabled;

    public IState state;

    public MockBoardMapPresenter(){
        drawDestinationCardsEnabled = false;
        drawTrainCardsEnabled = false;
        claimRoutesEnabled = false;
    }
    @Override
    public void enableDrawTrainCards() {
        this.drawTrainCardsEnabled = true;
    }

    @Override
    public void enableDrawDestinationCards() {
        this.drawDestinationCardsEnabled = true;
    }

    @Override
    public void enableClaimRoute() {
        this.claimRoutesEnabled = true;
    }

    @Override
    public void disableDrawTrainCards() {
        this.drawTrainCardsEnabled = false;
    }

    @Override
    public void disableDrawDestinationCards() {
        this.drawDestinationCardsEnabled = false;
    }

    @Override
    public void disableClaimRoute() {
        this.claimRoutesEnabled = false;
    }

    @Override
    public void openDestinationDrawer() {

    }

    @Override
    public void openClaimRouteDrawer() {
        //TODO
    }

    @Override
    public void openDrawTrainDrawer() {
        //TODO
    }

    @Override
    public void closeDestinationDrawer() {
        //TODO
    }

    @Override
    public void closeClaimRouteDrawer() {
        //TODO
    }

    @Override
    public void closeDrawTrainDrawer() {
        //TODO
    }

    @Override
    public void setState(IState state) {
        this.state = state;
    }

    @Override
    public void displayMessage_short(String message) {

    }

    @Override
    public void displayMessage_long(String message) {

    }
}
