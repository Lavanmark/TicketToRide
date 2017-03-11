package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This BoardmapActivity is an Android Activity that displays all the game board stuff. It has
 * representations of the train car cards and the destination tickets, the players, the routes,
 * the map, etc. In addition to typical Android stuff, it implements IBoardmapView.
 *
 * @invariant to start one of these, the user is logged in
 * @invariant 0 < numPlayersIn(game) <= size(game)
 */
public class BoardmapActivity extends AppCompatActivity implements IBoardmapView, NavigationView.OnNavigationItemSelectedListener   {

	/**
	 * The presenter that controls this view.
	 */
	BoardmapPresenter presenter = null;

	/**
	 * The constant value for max number of destinations.
	 */
	private final int MAXDESTINATIONS = 3;
	/**
	 * The constant value for max number of face up cards.
	 */
	private final int MAXFACEUP = 5;
	/**
	 * The Board is the model representation of the entire board, including the list of routes and
	 * the decks of cards (implemented through a DeckManager).
	 */
	private Board boardmap;

	/**
	 * Button used to show animation; only used for Phase 2 pass off.
 	 */
	private Button animateButton;
	/**
	 * Boolean used to prevent animation when it shouldn't be available.
	 * TODO: replace this with State pattern in Phase 3
	 */
	private boolean canAnimate = true;

	/**
	 * Button used to open the drawer that shows cards to draw.
	 */
	private Button drawCardsButton;
	/**
	 * Button used to open the drawer that shows destination tickets to draw.
	 */
	private Button drawDestinationTicketsButton;
	/**
	 * Button used to open the drawer that shows routes to claim.
	 */
	private Button claimRouteButton;
	/**
	 * Button used to open the drawer that shows the player's hand.
	 */
	private Button displayHandButton;

	/**
	 * Button used to draw from the deck of destination tickets.
	 */
	private Button drawFromDestinationDeck;
	/**
	 * Button used to keep selected destination tickets.
	 */
	private Button keepDestinations;
	/**
	 * Button(s) used to signify destination tickets.
	 */
	private ImageButton destinationTickets[] = new ImageButton[MAXDESTINATIONS];

	/**
	 * Button used to draw train car card from deck.
	 */
	private Button drawFromCardDeck;
	/**
	 * Button(s) used to signify face up train car cards.
	 */
	private ImageButton faceupCards[] = new ImageButton[MAXFACEUP];

	/**
	 * RecyclerView that shows all of the routes (which can easily be loaded from different CSVs
	 * for different maps... nice since we're doing Ticket to Mars instead of Ticket to Ride).
	 */
	private RecyclerView routeRecyclerView;
	/**
	 * Adapter for the route RecyclerView.
	 */
	private RouteRecyclerViewAdapter routeAdapter;

	/**
	 * TextView showing number of red cards in the player's hand.
	 */
	private TextView redCount;
	/**
	 * TextView showing number of orange cards in the player's hand.
	 */
	private TextView orangeCount;
	/**
	 * TextView showing number of yellow cards in the player's hand.
	 */
	private TextView yellowCount;
	/**
	 * TextView showing number of green cards in the player's hand.
	 */
	private TextView greenCount;
	/**
	 * TextView showing number of blue cards in the player's hand.
	 */
	private TextView blueCount;
	/**
	 * TextView showing number of purple cards in the player's hand.
	 */
	private TextView purpleCount;
	/**
	 * TextView showing number of black cards in the player's hand.
	 */
	private TextView blackCount;
	/**
	 * TextView showing number of white cards in the player's hand.
	 */
	private TextView whiteCount;
	/**
	 * TextView showing number of wild cards in the player's hand.
	 */
	private TextView wildCount;

	/**
	 * TextView showing the train count for the player.
	 */
	private TextView trainCount;

	/**
	 * LinearLayout showing the destination tickets.
	 */
	private LinearLayout destinationTicketHolder;

	/**
	 * LinearLayout that shows the players. The players are distinguishable by name and color
	 * and are clickable to see public information such as their score.
	 */
	private LinearLayout playerIcons;

	/**
	 * LinearLayout that holds chat stuff.
	 */
	private LinearLayout chatLayout;
	/**
	 * Button for sending chat messages.
	 */
	private Button sendMessageBut;
	/**
	 * EditText text field for composing chat messages.
	 */
	private EditText chatTextField;

	/**
	 * Initial set up for all of the activity's UI elements.
	 *
	 * @pre User is logged in
	 * @pre User has selected from the game list view one of the game's he has joined
	 * @pre 0 < numPlayersIn(game) < size(game) || numPlayersIn(game) == size(game)
	 * @post This Android Activity is created with all of its UI elements.
	 * @post Mouse click listeners are added to all of the buttons.
	 * @post if numPlayersin(game) != size(game): start PregameActivity
	 * @param savedInstanceState
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

		// Initialize UI element variables.

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

		destinationTicketHolder = (LinearLayout)findViewById(R.id.destinationHolder);

		// Set up chat stuff.

		final ScrollView scrollView = (ScrollView)findViewById(R.id.chatScroll);
		scrollView.post(new Runnable() {
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
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

		// More UI set up (drawers and drawer-open button click listeners).

		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
		drawDestinationTicketsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Corn.log("Opening Destination Drawer");
				presenter.displayDestinationCardDrawer(DRAWER, DRAWER_HOLDER);
			}
		});
		drawCardsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.displayDrawDrawer(DRAWER, DRAWER_HOLDER);
			}
		});
		claimRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.displayPlaceRouteDrawer(DRAWER, DRAWER_HOLDER);
			}
		});
		displayHandButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				DRAWER.openDrawer(GravityCompat.END); // Gravity...End is on the right side
				//TODO maybe this needs its own function to get all this information set up.
			}
		});
		animateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				animateButton.setEnabled(false);
				canAnimate = false;
				presenter.animate();
			}
		});

		// Set player icons to default "blank."

		playerIcons = (LinearLayout)findViewById(R.id.playerTokenHolder);
		for(int i = 0; i < presenter.getGameSize(); i++) {
			Button but = new Button(playerIcons.getContext());
			but.setText("PLAYER " + i);
			but.setTextColor(Color.WHITE);
			but.setBackgroundColor(Color.GRAY);
			but.setPadding(20, 0, 20, 0);
			playerIcons.addView(but);
		}

		checkStarted();
		if(!presenter.gameInProgress())
			launchPregame();
	}

	/** These next few methods serve the purposes of the animation and are not needed after that. **/

	@Override
	public void displayDrawingDeckDrawer(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
		Corn.log("Opening Drawing Drawer");
		presenter.displayDrawDrawer(DRAWER, DRAWER_HOLDER);
	}

	@Override
	public void displayDestinationCardDrawer(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
		Corn.log("Opening Destination Drawer");
		presenter.displayDestinationCardDrawer(DRAWER, DRAWER_HOLDER);
	}

	@Override
	public void displayHandDrawer(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
		DRAWER.openDrawer(GravityCompat.END);
	}

	@Override
	public void hideHandDrawer(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);
		DRAWER.closeDrawer(GravityCompat.END);
	}

	private boolean drawDrawerIsOpen(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		if(DRAWER.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) findViewById(R.id.drawer_draw_cards);
			if(tempFrame != null){
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean destinationDrawerIsOpen(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		if(DRAWER.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) findViewById(R.id.drawer_destinations);
			if(tempFrame != null){
				return true;
			}
			return false;
		}
		return false;
	}

	private boolean routeDrawerIsOpen() {
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		if(DRAWER.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) findViewById(R.id.drawer_place_routes);
			if(tempFrame != null){
				return true;
			}
			return false;
		}
		return false;
	}

	/**
	 * Unregisters the presenter (Observer pattern) and stops the poller.
	 *
	 * @pre Activity has been created
	 * @pre presenter != null
	 * @pre the user's screen focus is changing (he left the activity or Android going to sleep,
	 * 		etc)
	 * @post presenter unregistered from observing client model
	 * @post presenter is told to stop polling
	 */
	@Override
	public void onStop(){
		presenter.unregister();
		presenter.stopPolling();
		super.onStop();
	}

	/**
	 * Does super.onResume and checks if the game is started.
	 *
	 * @pre he user's screen focus is coming back (not creating the Activity but resuming)
	 * @post call checkStarted() to check if game has started, which may start polling or change
	 * 		other state things
	 */
	@Override
	protected void onResume(){
		super.onResume();
		checkStarted();
	}

	/**
	 * Starts the PregameActivity, meaning the waiting screen that is shown until enough players
	 * join.
	 *
	 * @pre 0 < numPlayersIn(game) < size(game)
	 * @post PregameActivity launched with a new Android Intent (basically a waiting screen)
	 */
	public void launchPregame() {
		startActivity(new Intent(BoardmapActivity.this, PregameActivity.class));
	}

	/**
	 * Sets the presenter. Throws an IllegalArgumentException if the object (implementing
	 * IPresenter) is not of type BoardmapPresenter.
	 *
	 * @param presenter the presenter to interact with, implements IPresenter
	 * @pre presenter implements IPresenter
	 * @pre presenter instanceof BoardmapPresenter
	 * @post presenter field for this BoardmapActivity object is set as presenter
	 */
	@Override
	public void setPresenter(IPresenter presenter) {
		if (presenter instanceof BoardmapPresenter)
			this.presenter = (BoardmapPresenter)presenter;
		else
			throw new IllegalArgumentException();
	}

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
	@Override
	public void checkStarted() {
		if(!presenter.gameInProgress()) {
			// Note: User can click out of the Pregame Activity. It's ok because we have disabled
			// buttons, but maybe in a later version we won't want that to be possible.
			drawDestinationTicketsButton.setEnabled(false);
			displayHandButton.setEnabled(false);
			claimRouteButton.setEnabled(false);
			drawCardsButton.setEnabled(false);
			sendMessageBut.setEnabled(false);
			animateButton.setEnabled(false);
		} else {
			presenter.startPollingCommands();
			drawDestinationTicketsButton.setEnabled(true);
			drawCardsButton.setEnabled(true);
			claimRouteButton.setEnabled(true);
			displayHandButton.setEnabled(true);
			sendMessageBut.setEnabled(true);
			animateButton.setEnabled(canAnimate);
			setupPlayerIcons();
		}
	}

	/**
	 * Set up player icons once there are enough players to start game / fill all spots.
	 *
	 * @pre numPlayersIn(game) == size(game)
	 * @post player icons at top of screen are not default, empty, gray boxes
	 * @post each player icon corresponds to the player in that position of the player list (first
	 * 		to have a turn is listed first, second is second, etc)
	 * @post each player icon has the background color of the player's chosen color
	 * @post each player icon's text is that player's name
	 * @post click listeners are added to each player icon to display player info on click via
	 * 		Android Snackbar (calls player.getCriticalPlayerInfo() to retrieve info)
	 * @post if isTurn(playerOf(aPlayerIcon)): player icon's text color == black
	 * @post if !isTurn(playerOf(aPlayerIcon)): player icon's text color == white
	 */
	private void setupPlayerIcons() {
		ArrayList<Player> players = presenter.getPlayers();
		for(final Player p : players) {
			Button button = (Button)playerIcons.getChildAt(p.getPlayerID());
			if(p.isTurn())
				button.setTextColor(Color.BLACK);
			else
				button.setTextColor(Color.WHITE);
			button.setText(p.getName());
			button.setBackgroundColor(getPlayerColor(p.getColor()));
			// TODO (future phases) check if the player is self. If so it should open the drawer.
			button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO (future phases) might need to update on every player list because p is final
					Snackbar snackbar = Snackbar.make(playerIcons, p.getCriticalPlayerInfo(), Snackbar.LENGTH_LONG);
					((TextView)snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(6);
					snackbar.show();
				}
			});
		}
	}

	/**
	 * Matches and returns the color we're using in the Activity to the player's color. If the
	 * value passed in is not one of the PlayerColors (RED, GREEN, BLUE, YELLOW, BLACK) this
	 * will return the color white.
	 * @pre pc param is a valid PlayerColor
	 * @post returns a valid color for use in Android UI stuff
	 * @param pc PlayerColor value (their "game piece")
	 * @return the color saved in Android resource (R.color. ...)
	 */
	private int getPlayerColor(PlayerColor pc) {

		if (pc == null)
			return ContextCompat.getColor(getBaseContext(), R.color.colorNotAPlayer);

		int val;
		switch(pc) {
			case RED:
				val = R.color.colorRedPlayer;
				break;
			case GREEN:
				val = R.color.colorGreenPlayer;
				break;
			case BLUE:
				val = R.color.colorBluePlayer;
				break;
			case YELLOW:
				val = R.color.colorYellowPlayer;
				break;
			case BLACK:
				val = R.color.colorBlackPlayer;
				break;
			default:
				val = R.color.colorNotAPlayer;
		}
		return ContextCompat.getColor(getBaseContext(), val);
	}

	/**
	 * Sets the visible chat log.
	 * @param log GameChatLog object
	 */
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

		// TODO (future phases) this line could be separated, put in a diff function?
		trainCount.setText(String.valueOf(presenter.getTrainCars()));
	}

	/**
	 * This is in the player hand.
	 * @param destinationCardList
	 */
	@Override
	public void setPlayerDestinationCardList(List<DestinationCard> destinationCardList) {
		destinationTicketHolder.removeAllViews();
		for(DestinationCard destinationCard : destinationCardList) {
			TextView textView = new TextView(destinationTicketHolder.getContext());
			String s = destinationCard.toString();
			textView.setText(s);
			destinationTicketHolder.addView(textView);
		}
	}

	@Override
	public void setClaimRoutesList(List<Route> routes) {
		if(routeDrawerIsOpen())
			routeAdapter.swapList(routes);
	}

	@Override
	public void setFaceUpTrainCards() {
		if(drawDrawerIsOpen())
			setFaceupImages();
	}

	@Override
	public void setDestinationCardChoices() {
		if(destinationDrawerIsOpen())
			buildDestinationDrawer();
	}

	/**
	 * Sets the visible, drawable cards.
	 */
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
		displayLeftDrawer(R.layout.drawer_draw_cards, DRAWER, DRAWER_HOLDER);
		drawFromCardDeck = (Button)findViewById(R.id.draw_from_card_deck);

		setFaceupImages();

		drawFromCardDeck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				presenter.drawTrainCardFromDeck();
			}
		});
		for(int i = 0; i < MAXFACEUP; i++){
			final int temp = i;
			faceupCards[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					presenter.drawFromFaceUp(temp);
				}
			});
		}

	}

	@Override
	public void hideDrawingDeckDrawer() {
		closeLeftDrawer();
	}

	private void setDestinationImages(){
		destinationTickets[0] = (ImageButton)findViewById(R.id.dest_card1);
		destinationTickets[1] = (ImageButton)findViewById(R.id.dest_card2);
		destinationTickets[2] = (ImageButton)findViewById(R.id.dest_card3);

		int[] imageId;
		try {
			imageId = presenter.getDiscardableDestinationCards();
			if(imageId == null)
				imageId = new int[]{R.drawable.back_destinations, R.drawable.back_destinations, R.drawable.back_destinations};
			for(int i = 0; i < 3; i++) {
				destinationTickets[i].setSelected(false);
				if(i >= imageId.length) {
					destinationTickets[i].setBackgroundResource(R.drawable.back_destinations);
					destinationTickets[i].setClickable(false);
				} else {
					destinationTickets[i].setImageResource(imageId[i]);
					if(imageId[i] == R.drawable.back_destinations)
						destinationTickets[i].setClickable(false);
					else
						destinationTickets[i].setClickable(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Displays the destination card drawer.
	 * @param DRAWER The layout of the Boardmap Activity
	 * @param DRAWER_HOLDER The layout of the frame that opens the drawer
	 */
	@Override
	public void displayDestinationCardDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
		displayLeftDrawer(R.layout.drawer_destinations, DRAWER, DRAWER_HOLDER);

		buildDestinationDrawer();
	}

	/**
	 * Sets up the destination cards drawer. (High level functionality needs: there are a few
	 * destination cards and user has to choose which to keep. These UI elements help with that.)
	 */
	private void buildDestinationDrawer() {
		drawFromDestinationDeck = (Button) findViewById(R.id.draw_from_dest_deck);
		drawFromDestinationDeck.setEnabled(false);
		keepDestinations = (Button) findViewById(R.id.keepCards);
		keepDestinations.setEnabled(false);

		setDestinationImages();

		if (presenter.getDiscardableCount() == 0) {
			presenter.setDiscarding(false);
			drawFromDestinationDeck.setEnabled(true);
			drawFromDestinationDeck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawFromDestinationDeck.setEnabled(false);
					presenter.drawNewDestinationCards();
				}
			});
		} else {
			presenter.setDiscarding(true);

			// Set images.
			for(int i = 0; i < MAXDESTINATIONS; i++) {
				destinationTickets[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setSelected(!v.isSelected());
						int notSelectedCount = 0;
						for(ImageButton ib : destinationTickets)
							if(!ib.isSelected())
								notSelectedCount++;
						if(notSelectedCount <= presenter.getDiscardableCount())
							keepDestinations.setEnabled(true);
						else
							keepDestinations.setEnabled(false);
					}
				});
			}

			// Setup "keep button."
			keepDestinations.setEnabled(false);
			keepDestinations.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for(int i = 0; i < MAXDESTINATIONS; i++) {
						if(!destinationTickets[i].isSelected() && destinationTickets[i].isClickable())
							presenter.discardDestination(i);
					}
					presenter.doneDiscarding();
				}
			});
		}
	}

	@Override
	public void hideDestinationDrawer() {
		closeLeftDrawer();
	}

	@Override
	public void displayClaimRouteDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
		displayLeftDrawer(R.layout.drawer_place_routes, DRAWER, DRAWER_HOLDER);
		routeRecyclerView = (RecyclerView) findViewById(R.id.route_recycler);
		assert routeRecyclerView != null;
		List<Route> routes = presenter.getRoutes();
		setupRecyclerView((RecyclerView) routeRecyclerView, routes);
	}

	/**
	 * This sets up the Recycler view with an adapter.
	 *
	 * @param recyclerView
	 */
	private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Route> routes) {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setAdapter(new RouteRecyclerViewAdapter(routes));
		assert recyclerView.getAdapter() != null;
		routeAdapter = (RouteRecyclerViewAdapter) recyclerView.getAdapter();
	}

	@Override
	public void hideRouteDrawer() {
		closeLeftDrawer();
	}

	private void closeLeftDrawer(){
		final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
		DRAWER.closeDrawer(GravityCompat.START);
	}

	private void displayLeftDrawer(int drawerID, DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER){
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(drawerID, null);
		DRAWER_HOLDER.removeAllViews();
		DRAWER_HOLDER.addView(layout);
		DRAWER.openDrawer(GravityCompat.START);
		Corn.log("Left drawer opened");
	}

	/**
	 * Sends user back to login activity. Uses FLAG_ACTIVITY_CLEAR_TOP to clear the activities above
	 * it in the stack.
	 */
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

	@Override
	public void animate_ClickOnDestinationCards(){
		this.destinationTickets[0].performClick();
		this.destinationTickets[1].performClick();
	}

	@Override
	public void animate_takeDestinationCards(){
		this.keepDestinations.performClick();
	}

	@Override
	public void animate_showOtherPlayerInfo(){
		this.playerIcons.getChildAt(presenter.getPlayers().get(1).getPlayerID()).performClick();
	}

	@Override
	public void animate_clickDrawDestination(){
		this.drawDestinationTicketsButton.performClick();
	}

	@Override
	public void animate_clickDrawDestinationDeck(){
		this.drawFromDestinationDeck.performClick();
	}

	@Override
	public void animate_clickOpenRouteDrawer(){
		this.claimRouteButton.performClick();
	}

	@Override
	public void animate_clickClaimRoute(){
		if(routeAdapter == null) {
			Toast.makeText(this, "Could not find list of routes! Reopen the game!", Toast.LENGTH_SHORT).show();
			return;
		}
		presenter.fakeClaimButtonClicked();
	}

	/**
	 * This is for showing all the routes. It shows a grid, each row corresponding to a route.
	 */
	public class RouteRecyclerViewAdapter
			extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {

		public List<Route> routes;

		RouteRecyclerViewAdapter(List<Route> routes) {
			this.routes = new ArrayList<>(routes);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			//View view = new TextView(parent.getContext());
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.claim_list_content, parent, false);
			ViewHolder viewHolder = new ViewHolder(view);
			return viewHolder;
		}

		void swapList(List<Route> list) {
			this.routes.clear();
			this.routes.addAll(list);
			notifyDataSetChanged();
		}

		class ViewHolder extends RecyclerView.ViewHolder {

			public LinearLayout itemLayout;
			public TextView city1;
			public TextView city2;
			public TextView routeColor;
			public TextView routeLength;
			public Button claimButton;
			public TextView owner;

			ViewHolder(View itemView) {
				super(itemView);
				itemLayout = (LinearLayout) itemView;
				city1 = (TextView)itemLayout.getRootView().findViewById(R.id.city1);
				city2 = (TextView)itemLayout.getRootView().findViewById(R.id.city2);
				routeColor = (TextView)itemLayout.getRootView().findViewById(R.id.color);
				routeLength = (TextView)itemLayout.getRootView().findViewById(R.id.length);
				claimButton = (Button)itemLayout.getRootView().findViewById(R.id.claimButton);
				owner = (TextView)itemLayout.getRootView().findViewById(R.id.routeOwner);
			}
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			final Route r = routes.get(position);
			holder.city1.setText(r.getFirstCity().getName());
			holder.city2.setText(r.getSecondCity().getName());
			holder.routeColor.setText(r.getColor().toString());
			holder.routeLength.setText(String.valueOf(r.getLength()));
			if (presenter.canClaim(r))
				holder.claimButton.setEnabled(true);
			else
				holder.claimButton.setEnabled(false);
			holder.claimButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					presenter.claimButtonClicked(r, presenter.getPlayers().get(0));
				}
			});

			// Set some information (name and color) for the person who owns the route.
			holder.owner.setTextColor(0xff000000);
			holder.owner.setText(presenter.getPlayerName(r.getOwner()));
			// This will reset the background color to white (important because a RecyclerView
			// recycles its elements) if the route is unclaimed, or it will set the background color
			// to match the player that owns it.
			PlayerColor pc = presenter.getPlayerColor(r.getOwner());
			holder.itemView.setBackgroundColor(getPlayerColor(pc));
		}

		/**
		 * Returns the total number of items in the data set held by the adapter.
		 *
		 * @return The total number of items in this adapter.
		 */
		@Override
		public int getItemCount() {
			return this.routes.size();
		}
	}

}