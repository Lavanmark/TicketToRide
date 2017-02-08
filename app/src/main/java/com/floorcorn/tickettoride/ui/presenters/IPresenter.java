package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.ui.views.IView;

/**
 * Created by Kaylee on 2/4/2017.
 */

public interface IPresenter {

    /**
     * This method sets the view which this presenter interacts with.
     *
     * @param view the view corresponding to this presenter.
     *
     * @pre view != null
     *
     * @post presenter.view() == view
     */
    public void setView(IView view);
    //public void setModel(UIFacade facade); commented out because uifacade is not implemented yet
}
