package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.views.IPregameView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * @author Joseph Hansen
 */

public class PregamePresenter implements IPresenter, Observer {
    private IPregameView view;

    /**
     *
     */
    public void cancelGame() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @return
     */
    public Set<Player> getPlayerList() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     */
    public void returnToLobby() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     */
    public void startGame() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param view the view corresponding to this presenter.
     *
     */
    @Override
    public void setView(IView view) {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        // TODO
        throw new UnsupportedOperationException();
    }
}
