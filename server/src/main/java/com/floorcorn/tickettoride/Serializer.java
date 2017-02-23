package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;
import com.google.gson.Gson;

/**
 * Created by Tyler on 2/1/17.
 */

public class Serializer {

	private Gson gson;

	private static Serializer instance = null;
	public static Serializer getInstance() {
		if(instance == null)
			instance = new Serializer();
		return instance;
	}
	private Serializer() {
		gson = new Gson();
	}

	/**
	 * turns objects into a string of json
	 * @param o any object to be serialized
	 * @return string of json of the object
	 */
	public String serialize(Object o) {
		return gson.toJson(o);
	}

	/**
	 * converts json string of a user into a user object
	 * @param str json string representing a user
	 * @return user object from json string
	 */
	public User deserializeUser(String str) {
		return gson.fromJson(str, User.class);
	}

	/**
	 * converts string of json representing a game to a game object
	 * @param str json representing a game
	 * @return game object from json string
	 */
	public GameInfo deserializeGameInfo(String str) {
		return gson.fromJson(str, GameInfo.class);
	}

	/**
	 * converts string of json representing a player to a player object
	 * @param str json representing a player
	 * @return player object from json string
	 */
	public Player deserializePlayer(String str) {
		return gson.fromJson(str, Player.class);
	}
}
