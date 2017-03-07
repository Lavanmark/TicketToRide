package com.floorcorn.tickettoride.ui.views;

import android.support.v4.widget.DrawerLayout;
import android.widget.Button;
import android.widget.FrameLayout;

import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Joseph Hansen
 */

public interface IBoardmapView extends IView {

	void checkStarted();

	void setBoard(Board board);
	void setPlayerTrainCardList(Map<TrainCardColor, Integer> cards);
	void setPlayerDestinationCardList(List<DestinationCard> destinationCardList);
	void setFaceUpTrainCards();
	void setDestinationCardChoices();
	void setPlayerTurn(Player player);
	void setScoreboard(Set<Player> playerSet);
	void setDestinationCardCompleted(DestinationCard destinationCard);
	void setPlayerPossibleRouteList(Set<Route> routeList);

	void setChatLog(GameChatLog log);

	void markRouteClaimed(Route claimed);
	void displayDrawingDeckDrawer();
	void displayDrawingDeckDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER);
	void hideDrawingDeckDrawer();
	void displayDestinationCardDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER);
	void displayDestinationCardDrawer();
	void hideDestinationDrawer();
	void displayClaimRouteDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER);
	void hideRouteDrawer();

	void displayHandDrawer();
	void animate_ClickOnDestinationCards();
	void animate_takeDestinationCards();
}
