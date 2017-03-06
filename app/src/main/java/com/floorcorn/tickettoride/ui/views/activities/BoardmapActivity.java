package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class BoardmapActivity extends AppCompatActivity implements IBoardmapView, NavigationView.OnNavigationItemSelectedListener   {

	BoardmapPresenter presenter = null;

	private final int MAXDESTINATIONS = 3;
	private final int MAXFACEUP = 5;
	private Board boardmap;

	//button used to show animation
	private Button animateButton;

	// buttons used to open the drawers
	private Button drawCardsButton;
	private Button drawDestinationTicketsButton;
	private Button claimRouteButton;
	private Button displayHandButton;

	//elements related to Draw Destination Tickets Drawer
	private Button drawFromDestinationDeck;
	private ImageButton destinationTickets[] = new ImageButton[MAXDESTINATIONS];

	//elements related to Draw Cards Drawer
	private Button drawFromCardDeck;
	private ImageButton faceupCards[] = new ImageButton[MAXFACEUP];

	//elements related to Claiming Route
	


	//elements related to Player's Hand
	private TextView redCount;
	private TextView orangeCount;
	private TextView yellowCount;
	private TextView greenCount;
	private TextView blueCount;
	private TextView purpleCount;
	private TextView blackCount;
	private TextView whiteCount;
	private TextView wildCount;

	private TextView trainCount;

	//elements related to the PlayerStatus/Turn Icons


	//elements related to the map



	//elements related to chat
	private LinearLayout chatLayout;
	private Button sendMessageBut;
	private EditText chatTextField;


	/*
	- presenter:IPresenter
	- boardmap:Board
	- drawCardsButton:Button
	- drawDestinationTicketsButton:Button
	- placeTrainsButton:Button ???
	- routeSelectionButton:Button ???
	- faceUpCardViews:ArrayList<Image>
	- playerIcons:Set<Image>
	- placeRouteDrawer:Drawer
	- drawCardsDrawer:Drawer
	- chooseDestinationCardDrawer:Drawer
	- faceUpCards:ArrayList<TrainCard>
	- drawTrainCardDeck:Image
	- drawDestinationCardDeck:Button/Image
	- playerTrainCard:ArrayList<TrainCard>
	- playerPossibleRoutes:Set<Route>


	void setBoard(Board board);
	void setPlayerTrainCardList(ArrayList<TrainCard> trainCardList);
	void setPlayerDestinationCardList(Set<DestinationCard> destinationCardList);
	void setFaceUpTrainCards(ArrayList<TrainCard> faceUpTrainCards);
	void setDestinationCardChoices(Set<DestinationCard> destinationCardChoices);
	void setPlayerTurn(Player player);
	void setScoreboard(Set<Player> playerSet);
	void setDestinationCardCompleted(DestinationCard destinationCard);
	void setPlayerPossibleRouteList(Set<Route> routeList);
	Card getCardDrawn();
	DestinationCard getDestinationCardPicked();
	void markRouteClaimed(Route claimed);
	void displayDrawingDeckDrawer();
	void hideDrawingDeckDrawer();
	void displayDestinationCardDrawer();



	 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boardmap);


        presenter = new BoardmapPresenter();
	    presenter.setView(this);

	    Toolbar mToolbar = (Toolbar) findViewById(R.id.bmap_toolbar);
	    setSupportActionBar(mToolbar);
	    if(getSupportActionBar() != null)
	        getSupportActionBar().setTitle(presenter.getGameName());


		//initialize UI elements

		drawDestinationTicketsButton = (Button)findViewById(R.id.open_dest_button);
		displayHandButton = (Button)findViewById(R.id.open_hand_button);
		claimRouteButton = (Button)findViewById(R.id.open_route_button);
		drawCardsButton = (Button)findViewById(R.id.open_card_button);
		animateButton = (Button)findViewById(R.id.animateButton);

	    redCount = (TextView)findViewById(R.id.red_card_count);
	    orangeCount = (TextView)findViewById(R.id.orange_card_count);
	    yellowCount = (TextView)findViewById(R.id.yellow_card_count);
	    greenCount = (TextView)findViewById(R.id.green_card_count);
	    blueCount = (TextView)findViewById(R.id.blue_card_count);
	    purpleCount = (TextView)findViewById(R.id.purple_card_count);
	    blackCount = (TextView)findViewById(R.id.black_card_count);
	    whiteCount = (TextView)findViewById(R.id.white_card_count);
	    wildCount = (TextView)findViewById(R.id.wild_card_count);

	    trainCount = (TextView)findViewById(R.id.train_count);

	    //CHAT
	    chatLayout = (LinearLayout)findViewById(R.id.chatHolder);
	    chatTextField = (EditText)findViewById(R.id.chatMessageField);
	    sendMessageBut = (Button)findViewById(R.id.sendMessageButton);
	    sendMessageBut.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View v) {
				if(chatTextField.getText().toString().isEmpty())
					return;
			    if(!presenter.gameInProgress())
				    return;
			    presenter.sendMessage(chatTextField.getText().toString());
			    chatTextField.setText("");
		    }
	    });
	    final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
	    final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
	    drawDestinationTicketsButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    Corn.log("Opening Destination Drawer");
			    displayDestinationCardDrawer(DRAWER, DRAWER_HOLDER);
		    }
	    });
	    drawCardsButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    displayDrawingDeckDrawer(DRAWER, DRAWER_HOLDER);
		    }
	    });
	    claimRouteButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    displayClaimRouteDrawer(DRAWER, DRAWER_HOLDER);
		    }
	    });
	    displayHandButton.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {
			    DRAWER.openDrawer(GravityCompat.END); //Gravity End is on the right side
			    //TODO maybe this needs its own function to get all this information set up.
		    }
	    });

	    checkStarted();
	    if(!presenter.gameInProgress())
		    launchPreGame();
    }

	@Override
	public void onStop(){
		presenter.unregister();
		presenter.stopPolling();
		super.onStop();
	}

    @Override
    protected void onResume(){
        super.onResume();
	    checkStarted();
    }

    public void launchPreGame() {
        startActivity(new Intent(BoardmapActivity.this, PregameActivity.class));
    }

    @Override
    public void setPresenter(IPresenter presenter) {
	    if(presenter instanceof BoardmapPresenter)
            this.presenter = (BoardmapPresenter)presenter;
	    else
		    throw new IllegalArgumentException();
    }

	@Override
	public void checkStarted() {
		if(!presenter.gameInProgress()) {
			//  TODO: you can click out of the PreGame Activity. Why?
			//Tyler - IDK how to prevent it and still go back to game list...
			drawDestinationTicketsButton.setEnabled(false);
			displayHandButton.setEnabled(false);
			claimRouteButton.setEnabled(false);
			drawCardsButton.setEnabled(false);
			sendMessageBut.setEnabled(false);
		} else {
			presenter.startPollingCommands();
			drawDestinationTicketsButton.setEnabled(true);
			drawCardsButton.setEnabled(true);
			claimRouteButton.setEnabled(true);
			displayHandButton.setEnabled(true);
			sendMessageBut.setEnabled(true);
		}
	}

	@Override
	public void setChatLog(GameChatLog log) {
		chatLayout.removeAllViews();
		for(Message message : log.getRecentMessages()) {
			TextView tv = new TextView(chatLayout.getContext());
			tv.setText(message.toString());
			chatLayout.addView(tv);
		}
	}

	@Override
	public void setBoard(Board board) {

	}

	@Override
	public void setPlayerTrainCardList(Map<TrainCardColor, Integer> cards) {
		redCount.setText(String.valueOf(cards.containsKey(TrainCardColor.RED)? cards.get(TrainCardColor.RED) : 0));

		orangeCount.setText(String.valueOf(cards.containsKey(TrainCardColor.ORANGE)? cards.get(TrainCardColor.ORANGE) : 0));
		yellowCount.setText(String.valueOf(cards.containsKey(TrainCardColor.YELLOW)? cards.get(TrainCardColor.YELLOW) : 0));
		greenCount.setText(String.valueOf(cards.containsKey(TrainCardColor.GREEN)? cards.get(TrainCardColor.GREEN) : 0));
		blueCount.setText(String.valueOf(cards.containsKey(TrainCardColor.BLUE)? cards.get(TrainCardColor.BLUE) : 0));
		purpleCount.setText(String.valueOf(cards.containsKey(TrainCardColor.PURPLE)? cards.get(TrainCardColor.PURPLE) : 0));
		blackCount.setText(String.valueOf(cards.containsKey(TrainCardColor.BLACK)? cards.get(TrainCardColor.BLACK) : 0));
		whiteCount.setText(String.valueOf(cards.containsKey(TrainCardColor.WHITE)? cards.get(TrainCardColor.WHITE) : 0));
		wildCount.setText(String.valueOf(cards.containsKey(TrainCardColor.WILD)? cards.get(TrainCardColor.WILD) : 0));
		for(TrainCardColor color : TrainCardColor.values())
			System.out.println(color.name() + ": " + cards.get(color));
	}

	@Override
	public void setPlayerDestinationCardList(Set<DestinationCard> destinationCardList) {

	}

	@Override
	public void setFaceUpTrainCards() {
		//TODO must limit to if the drawer is open
		//setFaceupImages();
	}

	@Override
	public void setDestinationCardChoices(Set<DestinationCard> destinationCardChoices) {

	}

	@Override
	public void setPlayerTurn(Player player) {

	}

	@Override
	public void setScoreboard(Set<Player> playerSet) {

	}

	@Override
	public void setDestinationCardCompleted(DestinationCard destinationCard) {

	}

	@Override
	public void setPlayerPossibleRouteList(Set<Route> routeList) {

	}

	@Override
	public DestinationCard getDestinationCardPicked() {
		return null;
	}

	@Override
	public void markRouteClaimed(Route claimed) {

	}

	private void setFaceupImages() {
		faceupCards[0] = (ImageButton)findViewById(R.id.card1);
		faceupCards[1] = (ImageButton)findViewById(R.id.card2);
		faceupCards[2] = (ImageButton)findViewById(R.id.card3);
		faceupCards[3] = (ImageButton)findViewById(R.id.card4);
		faceupCards[4] = (ImageButton)findViewById(R.id.card5);

		int[] imageId;
		try {
			imageId = presenter.getFaceupCardColors();
			for(int i = 0; i < MAXFACEUP; i++){
				faceupCards[i].setImageResource(imageId[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void displayDrawingDeckDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
//		faceupCards[1].setImageResource(R.drawable.card_blue);
//		faceupCards[2].setImageResource(R.drawable.card_wild);
		displayLeftDrawer(R.layout.drawer_draw_cards, DRAWER, DRAWER_HOLDER);
		drawFromCardDeck = (Button)findViewById(R.id.draw_from_card_deck);

		setFaceupImages();

		drawFromCardDeck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//presenter.drawNextCard();
				//TODO
			}
		});
		for(int i = 0; i < MAXFACEUP; i++){
			faceupCards[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
				//presenter.faceupClicked();
					//TODO
				}
			});
		}

	}

	@Override
	public void hideDrawingDeckDrawer() {

	}

	/**
	 *
	 * @param DRAWER The layout of the Boardmap Activity
	 * @param DRAWER_HOLDER The layout of the frame that opens the drawer
     */
	@Override
	public void displayDestinationCardDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
		displayLeftDrawer(R.layout.drawer_destinations, DRAWER, DRAWER_HOLDER);
		drawFromDestinationDeck = (Button)findViewById(R.id.draw_from_dest_deck);
		destinationTickets[0] = (ImageButton)findViewById(R.id.dest_card1);
		destinationTickets[1] = (ImageButton)findViewById(R.id.dest_card2);
		destinationTickets[2] = (ImageButton)findViewById(R.id.dest_card3);

		destinationTickets[0].setImageResource(R.drawable.dest_acydalia_viking_1);

		drawFromDestinationDeck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO
			}
		});
		for(int i = 0; i < MAXDESTINATIONS; i++){
			destinationTickets[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//TODO
				}
			});
		}


	}

	@Override
	public void hideDestinationDrawer() {

	}

	@Override
	public void displayClaimRouteDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
		displayLeftDrawer(R.layout.drawer_place_routes, DRAWER, DRAWER_HOLDER);
	}

	@Override
	public void hideRouteDrawer() {

	}

	private void displayLeftDrawer(int drawerID, DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER){
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(drawerID, null);
		DRAWER_HOLDER.removeAllViews();
		DRAWER_HOLDER.addView(layout);
		DRAWER.openDrawer(GravityCompat.START);
		Corn.log("Left drawer opened");
	}

	@Override
    public void backToLogin() {
	    startActivity(new Intent(BoardmapActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

	@Override
	public Activity getActivity() {
		return BoardmapActivity.this;
	}

	/**
	 * Called when an item in the navigation menu is selected.
	 *
	 * @param item The selected item
	 * @return true to display the item as the selected item
	 */
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		return false;
	}
}
