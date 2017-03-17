package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.List;

/**
 * Created by Tyler on 3/16/2017.
 */

public interface IGameOverPresenter extends IPresenter {
	List<Player> getPlayerList();
	Player getWinner();
	Player getLongestRoutePlayer();
}
