package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;

import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public interface ILobbyView extends IView {
    void setGameList(Set<GameInfo> gameList);
    int getGameID();
    PlayerColor getPlayerColor();
    int getNewGamePlayerNumber();
    String getNewGameName();
    void displayMessage(String message);
    void resumeGame(GameInfo game);
    void startGameView();
}
