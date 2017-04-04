package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

/**
 * Created by Michael on 3/20/2017.
 */

public class ClaimRouteState extends TurnState {
    
    @Override
    public void enter(IBoardMapPresenterStateful presenter) {
        
    }
    
    @Override
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) {
        if(route != null) {
            try {

                UIFacade.getInstance().claimRoute(route);
                presenter.displayMessage_short("Claimed route: " + route.getFirstCity().getName() + " to " + route.getSecondCity().getName());
                //presenter.getView().updateMap();
                presenter.setState(new PreTurnState());
            } catch(BadUserException e) {
                e.printStackTrace();
                //TODO logout
            } catch(GameActionException e) {
                e.printStackTrace();
            }
            
        } else
            presenter.displayMessage_short("No routes can be claimed!");
    }

    @Override
    public void closeClaimRoute(IBoardMapPresenterStateful presenter) {
        presenter.setState(new TurnState());
    }

    @Override
    public boolean openWildRouteDialog(){
        return true;
    }
}
