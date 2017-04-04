package com.floorcorn.tickettoride.ui.presenters;

import android.content.Context;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.states.IState;
import com.floorcorn.tickettoride.states.PreTurnState;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.IView;
import com.floorcorn.tickettoride.ui.views.activities.BoardmapActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michael on 3/15/2017.
 *
 * @author Lily
 *         Implements the methods the view needs on the Presenter
 */

public interface IBoardMapPresenter {

    void unregister();

    void register();

    //pertaining to poller
    void startPollingCommands();

    void stopPolling();

    void sendMessage(String text);

    // pertaining to view
    void setView(IView boardmapActivity);

    IBoardmapView getView();

    //pertaining to game & player's hand
    Game getGame();

    void setGame(Game game);

    User getUser();

    void setUser(User user);

    String getGameName();

    boolean gameInProgress();

    boolean gameFinished();

    ArrayList<Player> getPlayers();

    int getGameSize();

    int getTrainCars();

    String getPlayerName(int playerID);

    PlayerColor getPlayerColor(int playerID);

    int getResId(String resName, Context context);

    //pertaining to destinations
    int[] getDiscardableDestinationCards() throws Exception;

    int getDiscardableCount();

    //train card drawer notifying presenter of user action
    void clickedFaceUpCard(int temp);

    void clickedTrainCardDeck();

    //building train card drawer
    int[] getFaceupCardColors() throws GameActionException;
    
    List<Route> getRoutes();

    boolean canClaim(Route r);

    void claimButtonClicked(Route r);

    void drawNewDestinationCards();

    void discardDestinations(boolean[] discardem);
    
    void closedRoutes();

    void closedDestinations();

    void closedCards();
    
    void openedRoutes();
    
    void openedDestinations();
    
    void openedCards();
    
    List<DestinationCard> getDestinationCards();

}
