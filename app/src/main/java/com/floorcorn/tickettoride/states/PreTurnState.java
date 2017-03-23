package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/15/2017.
 */

public class PreTurnState extends IState {

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        //Disable all turn buttons except draw destination cards drawer.
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
        // if there are discardable cards, open the destination drawer
        try {
            if(presenter.getDiscardableDestinationCards().length > 0){
                //opens drawer
                presenter.openDestinationDrawer();
                presenter.updateDestinationDrawer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void exit(IBoardMapPresenter presenter) {
        super.exit(presenter);
        //close destination drawer
        presenter.closeDestinationDrawer();

    }

    @Override
    public void setTurn(IBoardMapPresenter presenter) {
        super.setTurn(presenter);
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenter presenter, DestinationCard toDiscard) {
        super.discardDestinationTickets(presenter, toDiscard);
        try {
            UIFacade.getInstance().discardDestinationCard(toDiscard);
        } catch (GameActionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void openDestinationDraw(IBoardMapPresenter presenter) {
        super.openDestinationDraw(presenter);
        presenter.openDestinationDrawer();
        presenter.updateDestinationDrawer();
    }

    @Override
    public void closeDestinationDraw(IBoardMapPresenter presenter) {
        super.closeDestinationDraw(presenter);
        presenter.closeDestinationDrawer();
    }
}
