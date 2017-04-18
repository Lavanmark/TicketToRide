package com.floorcorn.tickettoride.ui.presenters;

import com.floorcorn.tickettoride.UIFacade;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.ui.views.ILoginView;
import com.floorcorn.tickettoride.ui.views.IView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Michael on 2/4/2017.
 */

public class LoginPresenter implements IPresenter, Observer {

    private ILoginView loginView;
    private User user;

    /**
     * Default constructor for LoginPresenter class.
     *
     * @post a new instance of LoginPresenter is created.
     */
    public LoginPresenter() {

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
            communicationHandler();
            UIFacade.getInstance().login(userName, password);

        }catch(Exception e){
            e.printStackTrace();
            this.loginView.displayMessage(e.getMessage());
        }

    }

    public void communicationHandler(){
        String host = this.loginView.getIP();
        String port = this.loginView.getPort();
        if (host == null || host.isEmpty()) return;
        if (port == null || host.isEmpty()) return;
        UIFacade.getInstance().setServer(host, port);
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
            communicationHandler();
            UIFacade.getInstance().register(username, password, firstName, lastName);
        }catch(Exception e) {
            e.printStackTrace();
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
        if(arg instanceof User) {
            this.user = UIFacade.getInstance().getUser();
            if(this.user == null) {
                //TODO this won't ever happen as the arg can't be null
                this.loginView.displayMessage("Username/password error -- unable to authenticate");
                return;
            }
            this.loginView.launchNextActivity();
        }
    }

    public void register() {
        UIFacade.getInstance().registerObserver(this);
    }
    public void unregister() {
        UIFacade.getInstance().unregisterObserver(this);
    }
    public void clearObservers() {
        UIFacade.getInstance().clearObservers();
    }
}
