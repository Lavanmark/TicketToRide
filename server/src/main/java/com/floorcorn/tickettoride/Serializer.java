package com.floorcorn.tickettoride;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floorcorn.tickettoride.interfaces.ICommand;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;

import java.io.IOException;

/**
 * Created by Tyler on 2/1/17.
 */

public class Serializer {

	private ObjectMapper mapper = null;

	private static Serializer instance = null;
	public static Serializer getInstance() {
		if(instance == null)
			instance = new Serializer();
		return instance;
	}
	private Serializer() {
		mapper = new ObjectMapper();
		mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.OBJECT_AND_NON_CONCRETE);
	}

	/**
	 * turns objects into a string of json
	 * @param o any object to be serialized
	 * @return string of json of the object
	 */
	public String serialize(Object o) {
		try {
			return mapper.writeValueAsString(o);
		} catch(JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts json string of a user into a user object
	 * @param str json string representing a user
	 * @return user object from json string
	 */
	public User deserializeUser(String str) {
		try {
			return mapper.readValue(str, new TypeReference<User>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts string of json representing a game to a game object
	 * @param str json representing a game
	 * @return game object from json string
	 */
	public GameInfo deserializeGameInfo(String str) {
		try {
			return mapper.readValue(str, new TypeReference<GameInfo>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts string of json representing a player to a player object
	 * @param str json representing a player
	 * @return player object from json string
	 */
	public PlayerInfo deserializePlayerInfo(String str) {
		try {
			return mapper.readValue(str, new TypeReference<PlayerInfo>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts string of json representing a command to an ICommand object
	 * @param str json representing a command
	 * @return ICommand object from json string
	 */
	public ICommand deserializeCommand(String str) {
		try {
			return mapper.readValue(str, new TypeReference<ICommand>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
