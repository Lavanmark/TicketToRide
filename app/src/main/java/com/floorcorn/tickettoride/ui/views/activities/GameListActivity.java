package com.floorcorn.tickettoride.ui.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.LobbyPresenter;
import com.floorcorn.tickettoride.ui.presenters.LoginPresenter;
import com.floorcorn.tickettoride.ui.views.DummyContent;
import com.floorcorn.tickettoride.ui.views.ILobbyView;
import com.floorcorn.tickettoride.ui.views.IView;
import com.floorcorn.tickettoride.ui.views.fragments.GameDetailFragment;
import com.floorcorn.tickettoride.ui.views.GameListContent;
import com.floorcorn.tickettoride.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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
    static final int UPDATE_GAME_REQUEST = 2;
    static final int DISPLAY_GAME_REQUEST = 3;

    //model references
    private Player.PlayerColor playerColor;
    private int playerNumber;
    private String gameName;
    private Set<IGame> gameList;

    // ui references
    private Toolbar mToolbar;
    private FloatingActionButton mCreateGameFab;
    private FloatingActionButton mUpdateGameListFab;
    private View mGameListRecyclerView;
    private Button mCreateGameButton;
    private Button mRefreshGameButton;
    private Button mJoinGameButton;
    private Button mResumeGameButton;

    //presenter
    private LobbyPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(getTitle());

        LobbyPresenter mPresenter = new LobbyPresenter();
        setPresenter(mPresenter);
        presenter.setView(this);
        gameList = new HashSet<IGame>();

        //mCreateGameFab = (FloatingActionButton) findViewById(R.id.fab);
        //mUpdateGameListFab = (FloatingActionButton) findViewById(R.id.fab2);
        mCreateGameButton = (Button) findViewById(R.id.createGameButton);
        mRefreshGameButton = (Button) findViewById(R.id.refreshListButton);
        mJoinGameButton = (Button)findViewById(R.id.joinGameButton);
        mResumeGameButton = (Button)findViewById(R.id.resumeGameButton);

        mCreateGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //starts the CreateGameActivity as a dialogue
                Intent intent = new Intent(getBaseContext(), CreateGameActivity.class);
                startActivityForResult(new Intent(view.getContext(),CreateGameActivity.class), CREATE_GAME_REQUEST);

            }


        });

        mRefreshGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //request games from server, which updates model
                presenter.requestGames();
                //get games from model after it is updated.
                //TODO: Observer Pattern
                gameList = presenter.getGameList();
                if (gameList == null){
                    displayMessage("Game list from server was null");
                }
                else{
                    displayMessage("The server has " + gameList.size() + " games.");
//                    mGameListRecyclerView = findViewById(R.id.game_list);
//                    assert mGameListRecyclerView  != null;
                    List<IGame> games = new ArrayList<IGame>(gameList);
                    //refreshRecyclerView((RecyclerView) mGameListRecyclerView, games);
                    GameItemRecyclerViewAdapter a = (GameItemRecyclerViewAdapter)((RecyclerView) mGameListRecyclerView).getAdapter();
                    a.swapList(games);


                }

            }
        });

        mJoinGameButton.setEnabled(false);
        mResumeGameButton.setEnabled(false);

        //TODO: WHY DOES THIS BREAK EVERY TIME?? HOW DO I GET THE INITIAL LIST?
//        try {
//            gameList = presenter.getGameList();
//            if (gameList == null){
//                displayMessage("Game list from server was null");
//            }
//            else{
//                displayMessage("The server has " + gameList.size() + " games.");
//            }
//
//        } catch (BadUserException e) {
//            //TODO: move this back down to the presenter level and call display message on e.message()
//            e.printStackTrace();
//            displayMessage(e.getMessage());
//        }
        //initialize game list with what the server has

        mGameListRecyclerView = findViewById(R.id.game_list);
        assert mGameListRecyclerView  != null;
        List<IGame> games = new ArrayList<IGame>(gameList);
        setupRecyclerView((RecyclerView) mGameListRecyclerView, games);
        //setupSimpleRecyclerView((RecyclerView)mGameListRecyclerView);

        if (findViewById(R.id.game_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }


    }

    @Override
    public void setPresenter(IPresenter presenter) {
        if(presenter instanceof LobbyPresenter)
            this.presenter = (LobbyPresenter) presenter;
        else
            throw new IllegalArgumentException();
    }

    @Override
    public int getGameID() {
        return 0;
    }

    @Override
    public Player.PlayerColor getPlayerColor() {
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
    public void createNewGameDialogue() {
        //TODO: why? This implements on a click listener?
    }

    @Override
    public void displayGameList(Set<Game> games) {
        //TODO: why? This implements automatically?
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_LONG).show();
    }


    /**
     *
     * When the GameListActivity starts another Activity for a result (i.e. CreateGameActivity),
     * the onActivityResult is called when the activity finishes. It will process the data from
     * the requested activity and notify the presenter accordingly.
     *
     * @param requestCode type of request
     * @param resultCode result of the request (OK, FAILED)
     * @param data the data produced by the requested activity
     */

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CREATE_GAME_REQUEST){
            if (resultCode == RESULT_OK){
                setGameName(data.getStringExtra("GAMENAME"));
                setPlayerColor(data.getStringExtra("PLAYERCOLOR"));
                setPlayerNumber(data.getStringExtra("PLAYERNUMBER"));
                presenter.createGame();
            }
        }
    }

    /**
     * The CreateGameActivity will return the user's desired color as a string.
     * setPlayerColor will change the string to the correct PlayerColor Enum.
     * @param color
     */
    public void setPlayerColor(String color) {
        switch(color) {
            case "black":
                playerColor = Player.PlayerColor.BLACK;
                break;
            case "yellow":
                playerColor = Player.PlayerColor.YELLOW;
                break;
            case "blue":
                playerColor = Player.PlayerColor.BLUE;
                break;
            case "green":
                playerColor = Player.PlayerColor.GREEN;
                break;
            case "red":
                playerColor = Player.PlayerColor.RED;
        }
    }

    /**
     *
     * @param gameName
     */
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    /**
     * The CreateGameActivity will let the player choose the number
     * of players allowed in the game. It returns this playerNumber as a string.
     * setPlayerNumber converts the string to an integer.
     * @param playerNumber
     */
    public void setPlayerNumber(String playerNumber) {
        this.playerNumber = Integer.parseInt(playerNumber);
    }


    // RECYCLERS 
    
    /**
     * This sets up the Recycler view with an adapter
     * @param recyclerView
     */
    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<IGame> games) {
        recyclerView.setAdapter(new GameItemRecyclerViewAdapter(games));
    }


    public class GameItemRecyclerViewAdapter
            extends RecyclerView.Adapter<GameItemRecyclerViewAdapter.ViewHolder> {

        //private final List<DummyContent.DummyItem> mValues;
        private List<IGame> mValues;

//        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
//            mValues = items;
//        }
        public GameItemRecyclerViewAdapter(List<IGame> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.game_list_content, parent, false);
            return new ViewHolder(view);
        }

        public void swapList(List<IGame> list){
            mValues.clear();
            mValues.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = (Game)mValues.get(position);
            holder.mIdView.setText(String.valueOf(position));
            holder.mContentView.setText(mValues.get(position).getName());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(GameDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getGameID()));
                        GameDetailFragment fragment = new GameDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.game_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, GameDetailActivity.class);
                        intent.putExtra(GameDetailFragment.ARG_ITEM_ID, String.valueOf(holder.mItem.getGameID()));

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public Game mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }


}
