package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.Poller;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.views.IPregameView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Joseph Hansen
 */

public class PregamePresenter implements IPresenter, Observer {
    private IPregameView view;
    private Game game;
    private User user;

    public PregamePresenter() {
        game = UIFacade.getInstance().getCurrentGame();
        user = UIFacade.getInstance().getUser();
        UIFacade.getInstance().registerObserver(this);
    }

    /**
     * This does a leave game operation.
     */
    public void cancelGame() {
        try {
            if(UIFacade.getInstance().leaveGame(game.getGameID()))
	            returnToLobby();
        } catch (BadUserException e) {
            view.backToLogin();
        } catch(GameActionException e) {
	        e.printStackTrace();
	        view.displayMessage("Could not leave game");
        }
    }

    /**
     * // NOTE: I do not think this method, that we put in the design doc, is needed.
     * @return a List of Player objects
     */
    public List<Player> getPlayerList() {
        return game.getPlayerList();
    }

	public int getGameSize() {
		return game.getGameSize();
	}

    public void requestPlayerList() {
		UIFacade.getInstance().pollPlayerList(view);
    }
	public void stopPolling() {
		UIFacade.getInstance().stopPolling();
	}

    /**
     * This returns to the lobby view. It does not leave the game.
     */
    public void returnToLobby() {
        this.view.switchToLobbyActivity();
    }

    /**
     * Should be called when number of players in game matches the game's size. Stops the
     * StartGamePoller. Starts the game.
     */
    public void startGame() {
        view.startGame();
    }

	public boolean canCancel() {
		return game.getPlayer(user).isConductor();
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
	    System.out.println("update");
        if (arg instanceof Game) {
            game = (Game) arg;
	        if(!game.isPlayer(user.getUserID()))
		        view.switchToLobbyActivity();
            if (game.hasStarted())
                startGame();
            else
                updatePlayerList();
        } else if (arg instanceof User) {
            user = (User) arg;
        }
    }

    /**
     * Unregisters this presenter from observing the model and stuff.
     */
	public void unregister() {
		UIFacade.getInstance().unregisterObserver(this);
	}

    /**
     * Updates the displayed player list. (Sends the player list to the view and calls display
     * again.)
     */
    private void updatePlayerList() {
        this.view.displayPlayerList(this.game.getPlayerList());
    }
}
