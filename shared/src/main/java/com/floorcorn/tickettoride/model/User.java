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
	 * @pre username and password are not null;
	 * @post a new User is created with the given
     */
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public User(String token) {
		this.token = token;
	}

	public User(String username, String password, String fullName) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
	}

	public User(String username, String password, String fullName, int userID) {
		this.username = username;
		this.password = password;
		this.fullName = fullName;
		this.userID = userID;
	}

	public User(User user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.fullName = user.getFullName();
		this.token = user.getToken();
		this.userID = user.getUserID();
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getUserID() {
		return userID;
	}

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

	@Override
	public int hashCode() {
		int result = username.hashCode();
		result = 31 * result + password.hashCode();
		result = 31 * result + userID;
		return result;
	}

	@Override
	public String toString() {
		return username;
	}
}
