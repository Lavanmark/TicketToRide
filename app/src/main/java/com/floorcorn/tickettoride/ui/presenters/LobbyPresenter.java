package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.ui.views.ILobbyView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public class LobbyPresenter implements IPresenter {

    private ILobbyView view;

    @Override
    public void setView(IView v) {
        view = (ILobbyView) v;
    }

    public void createGame(){ //sends to the uifacade, who requests it . create game
        try {
            UIFacade.getInstance().createGame(view.getNewGameName(), view.getNewGamePlayerNumber(), view.getPlayerColor());
        }catch(GameActionException gameAction){
            view.displayMessage(gameAction.getMessage());
        }catch(BadUserException badUser){
            //TODO: toss them out to the login lobby because this user is bad juju
        }
    }

    public void joinGame(){
        try {
            UIFacade.getInstance().joinGame(view.getGameID(), view.getPlayerColor());
            //pop up window, get color, get game id, call ui facade
        }catch(GameActionException gameAction){
            view.displayMessage(gameAction.getMessage());
        }catch(BadUserException badUser){
            //TODO: toss them out to the login lobby because this user is bad juju
        }
    }

    public Set<IGame> getGameList() {
        return UIFacade.getInstance().getGames();
    }

    public void requestGames() {
        try {
            UIFacade.getInstance().requestGames();
        } catch (Exception e) {
            e.printStackTrace();
            view.displayMessage(e.getMessage());
        }
    }

    public Set<IGame> getGameList(User user){
        return UIFacade.getInstance().getGames(user);
    }

    public IGame getGameInfo(int gameID){ //user clicks on game on the list and it reports back the info of the game. returnt eh game object
        return UIFacade.getInstance().getInstance().getGame(gameID);
    }


}
