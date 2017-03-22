package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/15/2017.
 */

public class PreTurnState extends IState {

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        //Disable all turn buttons except draw destination cards drawer.
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
        //TODO: should we open the destination drawer if the user has discardable cards?
    }

    @Override
    public void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
        //TODO: close Destination Drawer if it was open
        presenter.enableClaimRoute();
        presenter.enableDrawTrainCards();

    }

    @Override
    public void setTurn(IBoardMapPresenter presenter) {
        super.setTurn(presenter);
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenter presenter) {
        super.discardDestinationTickets(presenter);

    }

    @Override
    public void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
        //TODO: open the drawer
        //TODO: disable the draw from deck
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
        //TODO: close the drawer
    }
}
