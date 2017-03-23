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
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.IView;
import com.floorcorn.tickettoride.ui.views.activities.BoardmapActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Joseph Hansen
 * @author Tyler
 * @author Michael
 * @author Lily
 */


/**
 * The Boardmap Presenter class is responsible for interpreting user inputs from the view,
 * and updating the view when the model changes.
 *
 * THIS CLASS IS ORGANIZED INTO 6 GROUPS
 * (1) OBSERVER METHODS
 * (2) GET AND SET METHODS
 * (3) GAME REFERENCE METHODS
 * (4) METHODS TO MANIPULATE THE VIEW
 * (5) METHODS TO INTERACT WITH UIFACADE
 * (6) STATE ACCESS METHODS
 *
 *
 */
public class BoardmapPresenter implements IPresenter, Observer, IBoardMapPresenter {

    /** reference to the view **/
    private IBoardmapView view = null;
    /** reference to the current game **/
	private Game game = null;
    /** reference to the current user **/
	private User user = null;

    /** reference to the current state object **/
    private IState state = null;

    /** array of destination cards that are discarded **/
	private DestinationCard[] destCardsToDiscard;

    /**
     * Constructor for the BoardmapPresenter
     * Called by the BoardMap Activity when the activity is created
     *
     * @pre: The model does not contain a null instance of current game or user
     * @post: The presenter now has a reference to the current game and user
     * @post: The presenter is now registered as an observer of the model
     */
	public BoardmapPresenter() {
		this.game = UIFacade.getInstance().getCurrentGame();
		this.user = UIFacade.getInstance().getUser();
		destCardsToDiscard = new DestinationCard[3];
		register();
	}

    /**
     * Called by the BoardMap Activity, to connect itself to the presenter
     * @param view the view corresponding to this presenter.
     * @pre view(parameter) is an IBoardMapView
     * @pre view(parameter) != null
     * @post this.view != null
     * @exception IllegalArgumentException thrown if view != IBoardmapView
     *
     */
    @Override
    public void setView(IView view) {
        if(view instanceof IBoardmapView)
	        this.view = (IBoardmapView)view;
	    else
	        throw new IllegalArgumentException("View arg was not an IBoardmapView");
    }

    /*********************** OBSERVER METHODS *********************************/

    /**
     * This method is part of the observer pattern. When the model is changed, it notifies this
     * presenter and calls update
     *
     * @param o Observable object
     * @param arg Object that updated
     */
    @Override
    public void update(Observable o, Object arg) {
        /** changed object is the Game **/
        if(arg instanceof Game) {
            /** update presenter's reference to game **/
	        game = (Game)arg;
	        if(!game.hasStarted()) {
                /** if the game not started update view **/
		        view.checkStarted();
	        } else {
                /** if game started, update view, set cards, etc. **/
		        view.checkStarted();
		        view.setFaceUpTrainCards();
		        view.setPlayerTrainCardList(game.getPlayer(user).getTrainCards());
				view.setPlayerDestinationCardList(game.getPlayer(user).getDestinationCards());
		        view.setClaimRoutesList(game.getRoutes());
		        if(destCardsToDiscard == null || destCardsToDiscard[0] == null)
		            view.setDestinationCardChoices();
	        }
        }
        /** if changed object is the GameChatLog update the chat room in the view **/
	    if(arg instanceof GameChatLog) {
		    view.setChatLog((GameChatLog)arg);
	    }
    }

    /**
     * This method unregisters the Boardmap Presenter as an observer of the model class
     * @pre presenter is an observer (will be notified)
     * @post presenter is not an observer (will not be notified)
     */
    public void unregister() {
        UIFacade.getInstance().unregisterObserver(this);
    }

    /**
     * This method registers the Boardmap Presenter as an observer of the model class
     * @pre presenter is not an observer (not notified)
     * @post presenter is an observer (will be notified)
     */
    public void register() {
        UIFacade.getInstance().registerObserver(this);
    }

    /*********************** END OBSERVER METHODS *********************************/

    /*********************** GET AND SET METHODS *********************************/

    public void setUser(User user) {
        this.user = user;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public String getGameName(){
        return this.game.getName();
    }


    /*********************** END GET AND SET METHODS *********************************/

    /*********************** GAME REFERENCE METHODS *********************************/
    /** Game reference methods are usually called by the view to get information about what to display**/
    /**
     *
     * @return
     */
    public boolean gameInProgress() {
        return game.hasStarted();
    }

    /**
     *
     * @return
     */
    public int getTrainCars() {
        return game.getPlayer(user).getTrainCarsLeft();
    }

    /**
     *
     * @return
     */
    public ArrayList<Player> getPlayers(){
        return game.getPlayerList();
    }

    /**
     *
     * @return
     */
    public int getGameSize() {
        return game.getGameSize();
    }

    public int[] getDiscardableDestinationCards() throws Exception {
        if (!gameInProgress()){
            throw new Exception("Game not Started");
        }
        destCardsToDiscard = game.getPlayer(user).getDiscardableDestinationCards();
        if(destCardsToDiscard == null)
            return null;

        int[] DestId = new int[destCardsToDiscard.length];
        for (int i = 0; i < destCardsToDiscard.length; i++) {
            if (destCardsToDiscard[i] == null) {
                DestId[i] = R.drawable.back_destinations;
            } else {
                DestId[i] = getResId(destCardsToDiscard[i].getResName(), view.getActivity().getBaseContext());
            }
        }
        return DestId;
    }

    public int getDiscardableCount() {
        if(game.getPlayer(user).getDiscardableDestinationCards() != null) {
            if(game.getPlayer(user).getDestinationCards().size() == 3)
                return 1;
            return 2;
        }
        return 0;
    }

    /**
     * Takes a string and converts it to a resource Id.
     * Used to match the destination card object to the correct image
     * @param resName string of teh resource name, i.e. dest_card_name
     * @param context the class the resource is in, i.e. Drawable
     * @return int of the resource
     */
    public static int getResId(String resName, Context context) {
        try {
            return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getPlayerName(int playerID) {
        for(Player p : game.getPlayerList())
            if(p.getPlayerID() == playerID)
                return p.getName();
        return "NONE";
    }

    /**
     * Returns the player color corresponding to the player ID. Returns null if playerID does
     * not correspond to a player.
     * @param playerID int player's ID
     * @return PlayerColor object or null
     */
    public PlayerColor getPlayerColor(int playerID) {
        for (Player p : game.getPlayerList())
            if (p.getPlayerID() == playerID)
                return p.getColor();
        return null;
    }

	public boolean gameFinished() {
		return game.isFinished();
	}


    /*********************** END GAME REFERENCE METHODS *********************************/


    /*********************** INTERACTIONS WITH UI FACADE *********************************/

    /**
     * This method is used to start the poller for commands.
     * Commands only start being used when the game has started.
     * @pre: game started
     * @post: poller active
     *
     */
	public void startPollingCommands() {
		stopPolling();
		UIFacade.getInstance().pollCurrentGameParts(view);
	}

    /**
     *
     */
    public void stopPolling() {
		UIFacade.getInstance().stopPollingGameStuff();
	}

    /**
     *
     * @param text
     */
    public void sendMessage(String text) {
        try {
            UIFacade.getInstance().sendChatMessage(new Message(text, game.getGameID(), game.getPlayer(user).getName()));
        } catch(BadUserException e) {
            e.printStackTrace();
            view.backToLogin();
        }
    }
    
    /**
     *
     */
	public void discardDestinations(boolean[] shouldDiscard) {
		if(destCardsToDiscard == null)
			return;
		
		List<DestinationCard> toDiscard = new ArrayList<>();
		for(int i = 0; i < shouldDiscard.length; i++)
			if(shouldDiscard[i])
				toDiscard.add(destCardsToDiscard[i]);
		destCardsToDiscard = new DestinationCard[3];
		try {
			UIFacade.getInstance().discardDestinationCards(toDiscard);
		} catch (GameActionException e) {
			e.printStackTrace();
		} catch(BadUserException e) {
			e.printStackTrace();
			view.backToLogin();
		}
	}
    /**
     *
     * @return
     */
	public boolean drawTrainCardFromDeck(){
//        try {
            this.state.drawTrainCardFromDeck(this);
			return true;
//		} catch(GameActionException e) {
//			e.printStackTrace();
//		} catch(BadUserException e) {
//			e.printStackTrace();
//			view.backToLogin();
//		}
//		return false;
	}

    /**
     *
     * @param position
     * @return
     */
	public boolean drawFromFaceUp(int position) {
		this.state.drawFaceUpCard(this, position);
		return true; //TODO this should be like the function above
	}

    /**
     *
     * @return
     * @throws GameActionException
     */
    public int[] getFaceupCardColors() throws GameActionException {
        if (!gameInProgress()){
            throw new GameActionException("Game not Started");
        }
        TrainCard[] faceUp = UIFacade.getInstance().getFaceUpCards();
        int[] imageId = new int[5];
        for (int i = 0; i < 5; i++) {
            if(faceUp[i] == null) {
                imageId[i] = R.drawable.back_trains;
                continue;
            }
            TrainCardColor color = faceUp[i].getColor();
            switch (color) {
                case RED:
                    imageId[i] = R.drawable.card_red;
                    break;
                case GREEN:
                    imageId[i] = R.drawable.card_green;
                    break;
                case BLUE:
                    imageId[i] = R.drawable.card_blue;
                    break;
                case YELLOW:
                    imageId[i] = R.drawable.card_yellow;
                    break;
                case PURPLE:
                    imageId[i] = R.drawable.card_purple;
                    break;
                case BLACK:
                    imageId[i] = R.drawable.card_black;
                    break;
                case ORANGE:
                    imageId[i] = R.drawable.card_orange;
                    break;
                case WHITE:
                    imageId[i] = R.drawable.card_white;
                    break;
                case WILD:
                    imageId[i] = R.drawable.card_wild;
                    break;
            }
        }
        return imageId;
    }

    /**
     *
     */
	public void drawNewDestinationCards() {
        this.state.drawDestinationTickets(this);
	}

    /**
     *
     * @return
     */
	public List<Route> getRoutes(){
		List<Route> lr = UIFacade.getInstance().getRoutes();
		System.out.println(lr.toString());
		return lr;

	}

    /**
     *
     * @param route
     */
	public void claimButtonClicked(Route route) {
		if(route != null) {
			try {
				this.state.claimRoute(this, route);
				Toast.makeText(view.getActivity(), "Claimed route: " + route.getFirstCity().getName() + " to " + route.getSecondCity().getName(), Toast.LENGTH_SHORT).show();
				return;
			} catch(BadUserException e) {
				e.printStackTrace();
				view.backToLogin();
			} catch(GameActionException e) {
				e.printStackTrace();
			}
		}
		Toast.makeText(view.getActivity(), "No routes can be claimed!", Toast.LENGTH_SHORT).show();
//        this.state.claimRoute(this, route);
	}

    /**
     *
     * @param route
     * @return
     */
	public boolean canClaim(Route route) {
		return UIFacade.getInstance().canClaimRoute(route);
	}

    /*********************** END INTERACTIONS WITH UI FACADE *********************************/

    /************************ BEGIN STATE ACCESS METHODS ************************************/

    @Override
    public void enableDrawTrainCards(){
        view.enableTrainCardButton(true);
    }

    @Override
    public void enableDrawDestinationCards(){
        view.enableDestinationCardButton(true);
    }

    @Override
    public void enableClaimRoute(){
        view.enableClaimRouteButton(true);
    }

    @Override
    public void disableDrawTrainCards() {
       view.enableTrainCardButton(false);
    }

    @Override
    public void disableDrawDestinationCards() {
        view.enableDestinationCardButton(false);
    }

    @Override
    public void disableClaimRoute() {
        view.enableClaimRouteButton(true);
    }

    @Override
    public void openDestinationDrawer() {
        view.getDestinationDrawer().open();
    }

    @Override
    public void openClaimRouteDrawer() {
        view.getClaimRouteDrawer().open();

    }

    @Override
    public void openDrawTrainDrawer() {
       view.getTrainCardDrawer().open();

    }

    @Override
    public void closeDestinationDrawer() {
        view.getDestinationDrawer().hide();

    }

    @Override
    public void closeClaimRouteDrawer() {
        view.getClaimRouteDrawer().hide();
    }

    @Override
    public void closeDrawTrainDrawer() {
        view.getTrainCardDrawer().hide();
    }

    public void setState(IState state) {
        if(this.state != null)
        {
            this.state.exit(this);
        }
        this.state = state;
        this.state.enter(this);
    }

    @Override
    public void displayMessage_short(String message) {
        Toast.makeText(view.getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayMessage_long(String message) {
        Toast.makeText(view.getActivity(), message, Toast.LENGTH_LONG).show();
    }

    /************************ END STATE ACCESS METHODS *************************************/
}
