package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.Game;
import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Set;

/**
 * Created by mgard on 2/1/2017.
 */

public class Serializer {

    private Gson gson = null;

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
    * This method converts an object into a String using the Gson library.
    *
    * @param obj The object to be serialized
    *
    * @return   The String representation of the input object.
     */
    public String serialize(Object obj){
        return gson.toJson(obj);
    }

    /**
     * This method converts a String into a corresponding Results object.
     *
     * @param resultsJson The String representing the results
     * @return    a Results object representing the input String
     */
    public Results deserializeResults(String resultsJson) {
	    return gson.fromJson(resultsJson, Results.class);
    }

	/**
	 * converts json of a user into a user object
	 * @param userJson string of json representing a user
	 * @return User object from string of json
	 */
	public IUser deserializeUser(String userJson) {
		return gson.fromJson(userJson, User.class);
	}

	/**
	 * converts a string into a Game object
	 * @param gameJson string of json representing the game
	 * @return game object from the string
	 */
	public Game deserializeIGame(String gameJson) {
		return gson.fromJson(gameJson, Game.class);
	}

	/**
	 * deserializes a set of games
	 * @param json string of json representing a set of games
	 * @return set of games
	 */
	public Set<IGame> deserializeGameSet(String json) {
		return gson.fromJson(json, new TypeToken<Set<Game>>(){}.getType());
	}

	/**
	 * potentially deserializes a class of any type
	 * PROBABLY SHOULDNT CALL??
	 * @param reser string of json
	 * @param type type/class of the json
	 * @return object of the type parameter from the json string.
	 */
	public Object deserialze(String reser, Type type) {
		return gson.fromJson(reser, type);
	}
}
