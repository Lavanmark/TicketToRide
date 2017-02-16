package com.floorcorn.tickettoride.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.PregamePresenter;
import com.floorcorn.tickettoride.ui.views.IPregameView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Joseph Hansen
 */

public class PregameActivity extends AppCompatActivity implements IPregameView {

    private PregamePresenter presenter;
    private RecyclerView playerListView;
    private Button cancelGameButton;
	private Button refreshButton;

	private PlayerListRecyclerViewAdapter playerListViewAdapter;
	private ScheduledExecutorService scheduledExecutorService;

    /**
     * Sets up the view components including the cancel/leave game button and the player list.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);
        setTitle(getTitleString(0,0));


        presenter = new PregamePresenter();
	    presenter.setView(this);

	    playerListView = (RecyclerView) findViewById(R.id.pregame_list);
	    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
	    mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
	    playerListView.setLayoutManager(mLinearLayoutManager);
	    playerListView.setAdapter(new PlayerListRecyclerViewAdapter(presenter.getPlayerList()));


        cancelGameButton = (Button) findViewById(R.id.cancelGameButton);

        if(presenter.isConductor())
            cancelGameButton.setText("Cancel Game");
        else
            cancelGameButton.setText("Leave Game");

        cancelGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cancelGame();
            }
        });

	    pollPlayerList();
    }

	@Override
	public void onStop () {
		if(scheduledExecutorService != null)
			scheduledExecutorService.shutdown();
		presenter.unregister();
		super.onStop();
	}

	private String getTitleString(int cur, int tot) {
		return "Waiting on Players... (" + cur + "/" + tot + ")";
	}
    /**
     * Sets the presenter to the argument if its the correct type. Will throw
     * IllegalArgumentException if presenter is not the correct type
     * @param presenter the presenter to interact with
     */
    @Override
    public void setPresenter(IPresenter presenter) {
        if (presenter instanceof PregamePresenter) {
            this.presenter = (PregamePresenter) presenter;
        } else {
            throw new IllegalArgumentException("Presenter arg was not a PregamePresenter");
        }
    }

    /**
     * Displays this game's players in the view.
     * @param players A Set of Player objects representing players in current game
     */
    @Override
    public void displayPlayerList(List<Player> players) {
	    playerListView = (RecyclerView) findViewById(R.id.pregame_list);
	    assert playerListView != null;
	    playerListViewAdapter = (PlayerListRecyclerViewAdapter) (playerListView).getAdapter();
	    playerListViewAdapter.swapList(players);
	    setTitle(getTitleString(players.size(), presenter.getGameSize()));
    }

    /**
     * Begins this game.
     */
    @Override
    public void startGame() {
        switchToBoardmapActivity();
    }

    /**
     * Switches to the Lobby view. It does not leave the game.
     */
    @Override
    public void switchToLobbyActivity() {
        // This needs to go back two views; the Boardmap makes the Pregame view and we want to
        // go back to before the Boardmap.
        // The FLAG_ACTIVITY_CLEAR_TOP tells Android that this activity is already running and that
        // we can go back to that one, popping/clearing the newer activities off the stack.
        startActivity(new Intent(PregameActivity.this, GameListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }

	@Override
	public void pollPlayerList() {
		scheduledExecutorService = Executors.newScheduledThreadPool(2);
		scheduledExecutorService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				PregameActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						presenter.requestPlayerList();
					}
				});
			}
		}, 0, 5, TimeUnit.SECONDS);
	}

    /**
     * Switches to the Boardmap view. This happens when the game is started and we don't need
     * Pregame view anymore.
     */
    @Override
    public void switchToBoardmapActivity() {
        this.finish();
    }

    /**
     * Displays a message in a Toast.
     * @param message String to display
     */
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

	@Override
	public void backToLogin() {
		startActivity(new Intent(PregameActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	public class PlayerListRecyclerViewAdapter extends RecyclerView.Adapter<PlayerListRecyclerViewAdapter.ViewHolder> {
		List<Player> players;

		PlayerListRecyclerViewAdapter(List<Player> items) {
			players = new ArrayList<>(items);
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.pregame_list_item, parent, false);
			return new ViewHolder(view);
		}

		void swapList(List<Player> list) {
			players.clear();
			players.addAll(list);
			notifyDataSetChanged();
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			holder.mItem = (Player) players.get(position);
			if(holder.mItem == null)
				return;
			holder.mNameView.setText(holder.mItem.getName());
			holder.mColorView.setText(String.valueOf(holder.mItem.getColor()));
		}

		@Override
		public int getItemCount() {
			return players.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			final View mView;
			final TextView mNameView;
			final TextView mColorView;
			Player mItem;

			ViewHolder(View view) {
				super(view);
				mView = view;
				mNameView = (TextView) view.findViewById(R.id.playerListNameText);
				mColorView = (TextView) view.findViewById(R.id.playerListColorText);
				view.setClickable(false);
			}

			@Override
			public String toString() {
				return super.toString() + " '" + mNameView.getText() + "'";
			}
		}
	}
}
