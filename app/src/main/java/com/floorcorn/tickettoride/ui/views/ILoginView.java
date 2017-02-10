package com.floorcorn.tickettoride.ui.views;

/**
 * Created by mgard on 2/4/2017.
 */

public interface ILoginView extends IView {

    /**
     * This method returns the username that was entered in the UI
     *
     * @return a String representing the username that was entered
     * in the UI username field.
     *
     * @pre login/register button has been clicked
     *
     * @post the returned String is identical to the one entered in the UI
     * username view.
     */
    public String getUsername();

    /**
     * This method returns the password that was entered in the UI
     *
     * @return a String representing the password that was entered
     * in the UI password field.
     *
     * @pre login/register button has been clicked
     *
     * @post the returned String is identical to the one entered in the UI
     * password field.
     */
    public String getPassword();

    /**
     * This method returns the password that was entered in the UI under the
     * new user section
     *
     * @return a String representing the password that was entered
     * in the UI password field for a new user.
     *
     * @pre register button has been clicked
     *
     * @post the returned String is identical to the one entered in the UI
     * password field for a new user.
     */
    public String getNewPassword();

    /**
     * This method returns the confirmation password that was entered in the UI
     *
     * @return a String representing the password that was entered
     * in the UI confirm password field.
     *
     * @pre register button has been clicked
     *
     * @post the returned String is identical to the one entered in the UI
     * confirm password field.
     */
    public String getConfirmPassword();

    /**
     * This method returns the first and last name that was entered in the UI
     *
     * @return a String representing the first name entered in the UI
     * field for first name.
     *
     * @pre register button has been clicked
     *
     * @post the returned String the UI first name field.
     */
    public String getFirstName();

    /**
     * This method returns the last name that was entered in the UI
     *
     * @return a String representing the last name entered in the UI
     * field for last name.
     *
     * @pre register button has been clicked
     *
     * @post the returned String is the UI last name field.
     */
    public String getLastName();

    /**
     * This method returns the username that was entered in the UI register field
     *
     * @return a String representing the username that was entered
     * in the UI register username field.
     *
     * @pre register button has been clicked
     *
     * @post the returned String is identical to the one entered in the UI
     * username view under the register section.
     */
    public String getNewUsername();

    /**
     * This method displays a message on the screen
     *
     * @param message the String to be displayed on the screen.
     *
     * @pre message is not null
     *
     * @post the input message has been printed on the screen.
     */
    public void displayMessage(String message);

    /**
     * This method removes all input from the fields in the UI
     *
     * @post the fields in the UI are reset to default values.
     *
     */
    public void clearView();

    /**
     * This method is called when the presenter logins succesfully. It then creates a new
     * activity for the game lobby.
     *
     * @pre The user login/register was succesful.
     * @post The client is now on the GameLobby activity
     */
    public void launchNextActivity();


}
