package com.floorcorn.tickettoride.ui.views.drawers;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.DestinationCard;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tyler on 3/22/2017.
 */

public class DestinationDrawer extends BMDrawer {
	
	private final int MAXDESTINATIONS = 3;
	private Button drawFromDestinationDeck;
	private Button keepDestinations;
	private ImageButton destinationTickets[] = new ImageButton[MAXDESTINATIONS];
	
	public DestinationDrawer(AppCompatActivity activity, IBoardMapPresenter presenter) {
		super(activity, presenter);
	}
	
	public void updateDestinations() {
		if(isOpen())
			buildDestinationDrawer();
	}
	
	private void buildDestinationDrawer() {
		drawFromDestinationDeck = (Button) parentActivity.findViewById(R.id.draw_from_dest_deck);
		drawFromDestinationDeck.setEnabled(false);
		keepDestinations = (Button) parentActivity.findViewById(R.id.keepCards);
		keepDestinations.setEnabled(false);
		
		setDestinationImages();
		
		if (parentPresenter.getDiscardableCount() == 0) {
			drawFromDestinationDeck.setEnabled(true);
			drawFromDestinationDeck.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					drawFromDestinationDeck.setEnabled(false);
					parentPresenter.drawNewDestinationCards();
				}
			});
		} else {
			// Set images.
			for(int i = 0; i < MAXDESTINATIONS; i++) {
				destinationTickets[i].setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						v.setSelected(!v.isSelected());
						int notSelectedCount = 0;
						for(ImageButton ib : destinationTickets)
							if(!ib.isSelected())
								notSelectedCount++;
						if(notSelectedCount <= parentPresenter.getDiscardableCount())
							keepDestinations.setEnabled(true);
						else
							keepDestinations.setEnabled(false);
					}
				});
			}
			
			// Setup "keep button."
			keepDestinations.setEnabled(false);
			keepDestinations.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					boolean[] discardem = new boolean[3];
					for(int i = 0; i < MAXDESTINATIONS; i++) {
						if(!destinationTickets[i].isSelected() && destinationTickets[i].isClickable())
							discardem[i] = true;
						else
							discardem[i] = false;
					}
					parentPresenter.discardDestinations(discardem);
				}
			});
		}
	}
	
	private void setDestinationImages(){
		destinationTickets[0] = (ImageButton)parentActivity.findViewById(R.id.dest_card1);
		destinationTickets[1] = (ImageButton)parentActivity.findViewById(R.id.dest_card2);
		destinationTickets[2] = (ImageButton)parentActivity.findViewById(R.id.dest_card3);
		
		int[] imageId;
		try {
			imageId = parentPresenter.getDiscardableDestinationCards();
			if(imageId == null)
				imageId = new int[]{R.drawable.back_destinations, R.drawable.back_destinations, R.drawable.back_destinations};
			for(int i = 0; i < 3; i++) {
				destinationTickets[i].setSelected(false);
				if(i >= imageId.length) {
					destinationTickets[i].setBackgroundResource(R.drawable.back_destinations);
					destinationTickets[i].setClickable(false);
				} else {
					destinationTickets[i].setImageResource(imageId[i]);
					if(imageId[i] == R.drawable.back_destinations)
						destinationTickets[i].setClickable(false);
					else
						destinationTickets[i].setClickable(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void open() {
		LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.drawer_destinations, null);
		DRAWER_HOLDER.removeAllViews();
		DRAWER_HOLDER.addView(layout);
		BM_DRAWER_LAYOUT.openDrawer(GravityCompat.START);
		
		buildDestinationDrawer();
	}
	
	@Override
	public void hide() {
		if (isOpen()){
			BM_DRAWER_LAYOUT.closeDrawer(GravityCompat.START);
		}
	}
	
	@Override
	public boolean isOpen() {
		if(BM_DRAWER_LAYOUT.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) parentActivity.findViewById(R.id.drawer_destinations);
			return tempFrame != null;
		}
		return false;
	}
}
