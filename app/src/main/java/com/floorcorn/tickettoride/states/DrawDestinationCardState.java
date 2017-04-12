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
    public void enter(IBoardMapPresenterStateful presenter) {
        
    }
    
    @Override
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter) {
        try {
            UIFacade.getInstance().drawDestinationCards();
        } catch (GameActionException e) {
            presenter.displayMessage_short("No more destination tickets! Do something else for your turn.");
            Corn.log(Level.SEVERE, e.getMessage());
            return;
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
            return;
        }
        presenter.setState(new PreTurnState());
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenterStateful presenter) {
        presenter.setState(new TurnState());
    }
}
