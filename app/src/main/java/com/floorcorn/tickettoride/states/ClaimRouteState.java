package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

/**
 * Created by Michael on 3/20/2017.
 */

public class ClaimRouteState extends TurnState {

    @Override
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) throws BadUserException, GameActionException {
        super.claimRoute(presenter, route);
        if(route != null) {
            UIFacade.getInstance().claimRoute(route);
            presenter.displayMessage_short("Claimed route: " + route.getFirstCity().getName() + " to " + route.getSecondCity().getName());
            presenter.setState(new PreTurnState());
        } else
            presenter.displayMessage_short("No routes can be claimed!");
    }

    @Override
    public void openClaimRoute(IBoardMapPresenterStateful presenter) {
        super.openClaimRoute(presenter);
        presenter.openClaimRouteDrawer();
    }

    @Override
    public void closeClaimRoute(IBoardMapPresenterStateful presenter) {
        super.closeClaimRoute(presenter);
        presenter.closeClaimRouteDrawer();
        presenter.setState(new TurnState());
    }
}
