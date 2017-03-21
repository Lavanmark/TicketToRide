package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class IState {

    public void enter(IBoardMapPresenter presenter){}
    public void exit(IBoardMapPresenter presenter){}
    public void setTurn(IBoardmapView presenter){}
    public void drawFaceUpCard(IBoardMapPresenter presenter, int position){}
    public void drawTrainCardFromDeck(IBoardMapPresenter presenter){}
    public void claimRoute(IBoardMapPresenter presenter){}
    public void drawDestinationTickets(IBoardMapPresenter presenter){}
    public void discardDestinationTickets(IBoardMapPresenter presenter){}
    public void openTrainDraw(IBoardMapPresenter presenter){}
    public void closeTrainDraw(IBoardMapPresenter presenter){}
    public void openDestinationDraw(IBoardMapPresenter presenter){}
    public void closeDestinationDraw(IBoardMapPresenter presenter){}
    public void openClaimRoute(IBoardMapPresenter presenter){}
    public void closeClaimRoute(IBoardMapPresenter presenter){}

}
