package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class TurnState extends IState {

    @Override
    void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
    }

    @Override
    void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
    }

    @Override
    void setTurn(IBoardmapView presenter) {
        super.setTurn(presenter);
    }

    @Override
    void drawFaceUpCard(IBoardMapPresenter presenter, int position) {
        super.drawFaceUpCard(presenter, position);
    }

    @Override
    void drawTrainCardFromDeck(IBoardMapPresenter presenter) {
        super.drawTrainCardFromDeck(presenter);
    }

    @Override
    void claimRoute(IBoardMapPresenter presenter) {
        super.claimRoute(presenter);
    }

    @Override
    void drawDestinationTickets(IBoardMapPresenter presenter) {
        super.drawDestinationTickets(presenter);
    }

    @Override
    void discardDestinationTickets(IBoardMapPresenter presenter) {
        super.discardDestinationTickets(presenter);
    }

    @Override
    void openTrainDraw(IBoardMapPresenter presenter) {
        super.openTrainDraw(presenter);
    }

    @Override
    void closeTrainDraw(IBoardMapPresenter presenter) {
        super.closeTrainDraw(presenter);
    }

    @Override
    void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
    }

    @Override
    void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
    }

    @Override
    void openClaimRoute(IBoardMapPresenter presenter) {
        super.openClaimRoute(presenter);
    }

    @Override
    void closeClaimRoute(IBoardMapPresenter presenter) {
        super.closeClaimRoute(presenter);
    }
}
