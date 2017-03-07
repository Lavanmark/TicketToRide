package com.floorcorn.tickettoride.ui.presenters;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.widget.FrameLayout;
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
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Joseph Hansen
 * @author Tyler
 */

public class BoardmapPresenter implements IPresenter, Observer {

    private IBoardmapView view = null;
	private Game game = null;
	private User user = null;

	public BoardmapPresenter() {
		this.game = UIFacade.getInstance().getCurrentGame();
		this.user = UIFacade.getInstance().getUser();
		register();

	}

    @Override
    public void setView(IView view) {
        if(view instanceof IBoardmapView)
	        this.view = (IBoardmapView)view;
	    else
	        throw new IllegalArgumentException("View arg was not an IBoardmapView");
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof Game) {
	        game = (Game)arg;
	        if(!game.hasStarted()) {
		        view.checkStarted();
	        } else {
		        view.checkStarted();
		        view.setFaceUpTrainCards();
		        view.setPlayerTrainCardList(game.getPlayer(user).getTrainCards());
				view.setPlayerDestinationCardList(game.getPlayer(user).getDestinationCards());
	        }
        }
	    if(arg instanceof GameChatLog) {
		    view.setChatLog((GameChatLog)arg);
	    }
    }

	public void startPollingCommands() {
		UIFacade.getInstance().pollCurrentGameParts(view);
	}
	public void stopPolling() {
		UIFacade.getInstance().stopPollingGameStuff();
	}
	public boolean gameInProgress() {
		return game.hasStarted();
	}
	public void setUser(User user) {
		this.user = user;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public String getGameName(){
		return this.game.getName();
	}
	public void unregister() {
		UIFacade.getInstance().unregisterObserver(this);
	}
	public void register() {
		UIFacade.getInstance().registerObserver(this);
	}


    //This method compares the old game object to the new one to see what changes have been made.
    public void getChanges(Game newGame){
        TrainCardColor newCard = getNewCardDrawn(newGame);
        if(newCard != null)
        {
            String toDisplay = "You drew a " + newCard.name() + " card";
            Toast.makeText(view.getActivity(), toDisplay, Toast.LENGTH_LONG).show();
        }

    }


    public TrainCardColor getNewCardDrawn(Game newGame) {
        Map<TrainCardColor, Integer> oldCards = game.getPlayer(user).getTrainCards();
        Map<TrainCardColor, Integer> newCards = newGame.getPlayer(user).getTrainCards();
        Iterator oldIt = oldCards.entrySet().iterator();
        Iterator newIt = newCards.entrySet().iterator();
        while(oldIt.hasNext()) {
            Map.Entry oldPair = (Map.Entry) oldIt.next();
            Map.Entry newPair = (Map.Entry) newIt.next();
            //If there is a new card of an existing type
            if(oldPair.getValue() != newPair.getValue()){
                return (TrainCardColor) newPair.getKey();
            }
            if(!oldPair.getKey().equals(newPair.getKey())){
                return (TrainCardColor) newPair.getKey();
            }
        }
        if(newCards.size() == oldCards.size()){
            return null;
        }
        return (TrainCardColor)((Map.Entry)oldIt.next()).getKey();
    }

    public void displayDrawDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER){
        view.setPlayerTrainCardList(game.getPlayer(user).getTrainCards());
        view.displayDrawingDeckDrawer(DRAWER, DRAWER_HOLDER);
    }

    public void displayDestinationCardDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER){
        view.displayDestinationCardDrawer(DRAWER, DRAWER_HOLDER);
    }

    public void displayPlaceRouteDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER){
        view.displayClaimRouteDrawer(DRAWER, DRAWER_HOLDER);
    }


    public void sendMessage(String text) {
		try {
			UIFacade.getInstance().sendChatMessage(new Message(text, game.getGameID(), game.getPlayer(user).getName()));
		} catch(BadUserException e) {
			e.printStackTrace();
			view.backToLogin();
		}
	}

	public int getTrainCars() {
		return game.getPlayer(user).getTrainCarsLeft();
	}

	public int[] getFaceupCardColors() throws Exception {
		if (!gameInProgress()){
			throw new Exception("Game not Started");
		}
		TrainCard[] faceUp = UIFacade.getInstance().getFaceUpCards();
		int[] imageId = new int[5];
		for (int i = 0; i < 5; i++) {
			if(faceUp[i] == null) {
				//TODO add the back of a card image
				imageId[i] = R.drawable.card_black;
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
	public int[] getDestinationCards() throws Exception {
		if (!gameInProgress()){
			throw new Exception("Game not Started");
		}
		DestinationCard[] destinationCards = game.getPlayer(user).getInitialDestinationCards();
		int[] DestId = new int[3];
		for (int i = 0; i < 3; i++) {
			if (destinationCards[i] == null) {
				//TODO add the back of a card image
				DestId[i] = R.drawable.card_black;
				continue;
			} else {
				DestId[i] = getResId(destinationCards[i].getResName(), view.getActivity().getBaseContext());
			}
		}
		return DestId;
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
			//Field idField = c.getDeclaredField(resName);
			//return idField.getInt(idField);
			System.out.println(resName);
			return context.getResources().getIdentifier(resName, "drawable", context.getPackageName());
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public void discardDestination(int index) {
		DestinationCard toDiscard = game.getPlayer(user).getDestinationCards().get(index);
		try {
			UIFacade.getInstance().discardDestinationCard(toDiscard);
			this.keepTwoDestinations();
		} catch (GameActionException e) {
			e.printStackTrace();
		}
	}


	public void keepTwoDestinations(){
		/*TODO: case that the user wants to keep two destination cards
			called after discarding one
			Should clear the destinations from the destination drawer,
			and make sure the player's hand was updated
		 */
	}

	public void keepThreeDestinations() {
		/*TODO: case that the user wants to keep all three destination cards
			The model won't change, but we need to clear the destination cards
			that we want to keep from the drawer.
		 */

	}

	public void disableKeepThree() {
		view.getKeepThree().setEnabled(false);
	}

	public ArrayList<Player> getPlayers(){
		return game.getPlayerList();
	}

	public int getGameSize() {
		return game.getGameSize();
	}

	public void animate(){

	}

	public void drawTrainCardFromDeck(){
		try {
			UIFacade.getInstance().drawTrainCardFromDeck();
		} catch(GameActionException e) {
			e.printStackTrace();
		}
	}

	public void drawFromFaceUp(int position) {
		try {
			UIFacade.getInstance().drawTrainCard(position);
		} catch(GameActionException e) {
			e.printStackTrace();
		}
	}
}
