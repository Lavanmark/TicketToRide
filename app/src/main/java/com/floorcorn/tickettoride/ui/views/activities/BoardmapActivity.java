package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

public class BoardmapActivity extends AppCompatActivity implements IBoardmapView {

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
}
