package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

/**
 * Created by Michael on 3/15/2017.
 */

public class IState {

    public IState(){
        System.out.println("Constructing new state");
    }
    public void enter(IBoardMapPresenterStateful presenter){
        System.out.println("Entering");
    }
    public void exit(IBoardMapPresenterStateful presenter){
        System.out.println("Exiting");
    }
    public void setTurn(IBoardMapPresenterStateful presenter, Player me) {}
    public boolean drawFaceUpCard(IBoardMapPresenterStateful presenter, int position) {
        displayNotTurn(presenter);
        return false;
    }
    public boolean drawTrainCardFromDeck(IBoardMapPresenterStateful presenter) {
        displayNotTurn(presenter);
        return false;
    }
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) {
        displayNotTurn(presenter);
    }
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter){
        displayNotTurn(presenter);
    }
    public void discardDestinationTickets(IBoardMapPresenterStateful presenter, DestinationCard[] toDiscard, boolean[] shouldDiscard) {
        displayNotHappen(presenter);
    }
    public void openTrainDraw(IBoardMapPresenterStateful presenter) {
        
    }
    public void closeTrainDraw(IBoardMapPresenterStateful presenter) {
        
    }
    public void openDestinationDraw(IBoardMapPresenterStateful presenter) {
        
    }
    public void closeDestinationDraw(IBoardMapPresenterStateful presenter) {
        
    }
    public void openClaimRoute(IBoardMapPresenterStateful presenter) {
        
    }
    public void closeClaimRoute(IBoardMapPresenterStateful presenter) {
        
    }
    
    protected void displayNotTurn(IBoardMapPresenterStateful presenter) {
        presenter.displayMessage_short("Not your turn!");
    }
    
    protected void displayNotHappen(IBoardMapPresenterStateful presenter) {
        presenter.displayMessage_short("That shouldn't happen...");
    }

    public boolean openWildRouteDialog() {
        return false;
    }
}
