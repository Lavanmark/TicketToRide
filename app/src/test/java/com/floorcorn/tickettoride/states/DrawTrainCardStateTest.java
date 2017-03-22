package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.TestPresenters.MockBoardMapPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Michael on 3/22/2017.
 */
public class DrawTrainCardStateTest {

    private MockBoardMapPresenter presenter;
    private IState state;

    @Before
    public void setUp() throws Exception {
        this.presenter = new MockBoardMapPresenter();
        this.state = new DrawTrainCardState();
        presenter.setState(state);
    }

    @Test
    public void testDrawFaceUpCard(){
        //Initialize fake face up cards
       /* TrainCard[] mockCards = new TrainCard[5];
        for(int i = 0; i< 4; i++){
            TrainCard card = new TrainCard(TrainCardColor.BLUE);
            mockCards[i] = card;
        }
        //Insert wild at end
        mockCards[4] = new TrainCard(TrainCardColor.WILD);
        try {
            UIFacade.getInstance().test_setFaceUpCard(mockCards);
        } catch (GameActionException e){
            fail("Exception thrown: " + e.getMessage());
        }
        */

        /*assertEquals(this.state, presenter.state);
        assertTrue(presenter.state instanceof DrawTrainCardState);
        assertTrue(presenter.numCardsDrawn == 0);

        state.drawFaceUpCard(presenter, 0);

        assertTrue(presenter.numCardsDrawn == 1);
        assertTrue(presenter.state instanceof TurnState);

        state.drawFaceUpCard(presenter, 4);

        assertTrue(presenter.numCardsDrawn == 1);
        assertTrue(presenter.state instanceof TurnState);

        state.drawFaceUpCard(presenter, 2);

        assertTrue(presenter.numCardsDrawn == 2);
        assertTrue(presenter.state instanceof PreTurnState);*/

    }
}