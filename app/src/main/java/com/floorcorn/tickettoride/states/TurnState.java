package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/15/2017.
 */

public class TurnState extends IState {

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        System.out.println("Entering TurnState");
        presenter.enableDrawTrainCards();
        presenter.enableClaimRoute();
        presenter.enableDrawDestinationCards();
        presenter.lockDrawerClosed();
    }

    @Override
    public void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
        //TODO: destinationCardState should override this method and not disable the Draw Destination button if new tickets drawn
        presenter.disableDrawDestinationCards();
        presenter.lockDrawerClosed();
    }

    @Override
    public void setTurn(IBoardMapPresenter presenter, Player me) {
        super.setTurn(presenter, me);
    }

    @Override
    public TrainCardColor drawFaceUpCard(IBoardMapPresenter presenter, int position) {
        return super.drawFaceUpCard(presenter, position);
    }

    @Override
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenter presenter) {
        return super.drawTrainCardFromDeck(presenter);
    }

    @Override
    public void claimRoute(IBoardMapPresenter presenter, Route route) throws BadUserException, GameActionException {
        super.claimRoute(presenter, route);
    }

    @Override
    public void drawDestinationTickets(IBoardMapPresenter presenter) {
        super.drawDestinationTickets(presenter);
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenter presenter, DestinationCard[] toDiscard, boolean[] shouldDiscard) {
        super.discardDestinationTickets(presenter, toDiscard, shouldDiscard);
    }

    @Override
    public void openTrainDraw(IBoardMapPresenter presenter) {
        super.openTrainDraw(presenter);
        presenter.setState(new DrawTrainCardState());
    }

    @Override
    public void closeTrainDraw(IBoardMapPresenter presenter) {
        super.closeTrainDraw(presenter);
    }

    @Override
    public void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
        presenter.openDestinationDrawer();
        presenter.setState(new DrawDestinationCardState());
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
        presenter.closeDestinationDrawer();
    }

    @Override
    public void openClaimRoute(IBoardMapPresenter presenter) {
        super.openClaimRoute(presenter);
        presenter.openClaimRouteDrawer();
        presenter.setState(new ClaimRouteState());
    }

    @Override
    public void closeClaimRoute(IBoardMapPresenter presenter) {
        super.closeClaimRoute(presenter);
    }
}
