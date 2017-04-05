package com.floorcorn.tickettoride.ui.views.drawers;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;
import com.floorcorn.tickettoride.ui.views.activities.BoardmapActivity;

/**
 * Created by Tyler on 3/22/2017.
 */

public class TrainCardDrawer extends BMDrawer {

	private TextView header;
	private final int MAXFACEUP = 5;
	private ImageButton faceupCards[] = new ImageButton[MAXFACEUP];
	
	public TrainCardDrawer(AppCompatActivity activity, IBoardMapPresenter presenter) {
		super(activity, presenter);

		BM_DRAWER_LAYOUT.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			
			@Override
			public void onDrawerOpened(View drawerView) {
				if (drawerView.findViewById(R.id.drawer_draw_cards) != null){
					if(parentPresenter != null)
						parentPresenter.openedCards();
				}
			}
			
			@Override
			public void onDrawerClosed(View drawerView) {
				if (drawerView.findViewById(R.id.drawer_draw_cards) != null){
					if(parentPresenter != null)
						parentPresenter.closedCards();
				}
			}
		});
	}

	public void updateFaceUp() {
		if(isOpen())
			setFaceupImages();
	}
	
	/**
	 * Sets the visible, drawable cards.
	 *
	 * @pre game board is initialized so card holder elements exist
	 * @post for each face up card f: display image for colored card that corresponds to f on the
	 * 		image button in the draw drawer
	 */
	private void setFaceupImages() {
		faceupCards[0] = (ImageButton)parentActivity.findViewById(R.id.card1);
		faceupCards[1] = (ImageButton)parentActivity.findViewById(R.id.card2);
		faceupCards[2] = (ImageButton)parentActivity.findViewById(R.id.card3);
		faceupCards[3] = (ImageButton)parentActivity.findViewById(R.id.card4);
		faceupCards[4] = (ImageButton)parentActivity.findViewById(R.id.card5);
		
		int[] imageId;
		try {
			imageId = parentPresenter.getFaceupCardColors();
			for(int i = 0; i < MAXFACEUP; i++){
				faceupCards[i].setImageResource(imageId[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void open() {
		//open drawer
		LayoutInflater inflater = (LayoutInflater)parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.drawer_draw_cards, null);
		DRAWER_HOLDER.removeAllViews();
		DRAWER_HOLDER.addView(layout);
		BM_DRAWER_LAYOUT.openDrawer(GravityCompat.START);

		header = (TextView)parentActivity.findViewById(R.id.train_card_header);
		PlayerColor pc = parentPresenter.getGame().getPlayer(parentPresenter.getUser().getUserID()).getColor();
		header.setBackground(((BoardmapActivity) parentActivity).getPlayerHeader(pc));
		//find button
		Button drawFromCardDeck = (Button) parentActivity.findViewById(R.id.draw_from_card_deck);
		
		// get the faceup images setup
		setFaceupImages();
		
		// set the onclick action
		drawFromCardDeck.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				parentPresenter.clickedTrainCardDeck();
			}
		});
		
		// set onclick action for each image.
		for(int i = 0; i < MAXFACEUP; i++){
			final int temp = i;
			faceupCards[i].setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					parentPresenter.clickedFaceUpCard(temp);
				}
			});
		}
	}
	
	@Override
	public void hide() {
		BM_DRAWER_LAYOUT.closeDrawer(Gravity.START);
	}
	
	@Override
	public boolean isOpen() {
		if(BM_DRAWER_LAYOUT.isDrawerOpen(GravityCompat.START)) {
			LinearLayout tempFrame = (LinearLayout) parentActivity.findViewById(R.id.drawer_draw_cards);
			return tempFrame != null;
		}
		return false;
	}
}
