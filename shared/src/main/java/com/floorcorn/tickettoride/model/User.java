package com.floorcorn.tickettoride.model;

/**
 * Created by Tyler on 2/1/17.
 */

public class User {

	public static final int NO_USER_ID = -1;

	private String username = null; // Must be 4 characters long
	private String password = null; // Must be 8 characters long
	private String fullName = null; // Can be null
	private String token = null;
	private int userID = NO_USER_ID;

	public static final int AUTH_TOKEN_SIZE = 16;

	/**
	 * Default constructor for serialization
	 *
	 * @post a new User has been created.
	 */
	private User(){}

	/**
	 * Constructor that creates a User with a name and password.
	 * @param username username of the new user
	 * @param password the password of the new user
	 *
	 * @pre username and password parameters are not null;
	 * @post a new User is created with the given username and password
     */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/**
	 * Constructor that creates a new user with a given Auth token
	 * @param token the auth token for the user to be created.
	 *
	 * @pre auth token param is not null
	 * @post a new User is created with the given auth token
     */
	public User(String token) {
		this.token = token;
	}

	/**
	 * Constructor that creates a new User with the username, password, and fullname
	 *
	 * @param username username of new user to be created
	 * @param password new user's password
	 * @param fullName new user's fullname
	 *
	 * @pre none of the params are null
	 * @post A new User is created with the given username, password, and fullname
     */
	public User(String username, String password, String fullName) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	/**
	 * Constructor that creates a User object with the provided username, password,
	 * fullname, and userID number.
	 * @param username username for the new User
	 * @param password password for the new User
	 * @param fullName fullname of the new User
     * @param userID id for the new User.
	 *
	 * @pre none of the parameters are null
	 * @post a new User is created with the given username, password, fullname, and userID
     */
	public User(String username, String password, String fullName, int userID) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.userID = userID;
	}

	/**
	 * A copy constructor for a new User.
	 * @param user the User object to be copied.
	 *
	 * @pre user param is not null
	 * @post A new User is created that is a copy of the provided User.
	 * @Post the new User has the same username, password, fullname, authToken,
	 * and userID as the provided user.
     */
	public User(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.fullName = user.getFullName();
		this.token = user.getToken();
		this.userID = user.getUserID();
	}

	/**
	 * Sets this User's token to the provided token
	 * @param token the token which should be set on this User
	 *
	 * @post this User's token is identical to the provided token
     */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Returns this user's token
	 * @return the token currently stored for this User
	 *
	 * @post retVal == this.token;
     */
	public String getToken() {
		return token;
	}

	/**
	 * Returns this User's username.
	 * @return the value of this User's username
	 *
	 * @post retVal == this.username
     */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns this User's password.
	 * @return the value of this User's password
	 *
	 * @post retVal == this.password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns this User's fullname.
	 * @return the value of this User's fullname
	 *
	 * @post retVal == this.fullname
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * Method to set the fullname of this User
	 * @param fullName the value to be stored as this User's fullName
	 *
	 * @post retVal == this.fullName
     */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * Returns this User's userID.
	 * @return the value of this User's userID
	 *
	 * @post retVal == this.userID
	 */
	public int getUserID() {
		return userID;
	}

	/**
	 * Method to compare two objects to determine if the other is the same as this one.
	 * @param o the other object to be compared to this User
	 * @return true/false depending on whether both this User and o are the same object
	 *
	 * @post this == o || this != o
     */
	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if(userID != user.userID) return false;
		if(!username.equals(user.username)) return false;
		if(!password.equals(user.password)) return false;
		return (fullName != null ? !fullName.equals(user.fullName) : user.fullName == null);
	}

	/**
	 * This method creates a hashing code for this userObject based on this.username, this.password,
	 * and this.userID.
	 * @return the hash code for this User object
	 *
	 * @pre this != null
	 * @pre this.username != null
	 * @post a unique hash code for this user has been generated.
     */
	@Override
	public int hashCode() {
		int result = username.hashCode();
		result = 31 * result + password.hashCode();
		result = 31 * result + userID;
		return result;
	}

	/**
	 * A string representation of this user which just consists of the username.
	 * @return the username of this user
	 *
	 * @post retVal == this.username
     */
	@Override
	public String toString() {
		return username;
	}
}
