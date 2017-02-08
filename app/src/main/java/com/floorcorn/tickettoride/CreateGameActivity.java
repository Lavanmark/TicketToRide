package com.floorcorn.tickettoride;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class CreateGameActivity extends AppCompatActivity {

    List<String> colorSpinnerArray;
    List<String> numPlayerArray;
    IView LobbyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        colorSpinnerArray = LobbyView
        numPlayerArray;
    }


}
