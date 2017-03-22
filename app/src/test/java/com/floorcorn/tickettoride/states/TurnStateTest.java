package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.presenters.TestPresenters.MockBoardMapPresenter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by Michael on 3/22/2017.
 */
public class TurnStateTest {

    private MockBoardMapPresenter presenter;
    private IState state;

    @Before
    public void setUp() throws Exception {
        this.presenter = new MockBoardMapPresenter();
        this.state = new TurnState();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testEnter() throws Exception {
        state.enter(presenter);
        assertTrue(presenter.drawTrainCardsEnabled);
        assertTrue(presenter.drawDestinationCardsEnabled);
        assertTrue(presenter.claimRoutesEnabled);
    }

    @Test
    public void testExit() throws Exception{
        state.exit(presenter);
        assertFalse(presenter.drawTrainCardsEnabled);
        assertFalse(presenter.drawDestinationCardsEnabled);
        assertFalse(presenter.claimRoutesEnabled);
    }
}