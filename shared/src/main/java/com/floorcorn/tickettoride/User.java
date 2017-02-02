package com.floorcorn.tickettoride;

/**
 * Created by Tyler on 2/1/17.
 */

public class User {

	private String username;
	private String password;
	private String fullName;
	private String token;
	private int userID;

	private static int nextID = 0;

	public User(String username, String password) {

	}

	public User(String token) {

	}

	public User(String username, String password, String fullName) {

	}

	public User(User user) {

	}

	private void createID() {
		userID = nextID++;
	}

	public void setToken(String token) {

	}
}
