package com.floorcorn.tickettoride.ui.presenters.TestPresenters;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.drawers.BMDrawer;

/**
 * Created by Michael on 3/22/2017.
 */

public class MockBoardMapPresenter implements IBoardMapPresenterStateful {

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
    public void openDestinationDrawer() {

    }
    
    @Override
    public BMDrawer getOpenDrawer() {
        return null;
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
    public IBoardmapView getView() {
        return null;
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
    public void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard) {

    }
    
    @Override
    public int getDiscardableCount() {
        return 0;
    }

    @Override
    public void displayCardDrawnDialog(TrainCardColor color) {

    }

}
