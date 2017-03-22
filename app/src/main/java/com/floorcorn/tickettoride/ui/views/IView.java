package com.floorcorn.tickettoride.ui.views;

import android.app.Activity;

import com.floorcorn.tickettoride.ui.presenters.IPresenter;

/**
 * Created by Kaylee on 2/4/2017.
 */

public interface IView {

    /**
     * This method assigns the presenter with which this view will interact
     *
     * @param presenter the presenter to interact with
     *
     * @pre presenter != null
     *
     * @post view.presenter() == presenter
     */
    void setPresenter(IPresenter presenter);
    
    /**
     * Sends user back to login activity. Uses FLAG_ACTIVITY_CLEAR_TOP to clear the activities above
     * it in the stack.
     *
     * @pre BoardmapActivity is active
     * @pre user either needs to re-login or be kicked out of app (possibly unauthorized user obj)
     * @post Android sent back to login Activity
     * @post stack of Activities is cleared (cannot go to other Activities from app without going
     * 		forward, including log in or registration)
     */
    void backToLogin();
    
    /**
     * Returns this activity.
     *
     * @pre game board has been initialized
     * @post returns this BoardmapActivity
     * @return BoardmapActivity this object
     */
    Activity getActivity();
}
