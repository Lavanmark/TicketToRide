package com.floorcorn.tickettoride;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Michael on 2/1/2017.
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
    * This method converts an object into a String using the Gson library.
    *
    * @param obj The object to be serialized
    *
    * @return   The String representation of the input object.
     */
    public String serialize(Object obj){
	    try {
		    return mapper.writeValueAsString(obj);
	    } catch(JsonProcessingException e) {
		    e.printStackTrace();
	    }
	    return null;
    }

    /**
     * This method converts a String into a corresponding Results object.
     *
     * @param resultsJson The String representing the results
     * @return    a Results object representing the input String
     */
    public Results deserializeResults(String resultsJson) {
	    try {
		    return mapper.readValue(resultsJson, Results.class);
	    } catch(IOException e) {
		    e.printStackTrace();
	    }
	    return null;
    }

	/**
	 * converts json of a user into a user object
	 * @param userJson string of json representing a user
	 * @return User object from string of json
	 */
	public User deserializeUser(String userJson) {
		try {
			return mapper.readValue(userJson, User.class);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts a string into a Game object
	 * @param gameJson string of json representing the game
	 * @return game object from the string
	 */
	public Game deserializeGame(String gameJson) {
		try {
			return mapper.readValue(gameJson, Game.class);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * converts a string into a Game object
	 * @param gameJson string of json representing the game
	 * @return game object from the string
	 */
	public GameInfo deserializeGameInfo(String gameJson) {
		try {
			return mapper.readValue(gameJson, GameInfo.class);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * deserializes a set of games
	 * @param json string of json representing a set of games
	 * @return set of games
	 */
	public Set<GameInfo> deserializeGameInfoSet(String json) {
		try {
			return mapper.readValue(json, new TypeReference<Set<GameInfo>>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ArrayList<ICommand> deserializeCommandList(String json) {
		try {
			return mapper.readValue(json, new TypeReference<ArrayList<ICommand>>(){});
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
