package com.floorcorn.tickettoride;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.CommandRequest;
import com.floorcorn.tickettoride.communication.Message;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;

import java.io.IOException;
import java.util.logging.Level;

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
		mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
		mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
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
			Corn.log(Level.SEVERE, e.getStackTrace());
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
			return mapper.readValue(str, User.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
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
			return mapper.readValue(str, GameInfo.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
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
			return mapper.readValue(str, PlayerInfo.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
		}
		return null;
	}

	/**
	 * converts string of json representing a command request to a CommandRequest object
	 * @param str json representing a CommandRequest
	 * @return CommandRequest object from json string
	 */
	public CommandRequest deserializeCommandRequest(String str) {
		try {
			return mapper.readValue(str, CommandRequest.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
		}
		return null;
	}

	/**
	 * converts string of json representing a command to an ICommand object
	 * @param str json representing a CommandRequest
	 * @return ICommand object from json string
	 */
	public ICommand deserializeCommand(String str) {
		try {
			return mapper.readValue(str, ICommand.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
		}
		return null;
	}

	public Message deserializeMessage(String str) {
		try {
			return mapper.readValue(str, Message.class);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
		}
		return null;
	}
}
