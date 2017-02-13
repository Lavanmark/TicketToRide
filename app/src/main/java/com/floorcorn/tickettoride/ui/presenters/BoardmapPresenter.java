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
 */

public class BoardmapPresenter implements IPresenter, Observer {

    private IBoardmapView view = null;
	private IGame game = null;
	private IUser user;

    @Override
    public void setView(IView view) {
        if(view instanceof IBoardmapView)
	        this.view = (IBoardmapView)view;
	    else
	        throw new IllegalArgumentException("View arg was not an IBoardmapView");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof IGame)
	        UIFacade.getInstance().getCurrentGame();
	    else if(arg instanceof User)


    }
}
