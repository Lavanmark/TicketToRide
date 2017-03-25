package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenterStateful;

import java.util.logging.Level;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawTrainCardState extends TurnState {

    boolean hasDrawn = false;
    @Override
    public TrainCardColor drawFaceUpCard(IBoardMapPresenterStateful presenter, int position) {
        super.drawFaceUpCard(presenter, position);
        TrainCardColor color = null;

        TrainCardColor toDraw = UIFacade.getInstance().getFaceUpCards()[position].getColor();
        //Attempt to draw wild as second card. Error.
        if(toDraw.equals(TrainCardColor.WILD) && hasDrawn) {
            String message = "You cannot draw that card";
            presenter.displayMessage_long(message);
            return null;
        }
        try {
            UIFacade.getInstance().drawTrainCard(position);
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
        }
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            presenter.displayMessage_short(toDisplay);
        }
        //User drew a wild as first card or drew their second card;
        if(toDraw.equals(TrainCardColor.WILD) || hasDrawn){
            presenter.setState(new PreTurnState());
            presenter.closeDrawTrainDrawer();
        }
        else
            hasDrawn = true;
        return color;
    }

    @Override
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenterStateful presenter) {
        super.drawTrainCardFromDeck(presenter);
        TrainCardColor color = null;
        try {
            UIFacade.getInstance().drawTrainCardFromDeck();
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        } catch(BadUserException e) {
            e.printStackTrace();
            //TODO logout
        }
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            presenter.displayMessage_short(toDisplay);
        }
        if(hasDrawn)
        {
            presenter.setState(new PreTurnState());
            presenter.closeDrawTrainDrawer();
        }
        else
            hasDrawn = true;
        return color;
    }

    @Override
    public void openTrainDraw(IBoardMapPresenterStateful presenter) {
        super.openTrainDraw(presenter);
    }

    @Override
    public void closeTrainDraw(IBoardMapPresenterStateful presenter) {
        super.closeTrainDraw(presenter);
        presenter.setState(new TurnState());
    }
}
