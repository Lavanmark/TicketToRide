package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.views.IGameOverView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Tyler on 3/16/2017.
 */

public class GameOverPresenter implements IGameOverPresenter, Observer{

	private Game game;
	private IGameOverView view;


	public GameOverPresenter() {
		game = UIFacade.getInstance().getCurrentGame();
	}

	@Override
	public void setView(IView view) {
		if(view instanceof IGameOverView)
			this.view = (IGameOverView) view;
	}

	@Override
	public void update(Observable o, Object arg) {

	}

	@Override
	public List<Player> getPlayerList() {
		return game.getPlayerList();
	}

	@Override
	public Player getWinner() {
		Player winner = null;
		for(Player p : game.getPlayerList()) {
			if(winner == null)
				winner = p;
			else if(winner.getScore() < p.getScore())
				winner = p;
		}
		return winner;
	}

	@Override
	public Player getLongestRoutePlayer() {
		return null;
	}
}
