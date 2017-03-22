package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

import java.util.logging.Level;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawTrainCardState extends TurnState {

    int cardsDrawn = 0;
    @Override
    public TrainCardColor drawFaceUpCard(IBoardMapPresenter presenter, int position) {
        super.drawFaceUpCard(presenter, position);
        TrainCardColor color = null;

        TrainCardColor toDraw = UIFacade.getInstance().getFaceUpCards()[position].getColor();
        if(toDraw.equals(TrainCardColor.WILD) && cardsDrawn > 0) {
            String message = "You cannot draw that card";
            presenter.displayMessage_long(message);
            return null;
        }
        try {
            color = UIFacade.getInstance().drawTrainCard(position);
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        }
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            presenter.displayMessage_short(toDisplay);
        }
        //User drew a wild or their second card;
        if(toDraw.equals(TrainCardColor.WILD) || cardsDrawn == 2){
            super.exit(presenter);
            presenter.setState(new PreTurnState());
            presenter.closeDrawTrainDrawer();
        }
        return color;
    }

    @Override
    public TrainCardColor drawTrainCardFromDeck(IBoardMapPresenter presenter) {
        super.drawTrainCardFromDeck(presenter);
        TrainCardColor color = null;
        try {
            color = UIFacade.getInstance().drawTrainCardFromDeck();
        } catch (GameActionException e){
            Corn.log(Level.SEVERE, e.getMessage());
        }
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            presenter.displayMessage_short(toDisplay);
        }
        cardsDrawn++;
        if(cardsDrawn == 2)
        {
            super.exit(presenter);
            presenter.setState(new PreTurnState());
            presenter.closeDrawTrainDrawer();
        }
        return color;
    }

    @Override
    public void openTrainDraw(IBoardMapPresenter presenter) {
        super.openTrainDraw(presenter);
    }

    @Override
    public void closeTrainDraw(IBoardMapPresenter presenter) {
        super.closeTrainDraw(presenter);
        presenter.setState(new TurnState());
        super.enter(presenter);
    }
}
