package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.model.Player;

import java.util.List;

/**
 * Created by Tyler on 3/16/2017.
 */

public interface IGameOverPresenter extends IPresenter {
	List<Player> getPlayerList();
	String getWinnerName();
	String getLongestRouteNames();
	void unregister();
	void stopPolling();
}
