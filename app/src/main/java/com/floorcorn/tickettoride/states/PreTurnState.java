package com.floorcorn.tickettoride.states;

import android.util.Log;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 3/15/2017.
 */

public class PreTurnState extends IState {

    private boolean myTurn;

    @Override
    public void enter(IBoardMapPresenter presenter) {
        super.enter(presenter);
        myTurn = false;
        System.out.println("Entering PreTurnState");
        //Disable all turn buttons except draw destination cards drawer.
        presenter.disableClaimRoute();
        presenter.disableDrawTrainCards();
        // if there are discardable cards, open the destination drawer
        try {
            if(presenter.getDiscardableDestinationCards().length > 0){
                //opens drawer
                presenter.openDestinationDrawer();
                //TODO: LOCK DRAWER OPEN
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
    public void setTurn(IBoardMapPresenter presenter, Player me) {
        System.out.println("In PreTurn setTurn");
        super.setTurn(presenter, me);
        //if it is not my turn, return
        if (!me.isTurn())
            return;
        if (myTurn)
            return;
        // if there are discardable cards, open the destination drawer
        try {
            // case it is my turn, and I still need to discard
            if (presenter.getDiscardableDestinationCards() != null) {
                if (presenter.getDiscardableDestinationCards().length > 0) {
                    myTurn = true;
                    presenter.displayMessage_short("Please select your destination cards to continue");
                    presenter.openDestinationDrawer();
                    presenter.updateDestinationDrawer();
                }
            }
            // case it is my turn but I already discarded
            else{
                presenter.displayMessage_short("Begin your turn");
                presenter.setState(new TurnState());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void discardDestinationTickets(IBoardMapPresenter presenter, DestinationCard[] destCardsToDiscard, boolean[] shouldDiscard) {
        super.discardDestinationTickets(presenter, destCardsToDiscard, shouldDiscard);
        if(destCardsToDiscard == null)
            return;
        List<DestinationCard> toDiscard = new ArrayList<>();
		for(int i = 0; i < shouldDiscard.length; i++)
			if(shouldDiscard[i])
				toDiscard.add(destCardsToDiscard[i]);
		presenter.setDestCardsToDiscard(new DestinationCard[3]);
		try {
			UIFacade.getInstance().discardDestinationCards(toDiscard);
            Log.d("INFO","Discarding Destination cards: "+String.valueOf(myTurn));
            if (myTurn){
                presenter.displayMessage_short("Begin your turn");
                presenter.setState(new TurnState());

            }
		} catch (GameActionException e) {
			e.printStackTrace();
		} catch(BadUserException e) {
			e.printStackTrace();
			presenter.getView().backToLogin();
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
