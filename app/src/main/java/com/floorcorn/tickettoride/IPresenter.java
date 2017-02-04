package com.floorcorn.tickettoride;

/**
 * Created by mgard on 2/4/2017.
 */

public interface IPresenter {

    public void setView(IView view);

    public void setModel(UIFacade facade);
}
