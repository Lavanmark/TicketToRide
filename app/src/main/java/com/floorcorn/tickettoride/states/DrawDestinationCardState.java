package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

import java.util.logging.Level;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawDestinationCardState extends TurnState {


    @Override
    public void enter(IBoardMapPresenterStateful presenter){
        presenter.openClaimRouteDrawer();
    }

    @Override
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter) {
        super.drawDestinationTickets(presenter);
        try {
            UIFacade.getInstance().drawDestinationCards();
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
        }
        presenter.setState(new PreTurnState());
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenterStateful presenter) {
        super.openDestinationDraw(presenter);
        presenter.openDestinationDrawer();
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenterStateful presenter) {
        super.closeDestinationDraw(presenter);
        presenter.closeDestinationDrawer();
        presenter.setState(new TurnState());
    }
}
