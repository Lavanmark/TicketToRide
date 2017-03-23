package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

import java.util.logging.Level;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawDestinationCardState extends TurnState {

    @Override
    public void drawDestinationTickets(IBoardMapPresenter presenter) {
        super.drawDestinationTickets(presenter);
        try {
            UIFacade.getInstance().drawDestinationCards();
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        }
        presenter.setState(new PreTurnState());
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
        presenter.openDestinationDrawer();
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
        presenter.closeDestinationDrawer();
        presenter.setState(new TurnState());
    }
}
