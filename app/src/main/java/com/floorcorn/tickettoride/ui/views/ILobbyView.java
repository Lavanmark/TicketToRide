package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;

import java.util.List;
import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public interface ILobbyView extends IView {
    void setGameList(Set<IGame> gameList);
    int getGameID();
    Player.PlayerColor getPlayerColor();
    int getNewGamePlayerNumber();
    String getNewGameName();
    void displayMessage(String message);
    void backToLogin();
    void resumeGame(IGame game);
}
