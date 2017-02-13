package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.model.Player;

import java.util.ArrayList;

/**
 * @author Joseph Hansen
 */

public interface IPregameView extends IView {

    /**
     *
     */
    public void createGameWaitingDialog();

    /**
     * Displays this game's players in the view.
     * @param players A Set of Player objects representing players in current game
     */
    public void displayPlayerList(ArrayList<Player> players);

    /**
     * Begins this game.
     */
    public void startGame();

    /**
     * Switches to the Lobby view. It does not leave the game (the game in the Pregame View).
     */
    public void switchToLobbyActivity();
}
