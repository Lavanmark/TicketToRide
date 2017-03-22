package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 3/15/2017.
 */

public class PreTurnState extends IState {

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        //TODO: Disable claim route and draw train cards

    }

    @Override
    public void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
        //TODO: close Destination Drawer

    }

    @Override
    public void setTurn(IBoardmapView presenter) {
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
