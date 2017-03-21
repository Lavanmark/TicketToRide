package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class PreTurnState extends IState {

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
    void discardDestinationTickets(IBoardMapPresenter presenter) {
        super.discardDestinationTickets(presenter);
    }

    @Override
    void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
    }

    @Override
    void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
    }
}
