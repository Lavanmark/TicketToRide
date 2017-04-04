package com.floorcorn.tickettoride.ui.views.fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.IBoardmapView;

/**
 * Created by Michael on 4/4/2017.
 */

public class DrawTrainCardFragment extends DialogFragment {

    private View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        Bundle args = getArguments();
        int imgId = args.getInt("imgId");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_draw_card, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.displayCardDialog);
        imageView.setImageResource(imgId);
        builder.setView(view)
                .setTitle("You drew");

        return builder.create();
    }
}
