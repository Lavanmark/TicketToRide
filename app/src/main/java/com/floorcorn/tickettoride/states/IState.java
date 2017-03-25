package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import java.util.List;

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
    public void setTurn(IBoardMapPresenterStateful presenter, Player me){}
    public TrainCardColor drawFaceUpCard(IBoardMapPresenterStateful presenter, int position){return null;}
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenterStateful presenter){return null;}
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) throws BadUserException, GameActionException {}
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter){}
    public void discardDestinationTickets(IBoardMapPresenterStateful presenter, DestinationCard[] toDiscard, boolean[] shouldDiscard){}
    public void openTrainDraw(IBoardMapPresenterStateful presenter){}
    public void closeTrainDraw(IBoardMapPresenterStateful presenter){}
    public void openDestinationDraw(IBoardMapPresenterStateful presenter){}
    public void closeDestinationDraw(IBoardMapPresenterStateful presenter){}
    public void openClaimRoute(IBoardMapPresenterStateful presenter){}
    public void closeClaimRoute(IBoardMapPresenterStateful presenter){}

}
