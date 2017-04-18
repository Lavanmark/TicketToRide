package com.example.groundcontrol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Created by Tyler on 4/18/2017.
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
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * converts json string of a user into a user object
	 * @param str json string representing a user
	 * @return user object from json string
	 */
	public UserDTO deserializeUserDTO(String str) {
		try {
			return mapper.readValue(str, UserDTO.class);
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
