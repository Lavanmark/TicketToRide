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

    //pertaining to observable pattern
    public void update(Observable o, Object arg);

    public void unregister();

    public void register();

    //pertaining to poller
    public void startPollingCommands();

    public void stopPolling();

    public void sendMessage(String text);

    // pertaining to view
    void setView(IView boardmapActivity);

    public IBoardmapView getView();

    // when the view is notifying the presenter that a user wants to open a drawer
    public void tryOpenDestinationDrawer();

    public void tryOpenDrawTrainDrawer();

    public void tryOpenClaimRouteDrawer();

    //pertaining to game & player's hand
    public Game getGame();

    public void setGame(Game game);

    public User getUser();

    public void setUser(User user);

    public String getGameName();

    public boolean gameInProgress();

    public boolean gameFinished();

    public ArrayList<Player> getPlayers();

    public int getGameSize();

    public int getTrainCars();

    public String getPlayerName(int playerID);

    public PlayerColor getPlayerColor(int playerID);

    public int getResId(String resName, Context context);

    //pertaining to destinations
    public int[] getDiscardableDestinationCards() throws Exception;

    public int getDiscardableCount();

    //train card drawer notifying presenter of user action
    public void clickedFaceUpCard(int temp);

    public void clickedTrainCardDeck();

    //building train card drawer
    public int[] getFaceupCardColors() throws GameActionException;



    //TODO
    List<Route> getRoutes();

    boolean canClaim(Route r);

    void claimButtonClicked(Route r);

    void drawNewDestinationCards();

    void discardDestinations(boolean[] discardem);


    void clickedOutOfRoutes();

    void clickedOutOfDestinations();

    void clickedOutOfCards();
}
