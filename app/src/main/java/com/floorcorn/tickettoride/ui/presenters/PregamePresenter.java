package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.views.IPregameView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Joseph Hansen
 */

public class PregamePresenter implements IPresenter, Observer {
    private IPregameView view;
    private IGame game;
    private IUser user;

    private ScheduledExecutorService scheduledTaskExecutor;

    public PregamePresenter() {
        this(null, null);
    }

    public PregamePresenter(IGame g, IUser u) {
        game = g;
        user = u;
        scheduledTaskExecutor = Executors.newSingleThreadScheduledExecutor();
        beginStartGamePoller();
    }

    class CheckGameFilledTask implements Runnable {
        @Override
        public void run() {
            IGame gameFromServer = UIFacade.getInstance().getGame(game.getGameID());
            if (gameFromServer != null) {
                game = gameFromServer;
                updatePlayerList();
                if (game.hasStarted()) {
                    startGame();
                }
            }
        }
    }

    /**
     * This does a leave game operation.
     */
    public void cancelGame() {
        try {
            UIFacade.getInstance().leaveGame(game.getGameID());
        } catch (BadUserException | GameActionException ex) {
            view.displayMessage("Could not leave game");
            // Here, we could send back to login. Put this in PregameActivity as a func and call
            // if that's what we want:
            // startActivity(new Intent(PregameActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        }
        returnToLobby();
    }

    /**
     * // NOTE: I do not think this method, that we put in the design doc, is needed.
     * @return
     */
    public Set<Player> getPlayerList() {
        throw new UnsupportedOperationException();
    }

    /**
     * This returns to the lobby view. It does not leave the game.
     */
    public void returnToLobby() {
        this.view.switchToLobbyActivity();
    }

    /**
     * This checks the status of the game and starts the game if it has filled with players.
     */
    public void beginStartGamePoller() {
        scheduledTaskExecutor.scheduleAtFixedRate(new CheckGameFilledTask(), 0, 5, TimeUnit.SECONDS);
    }

    /**
     * Ends the periodic checking for start game status.
     */
    public void endStartGamePoller() {
        scheduledTaskExecutor.shutdown();
    }

    /**
     * Should be called when number of players in game matches the game's size. Stops the
     * StartGamePoller. Starts the game.
     */
    public void startGame() {
        // For Phase 0, just show the Boardmap with message: Game Started
        endStartGamePoller();
        view.startGame();
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
            updatePlayerList();
        } else if (arg instanceof IUser) {
            user = (IUser) arg;
        }
    }

    /**
     * Updates the displayed player list. (Sends the player list to the view and calls display
     * again.)
     */
    private void updatePlayerList() {
        this.view.displayPlayerList(this.game.getPlayerList());
    }
}
