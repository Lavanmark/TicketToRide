package com.floorcorn.tickettoride.model;

/**
 * Created by Tyler on 2/1/17.
 */

public abstract class IUser {

	protected String username = null; // Must be 4 characters long
	protected String password = null; // Must be 8 characters long
	protected String fullName = null; // Can be null
	protected String token = null;
	protected int userID = -1;

	public static final int AUTH_TOKEN_SIZE = 16;

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

		IUser user = (IUser) o;

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
}
