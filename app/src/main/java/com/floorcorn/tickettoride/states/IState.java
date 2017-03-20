package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class IState {

    void enter(IBoardMapPresenter presenter){}
    void exit(IBoardMapPresenter presenter){}
    void setTurn(IBoardmapView presenter){}
    void drawFaceUpCard(IBoardMapPresenter presenter, int position){}
    void drawTrainCardFromDeck(IBoardMapPresenter presenter){}
    void claimRoute(IBoardMapPresenter presenter){}
    void drawDestinationTickets(IBoardMapPresenter presenter){}
    void discardDestinationTickets(IBoardMapPresenter presenter){}
    void openTrainDraw(IBoardMapPresenter presenter){}
    void closeTrainDraw(IBoardMapPresenter presenter){}
    void openDestinationDraw(IBoardMapPresenter presenter){}
    void closeDestinationDraw(IBoardMapPresenter presenter){}
    void openClaimRoute(IBoardMapPresenter presenter){}
    void closeClaimRoute(IBoardMapPresenter presenter){}

}
