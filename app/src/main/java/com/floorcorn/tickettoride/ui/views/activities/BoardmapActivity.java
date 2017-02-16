package com.floorcorn.tickettoride.ui.views.activities;

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
        System.out.println("BoardmapActivity:OnCreate");

        presenter = new BoardmapPresenter();

	    Toolbar mToolbar = (Toolbar) findViewById(R.id.bmap_toolbar);
	    setSupportActionBar(mToolbar);
	    if(getSupportActionBar() != null)
	        getSupportActionBar().setTitle(presenter.getGameName());

        if(!presenter.gameInProgress()) {
	        ((TextView)findViewById(R.id.gameStartedText)).setText("Waiting on Players");
			launchPreGame();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(presenter.getGameName());
        }
        if(!presenter.gameInProgress()) {
            getSupportActionBar().setTitle("Waiting on Players...");
        } else {
            getSupportActionBar().setTitle("Game Started!");
        }
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
	public void gameStarted() {
		((TextView)findViewById(R.id.gameStartedText)).setText("Game Started");
	}

	@Override
    public void backToLogin() {
	    startActivity(new Intent(BoardmapActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}
