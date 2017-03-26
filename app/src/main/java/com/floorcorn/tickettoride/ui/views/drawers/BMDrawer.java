package com.floorcorn.tickettoride.ui.views.drawers;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.BoardmapPresenter;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Tyler on 3/21/2017.
 */

abstract class BMDrawer {
	DrawerLayout BM_DRAWER_LAYOUT;
	FrameLayout DRAWER_HOLDER;
	
	AppCompatActivity parentActivity;
	IBoardMapPresenter parentPresenter;
	
	BMDrawer(AppCompatActivity activity, final IBoardMapPresenter presenter) {
		parentActivity = activity;
		parentPresenter = presenter;
		BM_DRAWER_LAYOUT = (DrawerLayout) activity.findViewById(R.id.boardmapActivity);
		DRAWER_HOLDER = (FrameLayout) activity.findViewById(R.id.left_drawer_holder);

		BM_DRAWER_LAYOUT.addDrawerListener(new DrawerLayout.DrawerListener() {
			private boolean notified = false;

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if (notified) notified = false;
			}

			@Override
			public void onDrawerClosed(View drawerView) {
//				if(notified){
//					return;
//				}
//				else if (drawerView.findViewById(R.id.drawer_place_routes) != null){
//					Toast toast=Toast.makeText(parentActivity,"routes just closed!", Toast.LENGTH_SHORT);
//					toast.show();
//					presenter.clickedOutOfRoutes();
//					notified = true;
//				}
//				else if (drawerView.findViewById(R.id.drawer_draw_cards) != null){
//					Toast toast=Toast.makeText(parentActivity,"cards just closed!", Toast.LENGTH_SHORT);
//					toast.show();
//					presenter.clickedOutOfCards();
//					notified = true;
//				}
//				else if (drawerView.findViewById(R.id.drawer_destinations) != null){
//					Toast toast=Toast.makeText(parentActivity,"destinations just closed!", Toast.LENGTH_SHORT);
//					toast.show();
//					presenter.clickedOutOfDestinations();
//					notified = true;
//				}
//				else {
//					Toast toast=Toast.makeText(parentActivity,"drawer just closed!", Toast.LENGTH_SHORT);
//					toast.show();
//					notified = true;
//				}
			}

			@Override
			public void onDrawerStateChanged(int newState) {

			}
		});
	}
	
	public abstract void open();
	public abstract void hide();
	public abstract boolean isOpen();
}
