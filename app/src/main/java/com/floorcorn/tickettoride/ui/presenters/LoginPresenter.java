package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.ui.views.ILoginView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by mgard on 2/4/2017.
 */

public class LoginPresenter implements IPresenter, Observer {

    private ILoginView loginView;
    private IUser user;

    /**
     * Default constructor for LoginPresenter class. Registers itself with UI facade as observer
     *
     * @post a new instance of LoginPresenter is created.
     */
    public LoginPresenter() {
        UIFacade.getInstance().registerObserver(this);
    }

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

        try {
            UIFacade.getInstance().login(userName, password);
        }catch(Exception e){
            this.loginView.displayMessage(e.getMessage());
        }

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

        try {
            UIFacade.getInstance().register(username, password, firstName, lastName);
        }catch(Exception e) {
            this.loginView.displayMessage(e.getMessage());
        }

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

    @Override
    public void update(Observable o, Object arg) {
        this.user = UIFacade.getInstance().getUser();
        if(this.user == null) {
            this.loginView.displayMessage("User error -- unable to find user");
            return;
        }

        this.loginView.launchNextActivity();
    }
}
