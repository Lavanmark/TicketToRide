package com.floorcorn.tickettoride.ui.views.drawers;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.floorcorn.tickettoride.R;
import com.floorcorn.tickettoride.ui.presenters.IBoardMapPresenter;

/**
 * Created by Tyler on 3/21/2017.
 */

public abstract class BMDrawer {
	DrawerLayout BM_DRAWER_LAYOUT;
	FrameLayout DRAWER_HOLDER;
	
	AppCompatActivity parentActivity;
	IBoardMapPresenter parentPresenter;
	
	BMDrawer(AppCompatActivity activity, final IBoardMapPresenter presenter) {
		parentActivity = activity;
		parentPresenter = presenter;
		BM_DRAWER_LAYOUT = (DrawerLayout) activity.findViewById(R.id.boardmapActivity);
		DRAWER_HOLDER = (FrameLayout) activity.findViewById(R.id.left_drawer_holder);
	}
	
	public abstract void open();
	public abstract void hide();
	public abstract boolean isOpen();
}
