package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
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
	private IGame game = null;
	private IUser user = null;

	public BoardmapPresenter() {
		this.game = UIFacade.getInstance().getCurrentGame();
		this.user = UIFacade.getInstance().getUser();
		UIFacade.getInstance().registerObserver(this);
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
        if(arg instanceof IGame) {
	        game = UIFacade.getInstance().getCurrentGame();
	        checkStart();
        }else if(arg instanceof User) {
	        if(UIFacade.getInstance().getUser() == null) {
		        UIFacade.getInstance().unregisterObserver(this);
		        view.backToLogin();
	        }
        }
    }
	private void checkStart() {
		if(game.hasStarted()){

		}
	}
	public boolean gameInProgress() {
		return game.hasStarted();
	}

	public void setUser(IUser user) {
		this.user = user;
	}

	public void setGame(IGame game) {
		this.game = game;
	}
	public String getGameName(){
		return this.game.getName();
	}
}
