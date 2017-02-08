package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
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
        String userName = this.loginView.getUsername();
        String password = this.loginView.getPassword();

        UIFacade.getInstance().login(userName, password);

    }

    public void registerClicked(){
        String firstName = this.loginView.getFirstName();
        String lastName = this.loginView.getLastName();
        String username = this.loginView.getNewUsername();
        String password;
        if(passwordsMatch()) {
            password = this.loginView.getNewPassword();
        } else {
            this.loginView.displayMessage("Passwords do not match");
            return;
        }

        UIFacade.getInstance().register(username, password, firstName, lastName);

    }

    /**
     * This method checks whether or not the passwords the user entered match.
     *
     * @return a boolean that evaluates to true if the passwords match
     *
     * @pre register button was clicked.
     * @pre password fields are not empty
     *
     * @post the passwords do match, or false was returned.
     */
    private boolean passwordsMatch() {
        if(this.loginView.getNewPassword().equals(this.loginView.getConfirmPassword()))
            return true;
        return false;
    }
}
