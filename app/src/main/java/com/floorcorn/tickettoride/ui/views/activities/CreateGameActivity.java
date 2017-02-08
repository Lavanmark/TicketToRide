package com.floorcorn.tickettoride.ui.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.views.IView;


public class CreateGameActivity extends AppCompatActivity {

    List<String> colorSpinnerArray;
    List<String> numPlayerArray;
    IView LobbyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

    }


}
