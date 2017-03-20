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

    void backToLogin();

    Activity getActivity();
}
