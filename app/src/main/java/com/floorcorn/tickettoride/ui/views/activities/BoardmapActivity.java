package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCardColor;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;
import com.floorcorn.tickettoride.ui.views.drawers.ClaimRouteDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.DestinationDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.HandDrawer;
import com.floorcorn.tickettoride.ui.views.drawers.TrainCardDrawer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This BoardmapActivity is an Android Activity that displays all the game board stuff. It has
 * representations of the train car cards and the destination tickets, the players, the routes,
 * the map, etc. In addition to typical Android stuff, it implements IBoardmapView.
 *
 * @invariant to start one of these, the user is logged in
 * @invariant 0 < numPlayersIn(game) <= size(game)
 */
public class BoardmapActivity extends AppCompatActivity implements IBoardmapView  {

	/**
	 * The presenter that controls this view.
	 */
	BoardmapPresenter presenter = null;
	
	/**
	 * Buttons used to open respective drawers.
	 */
	private Button drawCardsButton;
	private Button drawDestinationTicketsButton;
	private Button claimRouteButton;
	private Button displayHandButton;

	/**
	 * LinearLayout that shows the players. The players are distinguishable by name and color
	 * and are clickable to see public information such as their score.
	 */
	private LinearLayout playerIcons;
	
	/**
	 * Different drawer objects
	 */
	private HandDrawer handDrawer;
	private ClaimRouteDrawer claimRouteDrawer;
	private TrainCardDrawer trainCardDrawer;
	private DestinationDrawer destinationDrawer;



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

		
		handDrawer = new HandDrawer(this, presenter);
		claimRouteDrawer = new ClaimRouteDrawer(this, presenter);
		trainCardDrawer = new TrainCardDrawer(this, presenter);
		destinationDrawer = new DestinationDrawer(this, presenter);

		drawDestinationTicketsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.tryOpenDestinationDrawer();
			}
		});
		drawCardsButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.tryOpenDrawTrainDrawer();
			}
		});
		claimRouteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				presenter.tryOpenClaimRouteDrawer();
			}
		});
		displayHandButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handDrawer.open();
			}
		});

		// Set player icons to default "blank."
		initializePlayerIcons();

		if(checkStarted())
			presenter.startPollingCommands();
		if(!presenter.gameInProgress())
			launchPregame();
		if(presenter.gameFinished())
			showGameOver();
		//TODO we will want something to launch the game over when it happens in the game.
	}

	@Override
	public void lockDrawerClosed(){
		DrawerLayout BM_DRAWER_LAYOUT = (DrawerLayout) findViewById(R.id.boardmapActivity);
		BM_DRAWER_LAYOUT.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
		if(checkStarted())
			presenter.startPollingCommands();
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
	
	private void initializePlayerIcons() {
		//TODO why do we have this?
		playerIcons = (LinearLayout)findViewById(R.id.playerTokenHolder);
		for(int i = 0; i < presenter.getGameSize(); i++) {
			Button but = new Button(playerIcons.getContext());
			but.setText("PLAYER " + i);
			but.setTextColor(Color.WHITE);
			but.setBackgroundColor(Color.GRAY);
			but.setPadding(20, 0, 20, 0);
			playerIcons.addView(but);
		}
	}

	/**
	 * Matches and returns the color we're using in the Activity to the player's color. If the
	 * value passed in is not one of the PlayerColors (RED, GREEN, BLUE, YELLOW, BLACK) this
	 * will return the color white.
	 *
	 * @pre pc param is a valid PlayerColor
	 * @post returns a valid color for use in Android UI stuff
	 * @param pc PlayerColor value (their "game piece")
	 * @return the color saved in Android resource (R.color. ...)
	 */
	public int getPlayerColor(PlayerColor pc) {
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
	 * Starts the PregameActivity, meaning the waiting screen that is shown until enough players
	 * join.
	 *
	 * @pre 0 < numPlayersIn(game) < size(game)
	 * @post PregameActivity launched with a new Android Intent (basically a waiting screen)
	 */
	private void launchPregame() {
		startActivity(new Intent(BoardmapActivity.this, PregameActivity.class));
	}
	
	private void showGameOver() {
		startActivity(new Intent(BoardmapActivity.this, GameOverActivity.class));
	}
	
	private void returnToGameList() {
		//TODO here incase we need it...
		startActivity(new Intent(BoardmapActivity.this, GameListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
	
	@Override
	public boolean checkStarted() {
		if(!presenter.gameInProgress()) {
			// Note: User can click out of the Pregame Activity. It's ok because we have disabled
			// buttons, but maybe in a later version we won't want that to be possible.
			drawDestinationTicketsButton.setEnabled(false);
			displayHandButton.setEnabled(false);
			claimRouteButton.setEnabled(false);
			drawCardsButton.setEnabled(false);
			return false;
		} else {
			//LET the states enable these buttons
			setupPlayerIcons();
			return true;
		}
	}
	
	@Override
	public void setChatLog(GameChatLog log) {
		handDrawer.displayChatLog(log);
	}
	
	@Override
	public void setPlayerTrainCardList(Map<TrainCardColor, Integer> cards) {
		handDrawer.displayTrainCards(cards);
		handDrawer.displayTrainCarsLeft(presenter.getTrainCars());
	}
	
	@Override
	public void setPlayerDestinationCardList(List<DestinationCard> destinationCardList) {
		handDrawer.displayDestinationCards(destinationCardList);
	}
	
	@Override
	public void setClaimRoutesList(List<Route> routes) {
		claimRouteDrawer.setList(routes);
	}
	
	@Override
	public void setFaceUpTrainCards() {
		trainCardDrawer.updateFaceUp();
	}
	
	@Override
	public void setDestinationCardChoices() {
		destinationDrawer.updateDestinations();
	}
	
	@Override
	public void backToLogin() {
		startActivity(new Intent(BoardmapActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}
	
	@Override
	public Activity getActivity() {
		return BoardmapActivity.this;
	}
	
	@Override
	public void setPresenter(IPresenter presenter) {
		if (presenter instanceof BoardmapPresenter)
			this.presenter = (BoardmapPresenter)presenter;
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void enableClaimRouteButton(boolean enabled){
		claimRouteButton.setEnabled(enabled);
	}

	@Override
	public void enableTrainCardButton(boolean enabled){
		drawCardsButton.setEnabled(enabled);
	}

	@Override
	public void enableDestinationCardButton(boolean enabled){
		drawDestinationTicketsButton.setEnabled(enabled);
	}

	@Override
	public ClaimRouteDrawer getClaimRouteDrawer() {
		return claimRouteDrawer;
	}

	@Override
	public TrainCardDrawer getTrainCardDrawer() {
		return trainCardDrawer;
	}

	@Override
	public DestinationDrawer getDestinationDrawer() {
		return destinationDrawer;
	}

}