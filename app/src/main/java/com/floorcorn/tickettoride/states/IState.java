package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import java.util.List;

/**
 * Created by Michael on 3/15/2017.
 */

public class IState {

    public IState(){
        System.out.println("Constructing new state");
    }
    public void enter(IBoardMapPresenter presenter){
        System.out.println("Entering");
    }
    public void exit(IBoardMapPresenter presenter){
        System.out.println("Exiting");
    }
    public void setTurn(IBoardMapPresenter presenter, Player me){}
    public TrainCardColor drawFaceUpCard(IBoardMapPresenter presenter, int position){return null;}
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenter presenter){return null;}
    public void claimRoute(IBoardMapPresenter presenter, Route route) throws BadUserException, GameActionException {}
    public void drawDestinationTickets(IBoardMapPresenter presenter){}
    public void discardDestinationTickets(IBoardMapPresenter presenter, DestinationCard[] toDiscard, boolean[] shouldDiscard){}
    public void openTrainDraw(IBoardMapPresenter presenter){}
    public void closeTrainDraw(IBoardMapPresenter presenter){}
    public void openDestinationDraw(IBoardMapPresenter presenter){}
    public void closeDestinationDraw(IBoardMapPresenter presenter){}
    public void openClaimRoute(IBoardMapPresenter presenter){}
    public void closeClaimRoute(IBoardMapPresenter presenter){}

}
