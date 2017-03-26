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
import com.floorcorn.tickettoride.ui.views.drawers.BMDrawer;

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
 * <p>
 * THIS CLASS IS ORGANIZED INTO 6 GROUPS
 * (1) OBSERVER METHODS
 * (2) GET AND SET METHODS
 * (3) GAME REFERENCE METHODS
 * (4) METHODS TO MANIPULATE THE VIEW
 * (5) METHODS TO INTERACT WITH UIFACADE
 * (6) STATE ACCESS METHODS
 */
public class BoardmapPresenter
        implements IPresenter, Observer, IBoardMapPresenter, IBoardMapPresenterStateful {


    private IBoardmapView view = null;
    private Game game = null;
    private User user = null;
    private IState state = null;

    private DestinationCard[] destCardsToDiscard;

    public BoardmapPresenter() {
        this.game = UIFacade.getInstance().getCurrentGame();
        this.user = UIFacade.getInstance().getUser();
        setDestCardsToDiscard(new DestinationCard[3]);
        register();
    }

    @Override
    public void setView(IView view) {
        if (view instanceof IBoardmapView)
            this.view = (IBoardmapView) view;
        else
            throw new IllegalArgumentException("View arg was not an IBoardmapView");
    }

    @Override
    public IBoardmapView getView() {
        return view;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public User getUser() {
        return user;
    }
    
    @Override
    public void setDestCardsToDiscard(DestinationCard[] destCardsToDiscard) {
        this.destCardsToDiscard = destCardsToDiscard;
    }

    @Override
    public void update(Observable o, Object arg) {
        /** changed object is the Game **/
        if (arg instanceof Game) {
            /** update presenter's reference to game **/
            game = (Game) arg;
            if (!game.hasStarted()) {
                /** if the game not started update view **/
                view.checkStarted();
            } else {
                /** if game started, update view, set cards, etc. **/
                view.checkStarted();
                view.setFaceUpTrainCards();
                view.setPlayerTrainCardList(game.getPlayer(user).getTrainCards());
                view.setPlayerDestinationCardList(game.getPlayer(user).getDestinationCards());
                view.setClaimRoutesList(game.getRoutes());
                if (destCardsToDiscard == null || destCardsToDiscard[0] == null)
                    view.setDestinationCardChoices();
                //initialize state to PreTurn state for all players
                if (state == null)
                    setState(new PreTurnState());
                //if waiting for your turn, check if it is your turn
                if (state instanceof PreTurnState) {
                    if (game.getPlayer(user).isTurn())
                        //if it is your turn, setTurn transitions between preTurn and TurnState
                        state.setTurn(this, game.getPlayer(user));
                }
            }
        }
        /** if changed object is the GameChatLog update the chat room in the view **/
        if (arg instanceof GameChatLog) {
            view.setChatLog((GameChatLog) arg);
        }
    }

    @Override
    public void unregister() {
        UIFacade.getInstance().unregisterObserver(this);
    }

    @Override
    public void register() {
        UIFacade.getInstance().registerObserver(this);
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public String getGameName() {
        return this.game.getName();
    }
	
    @Override
    public boolean gameInProgress() {
        return game.hasStarted();
    }

    @Override
    public int getTrainCars() {
        return game.getPlayer(user).getTrainCarsLeft();
    }

    @Override
    public ArrayList<Player> getPlayers() {
        return game.getPlayerList();
    }

    @Override
    public int getGameSize() {
        return game.getGameSize();
    }

    @Override
    public int[] getDiscardableDestinationCards() throws Exception {
        if (!gameInProgress()) {
            throw new Exception("Game not Started");
        }
        destCardsToDiscard = game.getPlayer(user).getDiscardableDestinationCards();
        if (destCardsToDiscard == null)
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

    @Override
    public int getDiscardableCount() {
        if (game.getPlayer(user).getDiscardableDestinationCards() != null) {
            if (game.getPlayer(user).getDestinationCards().size() == 3)
                return 1;
            return 2;
        }
        return 0;
    }

    @Override
    public void clickedFaceUpCard(int temp) {
	    if(!state.drawFaceUpCard(this, temp))
	    	displayMessage_short("Could not draw card!");
    }

    @Override
    public void clickedTrainCardDeck() {
	    if(!state.drawTrainCardFromDeck(this))
	    	displayMessage_short("Could not draw card!");
    }

    /**
     * Takes a string and converts it to a resource Id.
     * Used to match the destination card object to the correct image
     *
     * @param resName string of teh resource name, i.e. dest_card_name
     * @param context the class the resource is in, i.e. Drawable
     * @return int of the resource
     */
    @Override
    public int getResId(String resName, Context context) {
        try {
            return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public String getPlayerName(int playerID) {
        for (Player p : game.getPlayerList())
            if (p.getPlayerID() == playerID)
                return p.getName();
        return "NONE";
    }

    @Override
    public PlayerColor getPlayerColor(int playerID) {
        for (Player p : game.getPlayerList())
            if (p.getPlayerID() == playerID)
                return p.getColor();
        return null;
    }
    
    @Override
    public boolean gameFinished() {
        return game.isFinished();
    }


    /**
     * POLLER
     **/
    @Override
    public void startPollingCommands() {
        stopPolling();
        UIFacade.getInstance().pollCurrentGameParts(view);
    }

    @Override
    public void stopPolling() {
        UIFacade.getInstance().stopPollingGameStuff();
    }

    @Override
    public void sendMessage(String text) {
        try {
            UIFacade.getInstance().sendChatMessage(new Message(text, game.getGameID(), game.getPlayer(user).getName()));
        } catch (BadUserException e) {
            e.printStackTrace();
            view.backToLogin();
        }
    }


    /**
     * STATE
     **/
    
    @Override
    public void discardDestinations(boolean[] shouldDiscard) {
        state.discardDestinationTickets(this, destCardsToDiscard, shouldDiscard);
    }
	
	@Override
	public void closedRoutes() {
		state.closeClaimRoute(this);
	}
	
	@Override
	public void closedDestinations() {
		state.closeDestinationDraw(this);
	}
	
	@Override
	public void closedCards() {
		state.closeTrainDraw(this);
	}
	
	@Override
	public void openedRoutes() {
		state.openClaimRoute(this);
	}
	
	@Override
	public void openedDestinations() {
		state.openDestinationDraw(this);
	}
	
	@Override
	public void openedCards() {
		state.openTrainDraw(this);
	}
	
	@Override
    public int[] getFaceupCardColors() throws GameActionException {
        if (!gameInProgress()) {
            throw new GameActionException("Game not Started");
        }
        TrainCard[] faceUp = UIFacade.getInstance().getFaceUpCards();
        int[] imageId = new int[5];
        for (int i = 0; i < 5; i++) {
            if (faceUp[i] == null || faceUp[i].getColor() == null) {
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

    @Override
    public void drawNewDestinationCards() {
        this.state.drawDestinationTickets(this);
    }
    
    @Override
    public List<Route> getRoutes() {
        List<Route> lr = UIFacade.getInstance().getRoutes();
        System.out.println(lr.toString());
        return lr;
    }
    
    @Override
    public void claimButtonClicked(Route route) {
	    this.state.claimRoute(this, route);
    }
    
    @Override
    public boolean canClaim(Route route) {
        return UIFacade.getInstance().canClaimRoute(route);
    }

    @Override
    public void openDestinationDrawer() {
        view.getDestinationDrawer().open();
    }
	
	@Override
	public BMDrawer getOpenDrawer() {
		if(view.getDestinationDrawer().isOpen())
			return view.getDestinationDrawer();
		if(view.getClaimRouteDrawer().isOpen())
			return view.getClaimRouteDrawer();
		if(view.getTrainCardDrawer().isOpen())
			return view.getTrainCardDrawer();
		return null;
	}
	
	@Override
    public void setState(IState state) {
        System.out.println("Presenter: setState");
        if (this.state != null) {
            this.state.exit(this);
        }
        System.out.println("Changing State: " + state.getClass().getName());
        //this.displayMessage_short("Changing State: " + state.getClass().getSimpleName());
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

    @Override
    public void updateDestinationDrawer() {
        view.getDestinationDrawer().updateDestinations();
    }
}
