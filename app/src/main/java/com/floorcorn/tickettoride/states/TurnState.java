package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/15/2017.
 */

public class TurnState extends IState {

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        presenter.enableDrawTrainCards();
        presenter.enableClaimRoute();
        presenter.enableDrawDestinationCards();
    }

    @Override
    public void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
    }

    @Override
    public void setTurn(IBoardMapPresenter presenter) {
        super.setTurn(presenter);
    }

    @Override
    public void drawFaceUpCard(IBoardMapPresenter presenter, int position) {
        super.drawFaceUpCard(presenter, position);
    }

    @Override
    public void drawTrainCardFromDeck(IBoardMapPresenter presenter) {
        super.drawTrainCardFromDeck(presenter);
    }

    @Override
    public void claimRoute(IBoardMapPresenter presenter) {
        super.claimRoute(presenter);
    }

    @Override
    public void drawDestinationTickets(IBoardMapPresenter presenter) {
        super.drawDestinationTickets(presenter);
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenter presenter) {
        super.discardDestinationTickets(presenter);
    }

    @Override
    public void openTrainDraw(IBoardMapPresenter presenter) {
        super.openTrainDraw(presenter);
    }

    @Override
    public void closeTrainDraw(IBoardMapPresenter presenter) {
        super.closeTrainDraw(presenter);
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
    }

    @Override
    public void openClaimRoute(IBoardMapPresenter presenter) {
        super.openClaimRoute(presenter);
    }

    @Override
    public void closeClaimRoute(IBoardMapPresenter presenter) {
        super.closeClaimRoute(presenter);
    }
}
