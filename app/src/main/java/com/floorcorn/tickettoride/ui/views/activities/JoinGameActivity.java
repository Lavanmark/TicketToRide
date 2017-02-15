package com.floorcorn.tickettoride.ui.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.floorcorn.tickettoride.R;

import java.util.ArrayList;

/**
 * This class acts as a dialogue box. It will send data back to the GameListActivity to notify the presenter.
 * Therefore, it does not implement the ILobbyView Interface.
 *
 * @author Lily, Kaylee
 *
 */
public class JoinGameActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String playerColor;
    private String gameName;
    private Button joinGameButton;

    private Spinner colorSpinner;
    private EditText gameNameField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);

        colorSpinner = (Spinner) findViewById(R.id.colorSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
	    ArrayList<String> strings = new ArrayList<String>();
	    //TODO create list from intent here
	    
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);

        // Specify the layout to use when the list of choices appears
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorSpinner.setAdapter(colorAdapter);
        colorSpinner.setOnItemSelectedListener(this);

        joinGameButton = (Button) findViewById(R.id.joinGameButton);
        joinGameButton.setEnabled(true);
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*
                This section allows us to pass back data to the GameListActivity class, who will notify the LobbyPresenter
                 */
                Intent intent = new Intent();
                intent.putExtra("PLAYERCOLOR", playerColor);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        gameNameField = (EditText) findViewById(R.id.gameNameTextField);
        Intent intent = getIntent();
        gameNameField.setText(intent.getStringExtra("game_name"));
    }

    private void setGameName(String name){
        gameName = name;
    }

    private void setColor(String color){
        playerColor = color;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
        switch(parent.getId()) {
            case R.id.colorSpinner:
                setColor((String)parent.getItemAtPosition(pos));
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
