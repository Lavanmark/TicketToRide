package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.GameOverPresenter;
import com.floorcorn.tickettoride.ui.presenters.IGameOverPresenter;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.IGameOverView;

import java.util.List;

/**
 * Created by Tyler on 3/16/2017.
 */

public class GameOverActivity extends AppCompatActivity implements IGameOverView {

	private IGameOverPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_over);
		presenter = new GameOverPresenter();
		presenter.setView(this);

		setPlayerList(presenter.getPlayerList());
		setWinnerText(presenter.getWinner().getName());
	}

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void setPresenter(IPresenter presenter) {
		if(presenter instanceof IGameOverPresenter)
			this.presenter = (IGameOverPresenter) presenter;
	}


	public void setPlayerList(List<Player> players) {
		LinearLayout ll = (LinearLayout) findViewById(R.id.gameOverPlayerList);
		ll.removeAllViews();
		for(Player p : players) {
			LinearLayout horlay = new LinearLayout(ll.getContext());
			horlay.setOrientation(LinearLayout.HORIZONTAL);
			TextView nameView = new TextView(ll.getContext());
			nameView.setText(p.getName());
			TextView scoreView = new TextView(ll.getContext());
			scoreView.setText(String.valueOf(p.getScore()));
			horlay.addView(nameView);
			horlay.addView(scoreView);
			ll.addView(horlay);
		}
	}//beginnings 

	private void setWinnerText(String playerName) {
		TextView view = (TextView)findViewById(R.id.winnerTextField);
		view.setText(playerName + " has Won!");
	}

	@Override
	public void backToLogin() {
		//TODO
	}

	@Override
	public Activity getActivity() {
		return this.getActivity();
	}
}
