package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

/**
 * Created by Michael on 3/15/2017.
 */

public class TurnState extends IState {

    @Override
    public void enter(IBoardMapPresenterStateful presenter) {
        super.enter(presenter);
        System.out.println("Entering TurnState");
        presenter.enableDrawTrainCards();
        presenter.enableClaimRoute();
        presenter.enableDrawDestinationCards();
        presenter.lockDrawerClosed();
    }

    @Override
    public void exit(IBoardMapPresenterStateful presenter) {
        super.exit(presenter);
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
        //TODO: destinationCardState should override this method and not disable the Draw Destination button if new tickets drawn
        presenter.disableDrawDestinationCards();
        presenter.lockDrawerClosed();
    }

    @Override
    public void setTurn(IBoardMapPresenterStateful presenter, Player me) {
        super.setTurn(presenter, me);
    }

    @Override
    public TrainCardColor drawFaceUpCard(IBoardMapPresenterStateful presenter, int position) {
        return super.drawFaceUpCard(presenter, position);
    }

    @Override
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenterStateful presenter) {
        return super.drawTrainCardFromDeck(presenter);
    }

    @Override
    public void claimRoute(IBoardMapPresenterStateful presenter, Route route) throws BadUserException, GameActionException {
        super.claimRoute(presenter, route);
    }

    @Override
    public void drawDestinationTickets(IBoardMapPresenterStateful presenter) {
        super.drawDestinationTickets(presenter);
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenterStateful presenter, DestinationCard[] toDiscard, boolean[] shouldDiscard) {
        super.discardDestinationTickets(presenter, toDiscard, shouldDiscard);
    }

    @Override
    public void openTrainDraw(IBoardMapPresenterStateful presenter) {
        super.openTrainDraw(presenter);
        presenter.setState(new DrawTrainCardState());

    }

    @Override
    public void closeTrainDraw(IBoardMapPresenterStateful presenter) {
        super.closeTrainDraw(presenter);
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenterStateful presenter) {
        super.openDestinationDraw(presenter);
        presenter.openDestinationDrawer();
        presenter.setState(new DrawDestinationCardState());
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenterStateful presenter) {
        super.closeDestinationDraw(presenter);
        presenter.closeDestinationDrawer();
    }

    @Override
    public void openClaimRoute(IBoardMapPresenterStateful presenter) {
        super.openClaimRoute(presenter);
        presenter.openClaimRouteDrawer();
        presenter.setState(new ClaimRouteState());
    }

    @Override
    public void closeClaimRoute(IBoardMapPresenterStateful presenter) {
        super.closeClaimRoute(presenter);
    }
}
