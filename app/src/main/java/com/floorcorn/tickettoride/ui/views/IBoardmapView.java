package com.floorcorn.tickettoride.ui.views;

import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;

import java.util.List;
import java.util.Map;

/**
 * @author Joseph Hansen
 */

public interface IBoardmapView extends IView {
	
	/**
	 * Enables/disables certain buttons depending on if the game is going or waiting to start.
	 *
	 * @pre 0 < numPlayersIn(game) <= size(game)
	 * @post if numPlayersIn(game) < size(game): buttons for animation and showing drawers are
	 * 		disabled
	 * @post if numPlayersIn(game) == size(game): poller started
	 * @post if numPlayersIn(game) == size(game): buttons for animation and showing drawers are
	 * 		enabled
	 * @post if numPlayersIn(game) == size(game): call setupPlayerIcons() to display players' info
	 * 		(implementation is to show buttons at top of screen that show popup data when clicked)
	 */
	void checkStarted();
	
	/**
	 * Sets the number of train car cards of each color in the text views for current player.
	 *
	 * @pre game board is initialized
	 * @post card count for each type of card is displayed in corresponding color's text field
	 * 		in the player's hand drawer
	 * @param cards
	 */
	void setPlayerTrainCardList(Map<TrainCardColor, Integer> cards);
	
	/**
	 * This is in the player hand.
	 *
	 * @pre game board is initialized so destinationTicketHolder exists
	 * @post delete(old(destinationTicketHolder.views)
	 * @post for each destination card d, add new text view t containing d.toString() to
	 * 		destinationTicketHolder
	 * @param destinationCardList
	 */
	void setPlayerDestinationCardList(List<DestinationCard> destinationCardList);
	
	/**
	 * Sets the routes list in the routes drawer.
	 *
	 * @pre game board is initialized so routeAdapter exists
	 * @post calls swapList() on the routeAdapter with the routes parameter, which removes
	 * 		everything in the routeAdapter and adds each route to the routeAdapter list
	 * @param routes
	 */
	void setClaimRoutesList(List<Route> routes);
	
	/**
	 * Displays the face up train cards if drawer open.
	 *
	 * @pre game board is initialized so draw drawer exists
	 * @post if open(drawDrawer): 5 (per the game rules) cards' front sides are displayed in
	 * 		drawDrawer on the ImageButtons designated for the job
	 */
	void setFaceUpTrainCards();
	
	/**
	 * Displays destination card options if drawer open.
	 *
	 * @pre game board is initialized so destination card drawer exists
	 * @post if open(destinationDrawer): call buildDestinationDrawer() which will display
	 * 		destination ticket deck and selection choices
	 */
	void setDestinationCardChoices();
	
	/**
	 * Sets the visible chat log.
	 *
	 * @pre log is a valid GameChatLog
	 * @pre BoardmapActivity.onCreate() has been called so as to create the chat layout UI element
	 * @post deleteAll(old(messagesInChatLayout))
	 * @post add new TextView containing message.toString() for each in log.getRecentMessages()
	 * @param log GameChatLog object
	 */
	void setChatLog(GameChatLog log);
}
