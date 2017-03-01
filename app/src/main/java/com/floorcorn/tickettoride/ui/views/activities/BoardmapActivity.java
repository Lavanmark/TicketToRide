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
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

public class BoardmapActivity extends AppCompatActivity implements IBoardmapView, NavigationView.OnNavigationItemSelectedListener   {

	BoardmapPresenter presenter = null;


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

		final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.boardmapActivity);
		final FrameLayout drawer_holder = (FrameLayout) findViewById(R.id.nav_view);

		Button mButton1 = (Button)findViewById(R.id.open_dest_button);
		mButton1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.drawer_destinations, null);
				drawer_holder.removeAllViews();
				drawer_holder.addView(layout);
				drawer.openDrawer(GravityCompat.START);
			}
		});

		Button mButton2 = (Button)findViewById(R.id.open_card_button);
		mButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.drawer_draw_cards, null);
				drawer_holder.removeAllViews();
				drawer_holder.addView(layout);
				drawer.openDrawer(GravityCompat.START);
			}
		});



		Button mButton3 = (Button)findViewById(R.id.open_route_button);
		mButton3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.drawer_place_routes, null);
				drawer_holder.removeAllViews();
				drawer_holder.addView(layout);
				drawer.openDrawer(GravityCompat.START);
			}
		});

		Button mButton4 = (Button)findViewById(R.id.open_hand_button);
		mButton4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				drawer.openDrawer(GravityCompat.END);
			}
		});






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
