package com.floorcorn.tickettoride.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.presenters.PregamePresenter;
import com.floorcorn.tickettoride.ui.views.IPregameView;

import java.util.ArrayList;
import java.util.Set;

/**
 * @author Joseph Hansen
 */

public class PregameActivity extends AppCompatActivity implements IPregameView {

    private PregamePresenter presenter;
    private RecyclerView playerList;
    private Button cancelGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregame);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerList = (RecyclerView) findViewById(R.id.pregame_player_list);
        cancelGameButton = (Button) findViewById(R.id.cancelGameButton);

        cancelGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.cancelGame();
            }
        });
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
     * NOTE: I don't think this function is necessary. The Pregame view *is* the game waiting
     * dialog.
     */
    @Override
    public void createGameWaitingDialog() {
        throw new UnsupportedOperationException();
    }

    /**
     * Displays this game's players in the view.
     * @param players A Set of Player objects representing players in current game
     */
    @Override
    public void displayPlayerList(ArrayList<Player> players) {
        // TODO
        throw new UnsupportedOperationException();
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
        // this needs to go back two views; the Boardmap makes the Pregame view and we want to
        // go back to before the Boardmap
        startActivity(new Intent(PregameActivity.this, GameListActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

}
