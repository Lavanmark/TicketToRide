package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.serverModel.Game;
import com.floorcorn.tickettoride.serverModel.User;
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

	public String serialize(Object o) {
		return gson.toJson(o);
	}

	public User deserializeUser(String str) {
		return gson.fromJson(str, User.class);
	}

	public IGame deserializeGame(String str) {
		return gson.fromJson(str, Game.class);
	}

	public Player deserializePlayer(String str) {
		return gson.fromJson(str, Player.class);
	}
}
