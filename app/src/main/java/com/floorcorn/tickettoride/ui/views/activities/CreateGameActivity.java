package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import java.util.Set;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.ui.presenters.IPresenter;
import com.floorcorn.tickettoride.ui.views.ILobbyView;

/**
 * This class acts as a dialogue box. It will send data back to the GameListActivity to notify the presenter.
 * Therefore, it does not implement the ILobbyView Interface.
 *
 * @author Lily, Kaylee
 *
 */
public class CreateGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String playerColor;
    private String playerNumber;
    private String gameName;
    private Button createGameButton;

    private Spinner colorSpinner;
    private Spinner playerSpinner;
    private EditText gameNameField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this,
                R.array.player_colors_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setOnItemSelectedListener(this);

        playerSpinner = (Spinner) findViewById(R.id.playerSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> playerAdapter = ArrayAdapter.createFromResource(this,
                R.array.num_players_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        playerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        playerSpinner.setAdapter(playerAdapter);
        playerSpinner.setOnItemSelectedListener(this);


        createGameButton = (Button) findViewById(R.id.createGameButton);
        createGameButton.setEnabled(false);
        createGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                This section allows us to pass back data to the GameListActivity class, who will notify the LobbyPresenter
                 */
                System.out.printf("%s,%s,%s\n",gameName, playerNumber, playerColor);
                Intent intent = new Intent();
                intent.putExtra("GAMENAME", gameName);
                intent.putExtra("PLAYERNUMBER", playerNumber);
                intent.putExtra("PLAYERCOLOR", playerColor);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        gameNameField = (EditText) findViewById(R.id.gameNameField);
        gameNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) { // made create game button a private data member so other classes can access it
                createGameButton = (Button) findViewById(R.id.createGameButton);
                if(s.length() > 0) {
	                setGameName(s.toString());
	                createGameButton.setEnabled(true);
                } else {
	                createGameButton.setEnabled(false);
                }

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
