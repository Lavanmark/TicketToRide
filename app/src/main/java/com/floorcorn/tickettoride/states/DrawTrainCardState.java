package com.floorcorn.tickettoride.states;

import android.os.Handler;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

import java.util.logging.Level;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawTrainCardState extends TurnState {

    boolean hasDrawn = false;
    boolean paused = false;
	
	@Override
	public void enter(IBoardMapPresenterStateful presenter) {
		
	}
	
	@Override
	public void exit(IBoardMapPresenterStateful presenter) {
		//TODO don't let it happen...
		//System.err.print("left draw traincard when only one card was drawn");
	}
    
    @Override
    public boolean drawFaceUpCard(IBoardMapPresenterStateful presenter, int position) {
        TrainCardColor toDraw = UIFacade.getInstance().getFaceUpCards()[position].getColor();
        TrainCardColor drawn = null;
        if(paused)
            return true;
        //Attempt to draw wild as second card. Error.
	    if(toDraw == null)
	    	return false;
	    
        if(toDraw == TrainCardColor.WILD) {
		    if(hasDrawn) {
			    String message = "You cannot draw that card";
			    presenter.displayMessage_long(message);
			    return false;
		    }
        }
        try {
            drawn = UIFacade.getInstance().drawTrainCard(position, toDraw != TrainCardColor.WILD && !hasDrawn);
            paused = true;
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
            return false;
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
            return false;
        }
        //User drew a wild as first card or drew their second card;
        if(toDraw == TrainCardColor.WILD || hasDrawn){
            presenter.setState(new PreTurnState());
        } else
            hasDrawn = true;
        if(drawn != null) {
            presenter.displayCardDrawnDialog(drawn);
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                paused = false;
            }
        }, 1500);
        return true;
    }

    @Override
    public boolean drawTrainCardFromDeck(IBoardMapPresenterStateful presenter) {
        TrainCardColor drawn = null;
        try {
            drawn = UIFacade.getInstance().drawTrainCardFromDeck(!hasDrawn);
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
            return false;
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
            return false;
        }
        if(hasDrawn) {
            presenter.setState(new PreTurnState());
        } else
            hasDrawn = true;
        if(drawn != null) {
            presenter.displayCardDrawnDialog(drawn);
        }
        return true;
    }

    @Override
    public void openTrainDraw(IBoardMapPresenterStateful presenter) {
	    if(!hasDrawn)
	    	super.openTrainDraw(presenter);
    }
    
	@Override
	public void openDestinationDraw(IBoardMapPresenterStateful presenter) {
		if(!hasDrawn)
			super.openDestinationDraw(presenter);
	}
	
	@Override
	public void openClaimRoute(IBoardMapPresenterStateful presenter) {
		if(!hasDrawn)
			super.openClaimRoute(presenter);
	}
	
    @Override
    public void closeTrainDraw(IBoardMapPresenterStateful presenter) {
        if(!hasDrawn)
            presenter.setState(new TurnState());
    }
	
	@Override
	public void claimRoute(IBoardMapPresenterStateful presenter, Route route) {
		if(hasDrawn)
			presenter.displayMessage_short("You already drew a card! Please draw another to end your turn.");
		else
			super.claimRoute(presenter, route);
	}
	
	@Override
	public void drawDestinationTickets(IBoardMapPresenterStateful presenter) {
		if(hasDrawn)
			presenter.displayMessage_short("You already drew a card! Please draw another to end your turn.");
		else
			super.drawDestinationTickets(presenter);
	}
}
