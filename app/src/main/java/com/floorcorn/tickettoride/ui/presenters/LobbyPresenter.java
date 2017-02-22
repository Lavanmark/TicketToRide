package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
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
            UIFacade.getInstance().joinGame(view.getGameID(), view.getPlayerColor());
            //pop up window, get color, get game id, call ui facade
        }catch(GameActionException gameAction){
            view.displayMessage(gameAction.getMessage());
        }catch(BadUserException badUser){
	        UIFacade.getInstance().logout();
	        view.backToLogin();
        }
    }

    public Set<GameInfo> getGameList() {
        return UIFacade.getInstance().getGames();
    }

    public void requestGames() {
        try {
            UIFacade.getInstance().requestGames();
        } catch (BadUserException e) {
            view.backToLogin();
        }
    }

    public Set<GameInfo> getGameList(User user){
        return UIFacade.getInstance().getGames(user);
    }

    public GameInfo getGameInfo(int gameID){ //user clicks on game on the list and it reports back the info of the game. returnt eh game object
        return UIFacade.getInstance().getGameInfo(gameID);
    }

	public void setCurrentGame(GameInfo game) {
		//if(game != null)
			//UIFacade.getInstance().setCurrentGame(game);
		//TODO fix to be actual game stuff
	}

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Set)
	        view.setGameList(UIFacade.getInstance().getGames());
	    if(UIFacade.getInstance().getUser() == null) {
		    unregister();
		    view.backToLogin();
	    }
    }
    public void unregister() {
        UIFacade.getInstance().unregisterObserver(this);
    }
	public void register() {
		UIFacade.getInstance().registerObserver(this);
	}
}
