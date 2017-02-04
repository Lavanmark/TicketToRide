package com.floorcorn.tickettoride;

/**
 * Created by mgard on 2/4/2017.
 */

public interface ILoginView {

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
     * This method returns the password that was entered in the UI
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
     * @return a String representing the first and last name entered in the UI
     * fields for first and last name.
     *
     * @pre register button has been clicked
     *
     * @post the returned String is a concatenation of the strings entered into
     * the UI first name and last name fields.
     */
    public String getFullName();

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


}
