package com.floorcorn.tickettoride.ui.views.fragments;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.ui.views.GameListContent;
import com.floorcorn.tickettoride.ui.views.activities.GameDetailActivity;
import com.floorcorn.tickettoride.ui.views.activities.GameListActivity;
import com.floorcorn.tickettoride.R;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link GameListActivity}
 * in two-pane mode (on tablets) or a {@link GameDetailActivity}
 * on handsets.
 */
public class GameDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_GAME_ID = "game_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private static IGame game = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GameDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_GAME_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            game = GameListContent.get(getArguments().getString(ARG_GAME_ID));
            //for(IGame g : GameListContent.getGamesMap())
            System.out.println(ARG_GAME_ID + " " + getArguments().getString(ARG_GAME_ID));
            System.out.println(GameListContent.get(getArguments().getString(ARG_GAME_ID)));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                if(game != null)
                    appBarLayout.setTitle(game.getName());
                System.out.println("Setting title");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game_detail, container, false);

        // Show the content as text in a TextView.
        if (game != null) {
            ((TextView) rootView.findViewById(R.id.game_detail)).setText(game.toString());
        }

        return rootView;
    }
}
