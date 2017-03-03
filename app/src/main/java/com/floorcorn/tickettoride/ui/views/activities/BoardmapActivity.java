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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.Route;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

import java.util.ArrayList;
import java.util.Set;

public class BoardmapActivity extends AppCompatActivity implements IBoardmapView, NavigationView.OnNavigationItemSelectedListener   {

	BoardmapPresenter presenter = null;
	private Board boardmap;

	//button used to show animation
	private Button animate;

	// buttons used to open the drawers
	private Button drawCardsButton;
	private Button drawDestinationTicketsButton;
	private Button claimRouteButton;
	private Button displayHandButton;

	//elements related to Draw Destination Tickets Drawer
	private Button drawFromDestinationDeck;
	private ImageButton destTicket1;
	private ImageButton destTicket2;
	private ImageButton destTicket3;


	//elements related to Draw Cards Drawer
	private Button drawFromCardDeck;
	private ImageButton faceup1;
	private ImageButton faceup2;
	private ImageButton faceup3;
	private ImageButton faceup4;
	private ImageButton faceup5;

	
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

	private TextView trainCount;

	//elements related to the PlayerStatus/Turn Icons


	//elements related to the map



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

	    checkStarted();
        if(!presenter.gameInProgress()) {
	        launchPreGame();
        }
		else{
			final DrawerLayout DRAWER = (DrawerLayout) findViewById(R.id.boardmapActivity);
			final FrameLayout DRAWER_HOLDER = (FrameLayout) findViewById(R.id.left_drawer_holder);

			drawDestinationTicketsButton = (Button)findViewById(R.id.open_dest_button);
			drawDestinationTicketsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					displayDestinationCardDrawer(DRAWER, DRAWER_HOLDER);

				}
			});

			drawCardsButton = (Button)findViewById(R.id.open_card_button);
			drawCardsButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					displayDrawingDeckDrawer(DRAWER, DRAWER_HOLDER);

				}
			});



			claimRouteButton = (Button)findViewById(R.id.open_route_button);
			claimRouteButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					displayDrawingDeckDrawer(DRAWER, DRAWER_HOLDER);
				}
			});

			displayHandButton = (Button)findViewById(R.id.open_hand_button);
			displayHandButton.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					DRAWER.openDrawer(GravityCompat.END); //Gravity End is on the right side
				}
			});

		}








    }

	@Override
	public void onStop() {
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
			((TextView)findViewById(R.id.gameStartedText)).setText("Waiting on Players...");
		} else {
			presenter.startPollingCommands();
			((TextView)findViewById(R.id.gameStartedText)).setText("Game Started!");
		}
	}

	@Override
	public void setBoard(Board board) {

	}

	@Override
	public void setPlayerTrainCardList(ArrayList<TrainCard> trainCardList) {

	}

	@Override
	public void setPlayerDestinationCardList(Set<DestinationCard> destinationCardList) {

	}

	@Override
	public void setFaceUpTrainCards(ArrayList<TrainCard> faceUpTrainCards) {

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

	@Override
	public void displayDrawingDeckDrawer(DrawerLayout DRAWER, FrameLayout DRAWER_HOLDER) {
		displayLeftDrawer(R.layout.drawer_draw_cards, DRAWER, DRAWER_HOLDER);
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
