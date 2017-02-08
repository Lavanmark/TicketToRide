package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.ui.views.ILoginView;
import com.floorcorn.tickettoride.ui.views.IView;

/**
 * Created by mgard on 2/4/2017.
 */

public class LoginPresenter implements IPresenter {

    private ILoginView loginView;

    /**
     * Default constructor for LoginPresenter class
     *
     * @post a new instance of LoginPresenter is created.
     */
    public LoginPresenter() {}

    @Override
    public void setView(IView view) {
        if(view instanceof ILoginView) {
            this.loginView = (ILoginView)view;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public void loginClicked() {

    }

    public void registerClicked(){

    }
}
