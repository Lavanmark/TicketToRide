package com.floorcorn.tickettoride.states;

import com.floorcorn.tickettoride.ui.presenters.TestPresenters.MockBoardMapPresenter;

import org.junit.Before;
import org.junit.Test;

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

    }
}