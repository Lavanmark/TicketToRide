package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class IState {

    public void enter(IBoardMapPresenter presenter){}
    public void exit(IBoardMapPresenter presenter){}
    public void setTurn(IBoardMapPresenter presenter){}
    public TrainCardColor drawFaceUpCard(IBoardMapPresenter presenter, int position){return null;}
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenter presenter){return null;}
    public void claimRoute(IBoardMapPresenter presenter){}
    public void drawDestinationTickets(IBoardMapPresenter presenter){}
    public void discardDestinationTickets(IBoardMapPresenter presenter, DestinationCard index){}
    public void openTrainDraw(IBoardMapPresenter presenter){}
    public void closeTrainDraw(IBoardMapPresenter presenter){}
    public void openDestinationDraw(IBoardMapPresenter presenter){}
    public void closeDestinationDraw(IBoardMapPresenter presenter){}
    public void openClaimRoute(IBoardMapPresenter presenter){}
    public void closeClaimRoute(IBoardMapPresenter presenter){}

}
