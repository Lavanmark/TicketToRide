package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.model.Player;

import java.util.ArrayList;

/**
 * @author Joseph Hansen
 */

public interface IPregameView extends IView {
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

    /**
     * Switches to the Boardmap view. This happens when the game is started and we don't need
     * Pregame view anymore.
     */
    public void switchToBoardmapActivity();

    /**
     * Displays a message in a Toast.
     * @param message String to display
     */
    public void displayMessage(String message);

    public void backToLogin();
}
