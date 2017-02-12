package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Set;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.ILobbyView;
import com.floorcorn.tickettoride.ui.views.IView;


public class CreateGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,ILobbyView {

    private IPresenter presenter;
    private String playerColor;
    private String playerNumber;
    private String gameName;
    private Button createGameButton;
    private Button refreshListButton; //this button needs to be implemented
    private Button joinGameButton; // this button needs to be implemented
    private RecyclerView gameList; // this view? whats going on with it?

    @Override
    public void setPresenter(IPresenter p) {
        presenter = p;
    }

    //uifacade is a singleton.
    @Override
    public int getGameID(){
        //game number? this is game ID
        return 0;
    }

    @Override
    public String getPlayerColor(){ // i think this can double as the starting player color and also eveyrone elses color, just has to be stored somewhere else, and depends on the time of the call
        return this.playerColor;
    }

    @Override
    public int getNewGamePlayerNumber(){ //number of people in a game
        return Integer.parseInt(this.playerNumber);
    }

    @Override
    public String getNewGameName(){
        return this.gameName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        Spinner colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.player_colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setOnItemSelectedListener(this);
        //color = (String) colorAdapter.getItem(0);

        Spinner playerSpinner = (Spinner) findViewById(R.id.playerSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> playerAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_players_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        playerSpinner.setAdapter(playerAdapter);
        playerSpinner.setOnItemSelectedListener(this);
        //playerNumber = (String) playerAdapter.getItem(0);


        Button button = (Button) findViewById(R.id.createGameButton);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: Send Info to the View
                System.out.printf("%s,%s,%s\n",gameName, playerNumber, playerColor);
                // view.createGame(name, playerNumber, color)
                // TODO: Close Activity
                ((Activity) v.getContext()).finish();
            }
        });

        EditText gameNameField = (EditText) findViewById(R.id.gameNameField);
        gameNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) { // made create game button a private data member so other classes can access it
                createGameButton = (Button) findViewById(R.id.createGameButton);
                createGameButton.setEnabled(true);
                setGameName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                createGameButton = (Button) findViewById(R.id.createGameButton);
                createGameButton.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });
    }

    private void setGameName(String name){
        gameName = name;
    }

    private void setPlayerNumber(String number){
        playerNumber = number;
    }

    private void setColor(String color){
        playerColor = color;
    }

    @Override
    public void createNewGameDialogue(){
        //Lily
    }

    @Override
    public void displayGameList(Set<Game> games) {
        //Lily
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch(parent.getId()) {
            case R.id.playerSpinner:
                setPlayerNumber((String)parent.getItemAtPosition(pos));
                break;
            case R.id.colorSpinner:
                setColor((String)parent.getItemAtPosition(pos));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
