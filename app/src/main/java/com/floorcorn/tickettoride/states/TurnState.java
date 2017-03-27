package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.commands.ClaimRouteCmd;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;
import com.floorcorn.tickettoride.ui.views.drawers.BMDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.ClaimRouteDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.DestinationDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.TrainCardDrawer;

/**
 * Created by Michael on 3/15/2017.
 */

public class TurnState extends IState {

    @Override
    public void enter(IBoardMapPresenterStateful presenter) {
        System.out.println("Entering TurnState");
        //check if any of the drawers are open already
	    BMDrawer isopen = presenter.getOpenDrawer();
        if(isopen != null) {
	        if(isopen instanceof ClaimRouteDrawer)
	        	openClaimRoute(presenter);
	        else if(isopen instanceof DestinationDrawer)
	        	openDestinationDraw(presenter);
	        else if(isopen instanceof TrainCardDrawer)
	        	openTrainDraw(presenter);
        }
    }

    @Override
    public boolean drawFaceUpCard(IBoardMapPresenterStateful presenter, int position) {
        displayNotHappen(presenter);
        return false;
    }

    @Override
    public boolean drawTrainCardFromDeck(IBoardMapPresenterStateful presenter) {
        displayNotHappen(presenter);
        return false;
    }

    @Override
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) {
        displayNotHappen(presenter);
    }

    @Override
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter) {
        displayNotHappen(presenter);
    }

    @Override
    public void openTrainDraw(IBoardMapPresenterStateful presenter) {
        presenter.setState(new DrawTrainCardState());
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenterStateful presenter) {
        presenter.setState(new DrawDestinationCardState());
    }

    @Override
    public void openClaimRoute(IBoardMapPresenterStateful presenter) {
        presenter.setState(new ClaimRouteState());
    }
}
