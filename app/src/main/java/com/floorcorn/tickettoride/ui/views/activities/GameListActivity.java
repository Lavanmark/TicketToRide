package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.LobbyPresenter;
import com.floorcorn.tickettoride.ui.views.GameListContent;
import com.floorcorn.tickettoride.ui.views.ILobbyView;
import com.floorcorn.tickettoride.ui.views.fragments.GameDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * An activity representing a list of Games. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link GameDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class GameListActivity extends AppCompatActivity implements ILobbyView {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;

	static final int CREATE_GAME_REQUEST = 1;
	static final int JOIN_GAME_REQUEST = 2;

	//model references
	private PlayerColor playerColor;
	private int playerNumber;
	private String gameName;
	private int gameID;

	private View mGameListRecyclerView;

	//presenter
	private LobbyPresenter presenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_list);

		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolbar);
		mToolbar.setTitle(getTitle());

		LobbyPresenter mPresenter = new LobbyPresenter();
		setPresenter(mPresenter);
		presenter.setView(this);

		Button mCreateGameButton = (Button) findViewById(R.id.createGameButton);
		Button mRefreshGameButton = (Button) findViewById(R.id.refreshListButton);



		mCreateGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//starts the CreateGameActivity as a dialogue
				startActivityForResult(new Intent(view.getContext(), CreateGameActivity.class), CREATE_GAME_REQUEST);

			}


		});

		mRefreshGameButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//request games from server, which updates model
				presenter.requestGames();
				//get games from model after it is updated.
			}
		});

		//initialize game list with what the server has

		mGameListRecyclerView = findViewById(R.id.game_list);
		assert mGameListRecyclerView != null;
		List<GameInfo> games = new ArrayList<GameInfo>();
		if(presenter.getGameList() != null) {
			games.clear();
			games.addAll(presenter.getGameList());
		}
		setupRecyclerView((RecyclerView) mGameListRecyclerView, games);

		if(findViewById(R.id.game_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}

	@Override
	public void onStop() {
		presenter.unregister();
		super.onStop();
	}

	@Override
	public void onResume() {
		presenter.register();
		presenter.requestGames();
		super.onResume();
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		if(presenter instanceof LobbyPresenter)
			this.presenter = (LobbyPresenter) presenter;
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void setGameList(Set<GameInfo> gameList) {
		if(gameList == null) {
			displayMessage("GameInfo list from server was null");
		} else {
			displayMessage("The server has " + gameList.size() + " games.");
			mGameListRecyclerView = findViewById(R.id.game_list);
			assert mGameListRecyclerView != null;
			List<GameInfo> games = new ArrayList<GameInfo>(gameList);
			GameItemRecyclerViewAdapter a = (GameItemRecyclerViewAdapter) ((RecyclerView) mGameListRecyclerView).getAdapter();
			a.swapList(games);
		}
	}

	@Override
	public int getGameID() {
		return gameID;
	}

	@Override
	public PlayerColor getPlayerColor() {
		return this.playerColor;
	}

	@Override
	public int getNewGamePlayerNumber() {
		return this.playerNumber;
	}

	@Override
	public String getNewGameName() {
		return this.gameName;
	}

	@Override
	public void displayMessage(String message) {
		Toast.makeText(this, message,
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void backToLogin() {
		startActivity(new Intent(GameListActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
	}

	@Override
	public Activity getActivity() {
		return GameListActivity.this;
	}

	@Override
	public void resumeGame(GameInfo game) {
		presenter.setCurrentGame(game);
		startGameView();
	}

	@Override
	public void startGameView(){
		startActivity(new Intent(GameListActivity.this, BoardmapActivity.class));
	}


	/**
	 * When the GameListActivity starts another Activity for a result (i.e. CreateGameActivity),
	 * the onActivityResult is called when the activity finishes. It will process the data from
	 * the requested activity and notify the presenter accordingly.
	 *
	 * @param requestCode type of request
	 * @param resultCode  result of the request (OK, FAILED)
	 * @param data        the data produced by the requested activity
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CREATE_GAME_REQUEST) {
			if(resultCode == RESULT_OK) {
				setGameName(data.getStringExtra("GAMENAME"));
				setPlayerColor(data.getStringExtra("PLAYERCOLOR"));
				setPlayerNumber(data.getStringExtra("PLAYERNUMBER"));
				presenter.createGame();
			}
		} else if(requestCode == JOIN_GAME_REQUEST) {
			if(resultCode == RESULT_OK) {
				setPlayerColor(data.getStringExtra("PLAYERCOLOR"));
				presenter.joinGame();
			}
		}
	}

	/**
	 * The CreateGameActivity will return the user's desired color as a string.
	 * setPlayerColor will change the string to the correct PlayerColor Enum.
	 *
	 * @param color
	 */
	public void setPlayerColor(String color) {
		color = color.toLowerCase();
		switch(color) {
			case "black":
				playerColor = PlayerColor.BLACK;
				break;
			case "yellow":
				playerColor = PlayerColor.YELLOW;
				break;
			case "blue":
				playerColor = PlayerColor.BLUE;
				break;
			case "green":
				playerColor = PlayerColor.GREEN;
				break;
			case "red":
				playerColor = PlayerColor.RED;
				break;
		}
	}

	/**
	 * @param gameName
	 */
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	/**
	 * The CreateGameActivity will let the player choose the number
	 * of players allowed in the game. It returns this playerNumber as a string.
	 * setPlayerNumber converts the string to an integer.
	 *
	 * @param playerNumber
	 */
	public void setPlayerNumber(String playerNumber) {
		this.playerNumber = Integer.parseInt(playerNumber);
	}


	// RECYCLERS

	/**
	 * This sets up the Recycler view with an adapter
	 *
	 * @param recyclerView
	 */
	private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<GameInfo> games) {
		recyclerView.setAdapter(new GameItemRecyclerViewAdapter(recyclerView, games));
	}

	public class GameItemRecyclerViewAdapter extends RecyclerView.Adapter<GameItemRecyclerViewAdapter.ViewHolder> {
		private RecyclerView rv;
		private GameListContent glc = null;
		private int mSelectedPosition;
		private View mSelectedView;

		GameItemRecyclerViewAdapter(RecyclerView rv, List<GameInfo> items) {
			glc = new GameListContent(items);
			mSelectedPosition = -1;
			this.rv = rv;
		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.game_list_content, parent, false);
			return new ViewHolder(view);
		}

		void swapList(List<GameInfo> list) {
			GameListContent.setGamesList(list);
			notifyDataSetChanged();
		}

		@Override
		public void onBindViewHolder(final ViewHolder holder, int position) {
			holder.mItem = (GameInfo) GameListContent.get(position);
			holder.mIdView.setText(String.valueOf(position));
			if(holder.mItem == null)
				return;
			holder.mContentView.setText(holder.mItem.getName());
			holder.mStatusView.setText(holder.mItem.getPlayerList().size() + "/" + holder.mItem.getGameSize());
			try {
				holder.mJoinButton.setEnabled(holder.mItem.userCanJoin(UIFacade.getInstance().getUser()));
			} catch(BadUserException e) {
				backToLogin();
			} catch(GameActionException e) {
				holder.mJoinButton.setText("Resume");
				holder.mJoinButton.setEnabled(true);
			}
			holder.mView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!v.isSelected()) {
						if (mSelectedView != null) {
							// deselect the previously selected view
							mSelectedView.setSelected(false);
						}
						mSelectedPosition = rv.getChildAdapterPosition(v);
						mSelectedView = v;
					} else {
						mSelectedPosition = -1;
						mSelectedView = null;
					}
					v.setSelected(!v.isSelected());
					v.setActivated(v.isSelected());

					if(mTwoPane) {
						Bundle arguments = new Bundle();
						arguments.putString(GameDetailFragment.ARG_GAME_ID, String.valueOf(holder.mItem.getGameID()));
						GameDetailFragment fragment = new GameDetailFragment();
						fragment.setArguments(arguments);
						getSupportFragmentManager().beginTransaction()
								.replace(R.id.game_detail_container, fragment)
								.commit();
					} else {
						Context context = v.getContext();
						Intent intent = new Intent(context, GameDetailActivity.class);
						intent.putExtra(GameDetailFragment.ARG_GAME_ID, String.valueOf(holder.mItem.getGameID()));

						context.startActivity(intent);
					}
				}
			});
			if (position == mSelectedPosition) {
				holder.itemView.setSelected(true);

				// keep track of the currently selected view when recycled
				mSelectedView = holder.mView;
			} else {
				holder.itemView.setSelected(false);
			}
			if(mSelectedView != null)
				mSelectedView.setActivated(true);
		}

		@Override
		public int getItemCount() {
			return GameListContent.getSize();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			final View mView;
			final TextView mIdView;
			final TextView mContentView;
			final TextView mStatusView;
			final Button mJoinButton;
			GameInfo mItem;

			ViewHolder(View view) {
				super(view);
				mView = view;
				mIdView = (TextView) view.findViewById(R.id.id);
				mContentView = (TextView) view.findViewById(R.id.content);
				mStatusView = (TextView) view.findViewById(R.id.status);
				mJoinButton = (Button) view.findViewById(R.id.joinButton);
				mJoinButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

						if(mJoinButton.getText().equals("Join")) {
							Intent intent = new Intent(v.getContext(), JoinGameActivity.class);
							intent.putExtra("game_name", mItem.getName());
							List<PlayerColor> colList = mItem.getAvailableColors();
							ArrayList<String> strList = new ArrayList<>();
							for(PlayerColor p :colList){
								strList.add(String.valueOf(p));
							}
							intent.putStringArrayListExtra("colList", strList);
							gameID = mItem.getGameID();
							startActivityForResult(intent, JOIN_GAME_REQUEST);
						} else {
							if(mItem.isPlayer(UIFacade.getInstance().getUser().getUserID()))
								resumeGame(mItem);
						}
					}
				});
				view.setClickable(true);
			}

			@Override
			public String toString() {
				return super.toString() + " '" + mContentView.getText() + "'";
			}
		}
	}


}
