package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
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
    private IGame game;
    private IUser user;

    /**
     * This does a leave game operation.
     */
    public void cancelGame() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * // NOTE: I do not think this method, that we put in the design doc, is needed.
     * @return
     */
    public Set<Player> getPlayerList() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * This returns to the lobby view. It does not leave the game.
     */
    public void returnToLobby() {
        this.view.switchToLobbyActivity();
    }

    /**
     *
     */
    public void startGame() {
        // TODO
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the view. If the view parameter is not an IPregameView, this will throw an
     * IllegalArgumentException.
     * @param view the view corresponding to this presenter.
     */
    @Override
    public void setView(IView view) {
        if (view instanceof IPregameView) {
            this.view = (IPregameView) view;
        } else {
            throw new IllegalArgumentException("View arg was not an IPregameView");
        }
    }

    /**
     * o has called notifyObservers which resulted in this update function being called. It will
     * update this PregamePresenter based on what was changed in the ClientModel.
     * @param o An Object that extends Observable
     * @param arg Is an Object, this is what was changed
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof IGame) {
            game = (IGame) arg;
            this.view.displayPlayerList(game.getPlayerList());
        } else if (arg instanceof IUser) {
            user = (IUser) arg;
        }
    }
}
