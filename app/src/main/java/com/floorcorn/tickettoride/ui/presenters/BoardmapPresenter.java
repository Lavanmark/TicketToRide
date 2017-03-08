package com.floorcorn.tickettoride.ui.presenters;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;

/**
 * @author Joseph Hansen
 * @author Tyler
 * @author Michael
 */

public class BoardmapPresenter implements IPresenter, Observer {

    private IBoardmapView view = null;
	private Game game = null;
	private User user = null;

	private boolean discarding = false;
	private DestinationCard[] destCardsToDiscard;

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
			getChanges((Game)arg);
	        game = (Game)arg;
	        if(!game.hasStarted()) {
		        view.checkStarted();
	        } else {
		        view.checkStarted();
		        view.setFaceUpTrainCards();
		        view.setPlayerTrainCardList(game.getPlayer(user).getTrainCards());
				view.setPlayerDestinationCardList(game.getPlayer(user).getDestinationCards());
		        if(!discarding)
		            view.setDestinationCardChoices();
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
		//System.out.println("Changes being checked");
        //TrainCardColor newCard = getNewCardDrawn(newGame);
		//TrainCard card = game.getLastDrawn();
		//game.setLastDrawn(null);
        //if(card != null) {
          //  String toDisplay = "You drew a " + card.getColor().name() + " card";
            //Toast.makeText(view.getActivity(), toDisplay, Toast.LENGTH_LONG).show();
       // }

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

	public void setDiscarding(boolean areu) {
		discarding = areu;
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
		if(destCardsToDiscard == null)
			return;
		try {
			UIFacade.getInstance().discardDestinationCard(destCardsToDiscard[index]);
		} catch (GameActionException e) {
			e.printStackTrace();
		}
	}

	public void doneDiscarding() {
		destCardsToDiscard = null;
		discarding = false;
		UIFacade.getInstance().stopDiscarding();

	}

	public ArrayList<Player> getPlayers(){
		return game.getPlayerList();
	}

	public int getGameSize() {
		return game.getGameSize();
	}

	public TrainCardColor drawTrainCardFromDeck(){
        TrainCardColor color = null;
		try {
			color = UIFacade.getInstance().drawTrainCardFromDeck();
		} catch(GameActionException e) {
			e.printStackTrace();
		}
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            Toast.makeText(view.getActivity(), toDisplay, Toast.LENGTH_SHORT).show();
        }
        return color;
	}

	public TrainCardColor drawFromFaceUp(int position) {
        TrainCardColor color = null;
		try {
			color = UIFacade.getInstance().drawTrainCard(position);
		} catch(GameActionException e) {
			e.printStackTrace();
		}
        if(!(color == null)) {
            String toDisplay = "You drew 1 " + color.name() + " card";
            Toast.makeText(view.getActivity(), toDisplay, Toast.LENGTH_SHORT).show();
        }
        return color;
	}

	public void drawNewDestinationCards() {
		try {
			UIFacade.getInstance().drawDestinationCards();
		} catch(GameActionException e) {
			e.printStackTrace();
		}
	}

	public List<Route> getRoutes(){
		List<Route> lr = UIFacade.getInstance().getAvailableRoutes();
		System.out.println(lr.toString());
		return lr;

	}

	public void routeClicked(View v) {
	}

	public void claimButtonClicked(View v) {
	}
    /********************* BEGIN ANIMATION METHODS **********************************/

    /** Basic idea here is that this animate() method starts the chain,
     * then each animate method calls the next, which calls the next, and
     * so on. That was the only way to get it to work with the Handlers.
     */
    public void animate(){
        Corn.log(Level.FINE, "Beginning animation");
        animateDraw1();
    }

    private void animateDraw1(){
        view.displayHandDrawer();
        view.displayDrawingDeckDrawer();
        final Handler handler = new Handler();
        //Wait 2 seconds (1 from previous method) then draw from slot 1;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                drawFromFaceUp(1);
            }
        }, 3000);
        //Wait 5 seconds, then animate route selection
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateDrawTopDeck();
            }
        }, 6000);
    }

    private void animateDrawTopDeck() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TrainCardColor color = drawTrainCardFromDeck();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateDrawRoutes();
            }
        }, 4000);
    }

    private void animateDrawRoutes(){
        view.hideDrawingDeckDrawer();
        final Handler handler = new Handler();
        //Wait 2 seconds then open route drawer;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_clickDrawDestination();
            }
        }, 2000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_ClickOnDestinationCards();
            }
        }, 4000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_takeDestinationCards();
            }
        }, 6000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.hideDestinationDrawer();
            }
        }, 7000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateChatMessages();
            }
        }, 9000);

    }

    private void animateChatMessages(){
        UIFacade.getInstance().stopPollingAll();
        final Handler handler = new Handler();
        int i = 1;
        for(final Player p : UIFacade.getInstance().getCurrentGame().getPlayerList()){

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        UIFacade.getInstance().animate_sendChatMessage(new Message(p.getName() + ": Hey, i'm a chat message"));
                    } catch (BadUserException e){
                        e.printStackTrace();
                    }
                }
            }, 1500 * i);
            i++;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.hideHandDrawer();
            }
        }, (1500 * i) + 1500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIFacade.getInstance().pollCurrentGameParts(view);
                animateUpdateOtherPlayersTrainCards();
            }
        }, (1500 * i) + 3000);
    }

    private void animateUpdateOtherPlayersTrainCards(){

        Toast.makeText(view.getActivity(), "Updating other player score, destination cards, and train cards", Toast.LENGTH_LONG).show();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_showOtherPlayerInfo();
            }
        }, 5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                UIFacade.getInstance().animate_AddTrainCardForOtherPlayer();
                UIFacade.getInstance().animate_AddDestinationCardForOtherPlayer();
                UIFacade.getInstance().animate_UpdatePointsForOtherPlayer();
                view.animate_showOtherPlayerInfo();
            }
        }, 10000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animateDrawNewDestinationCards();
            }
        }, 11000);
    }

    private void animateDrawNewDestinationCards(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_clickDrawDestination();
                view.displayHandDrawer();
            }
        }, 3000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_clickDrawDestinationDeck();
            }
        }, 4500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_ClickOnDestinationCards();
            }
        }, 6000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.animate_takeDestinationCards();
            }
        }, 7200);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.hideDestinationDrawer();
                view.hideHandDrawer();
            }
        }, 8200);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationPlaceRoute_Player();
            }
        }, 8500);
    }

    private void animationPlaceRoute_Player(){
        view.animate_clickOpenRouteDrawer();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //UIFacade.getInstance().animate_ClaimRoute();
                view.animate_clickClaimRoute();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationPlaceRoute_OtherPlayer();
            }
        }, 4500);

    }

    private void animationPlaceRoute_OtherPlayer(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //UIFacade.getInstance().animate_ClaimRouteOtherPlayer();
                view.animate_clickClaimRoute();
            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.hideRouteDrawer();
                animationEnd();
            }
        }, 5000);
    }


    private void animationEnd(){
        Toast.makeText(view.getActivity(), "Animation Complete", Toast.LENGTH_LONG).show();
        Corn.log(Level.FINE, "Animation finished");

    }
    /*********************** END ANIMATION METHODS *********************************/
}
