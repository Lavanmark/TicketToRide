package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.ui.views.ILobbyView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Kaylee on 2/10/2017.
 */

public class LobbyPresenter implements IPresenter, Observer {

    private ILobbyView view;

    public LobbyPresenter() {
        view = null;
        UIFacade.getInstance().registerObserver(this);
    }

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
            UIFacade.getInstance().logout();
            view.backToLogin();
        }
    }

    public void joinGame(){
        try {
	        //TODO need to implement a join game dialogue that has the parameters
	        System.out.println("joinin");
            UIFacade.getInstance().joinGame(view.getGameID(), view.getPlayerColor());
            //pop up window, get color, get game id, call ui facade
        }catch(GameActionException gameAction){
            view.displayMessage(gameAction.getMessage());
        }catch(BadUserException badUser){
	        UIFacade.getInstance().logout();
	        view.backToLogin();
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
        return UIFacade.getInstance().getGame(gameID);
    }

	public void setCurrentGame(IGame game) {
		if(game != null)
			UIFacade.getInstance().setCurrentGame(game);
	}

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Set)
	        view.setGameList(UIFacade.getInstance().getGames());
    }
}
