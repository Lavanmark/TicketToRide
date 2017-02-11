package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public class LobbyPresenter implements IPresenter {

    //private LobbyView view;

    @Override
    public void setView(IView v) {
      //  view = (LobbyView) v;
    }

    public void createGame(){
       // String color = view.getPlayerColor();
       // int numPlayers = view.getNewGamePlayerNumber();
       // String gameName = view.getNewGameName();

        //color, num players, game name
        //asks the view, the view asks the activity
        //sends to the uifacade, who requests it . create game
    }

    public void joinGame(){
        //what does this do?
    }

    public Set<Game> getGameList(){
        return null;
    } // where am i supposed to get the gamelist from

    public Set<Game> getGameList(User user){ //where am i supposed to get the gamelist from
        Set<Game> returnSet = null;
        //for loop, pick out the games that have the user in it

        return returnSet;
    }

    public Game getGameInfo(int gameID){
        return null;
    } //user clicks on game on the list and it reports back the info of the game. returnt eh game object


}
