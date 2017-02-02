package com.floorcorn.tickettoride;

import com.google.gson.Gson;

/**
 * Created by Tyler on 2/1/17.
 */

public class Serializer {
	private Gson gson;

	private static Serializer instance;
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
	public User deserialize(String str) {
		return gson.fromJson(str, User.class);
	}
}
