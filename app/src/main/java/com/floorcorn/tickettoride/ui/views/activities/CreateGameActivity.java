package com.floorcorn.tickettoride.ui.views.activities;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.views.IView;
import com.floorcorn.tickettoride.ui.views.LobbyView;


public class CreateGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LobbyView view;
    private String color;
    private String playerNumber;
    private String gameName;

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
                System.out.printf("%s,%s,%s\n",gameName, playerNumber, color);
                // view.createGame(name, playerNumber, color)
                // TODO: Close Activity
                ((Activity) v.getContext()).finish();
            }
        });

        EditText gameNameField = (EditText) findViewById(R.id.gameNameField);
        gameNameField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                Button button = (Button) findViewById(R.id.createGameButton);
                button.setEnabled(true);
                setGameName(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Button button = (Button) findViewById(R.id.createGameButton);
                button.setEnabled(false);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

    }







    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(String number) {
        this.playerNumber = number;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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
