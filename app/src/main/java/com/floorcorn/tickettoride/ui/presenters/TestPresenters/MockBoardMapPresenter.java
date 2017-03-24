package com.floorcorn.tickettoride.ui.presenters.TestPresenters;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/22/2017.
 */

public class MockBoardMapPresenter implements IBoardMapPresenter {

    public boolean drawTrainCardsEnabled;
    public boolean drawDestinationCardsEnabled;
    public boolean claimRoutesEnabled;

    public int numCardsDrawn = 0;

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
    }

    @Override
    public void openDrawTrainDrawer() {
    }

    @Override
    public void closeDestinationDrawer() {
    }

    @Override
    public void closeClaimRouteDrawer() {
    }

    @Override
    public void closeDrawTrainDrawer() {
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

    @Override
    public int[] getDiscardableDestinationCards() throws Exception {
        return new int[0];
    }

    @Override
    public void updateDestinationDrawer() {

    }

    @Override
    public void tryOpenDestinationDrawer() {

    }

    @Override
    public void tryOpenDrawTrainDrawer() {

    }

    @Override
    public void tryOpenClaimRouteDrawer() {

    }

    @Override
    public IBoardmapView getView() {
        return null;
    }

    @Override
    public void setView(IBoardmapView view) {

    }

    @Override
    public Game getGame() {
        return null;
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public IState getState() {
        return null;
    }

    @Override
    public DestinationCard[] getDestCardsToDiscard() {
        return new DestinationCard[0];
    }

    @Override
    public void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard) {

    }
}
