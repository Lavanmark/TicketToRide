package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public interface ILobbyView extends IView {

    //set presenter is the one function

    @Override
    void setPresenter(IPresenter presenter);
    int getGameID();
    Player.PlayerColor getPlayerColor();
    int getNewGamePlayerNumber();
    String getNewGameName();
    void createNewGameDialogue();
    void displayGameList(Set<Game> games);
    void displayMessage(String message);
    void backToLogin();
}
