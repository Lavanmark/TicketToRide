package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.model.Player;

import java.util.List;

/**
 * Created by Tyler on 3/16/2017.
 */

public interface IGameOverView extends IView {
	void setPlayerList(List<Player> players);
	void setWinnerText(String playerName);
	void setLongestWinners(String playerNames);
}
