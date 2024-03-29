package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
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
        register();
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

	public void setCurrentGame(GameInfo game) {
		if(game != null) {
            try {
                UIFacade.getInstance().requestGame(game);
            } catch(BadUserException e) {
                e.printStackTrace();
                view.backToLogin();
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Set)
	        view.setGameList(UIFacade.getInstance().getGames());
	    if(arg instanceof Game) {
            if(((Game)arg).getPlayerList() != null && ((Game)arg).getPlayerList().size() > 0)
                view.startGameView();
            else
                requestGames();
        }
    }
    public void unregister() {
        UIFacade.getInstance().unregisterObserver(this);
    }
	public void register() {
		UIFacade.getInstance().registerObserver(this);
	}
}
