package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Michael on 3/20/2017.
 */

public class DrawTrainCardState extends TurnState {

    @Override
    public void drawFaceUpCard(IBoardMapPresenter presenter, int position) {
        super.drawFaceUpCard(presenter, position);
    }

    @Override
    public void drawTrainCardFromDeck(IBoardMapPresenter presenter) {
        super.drawTrainCardFromDeck(presenter);
    }

    @Override
    public void openTrainDraw(IBoardMapPresenter presenter) {
        super.openTrainDraw(presenter);
    }

    @Override
    public void closeTrainDraw(IBoardMapPresenter presenter) {
        super.closeTrainDraw(presenter);
    }
}
