package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Joseph Hansen
 * @author Tyler
 */

public class BoardmapPresenter implements IPresenter, Observer {

    private IBoardmapView view = null;
	private Game game = null;
	private User user = null;

	public BoardmapPresenter() {
		this.game = UIFacade.getInstance().getCurrentGame();
		this.user = UIFacade.getInstance().getUser();
		register();
	}

    @Override
    public void setView(IView view) {
        if(view instanceof IBoardmapView)
	        this.view = (IBoardmapView)view;
	    else
	        throw new IllegalArgumentException("View arg was not an IBoardmapView");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Game) {
	        game = (Game)arg;
	        if(game.hasStarted()) //TODO this might break since it launches pregame...
	            view.checkStarted();
        }
	    if(arg instanceof GameChatLog) {
		    view.setChatLog((GameChatLog)arg);
	    }
    }

	public void startPollingCommands() {
		UIFacade.getInstance().pollCurrentGameParts(view);
	}
	public void stopPolling() {
		UIFacade.getInstance().stopPolling();
	}
	public boolean gameInProgress() {
		return game.hasStarted();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public String getGameName(){
		return this.game.getName();
	}
	public void unregister() {
		UIFacade.getInstance().unregisterObserver(this);
	}
	public void register() {
		UIFacade.getInstance().registerObserver(this);
	}

	public void sendMessage(String text) {
		try {
			UIFacade.getInstance().sendChatMessage(new Message(text, game.getGameID(), game.getPlayer(user).getName()));
		} catch(BadUserException e) {
			e.printStackTrace();
			view.backToLogin();
		}
	}


}
