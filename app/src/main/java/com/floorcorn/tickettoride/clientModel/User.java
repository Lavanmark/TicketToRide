package com.floorcorn.tickettoride.clientModel;

import com.floorcorn.tickettoride.model.IUser;

/**
 * Created by Tyler on 2/8/17.
 */

public class User extends IUser {
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

	public User(IUser user) {
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.fullName = user.getFullName();
		this.token = user.getToken();
		this.userID = user.getUserID();
	}
}
