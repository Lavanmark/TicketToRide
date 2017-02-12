package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.model.Player;

import java.util.Set;

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
    public void displayPlayerList(Set<Player> players);

    /**
     * Begins this game.
     */
    public void startGame();
}
