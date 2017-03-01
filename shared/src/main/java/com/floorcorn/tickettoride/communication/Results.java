package com.floorcorn.tickettoride.communication;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;

import java.util.HashMap;

public class Results {
	private boolean success;

	@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@resType")
	private Object result = null;
	private HashMap<String, Exception> errors = null;
	private BadUserException badUserException = null;
	private GameActionException gameActionException = null;
	private UserCreationException userCreationException = null;

	private Results(){};
	public Results(boolean success, Object result) {
		this.success = success;
		errors = new HashMap<String, Exception>();
		if(success) {
			this.result = result;
		} else {
			this.result = null;
			if(result instanceof Exception)
				errors.put(result.getClass().getSimpleName(), (Exception)result);
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getResult(){

		return result;
	}

	/**
	 * add an exception to the result object
	 * @param className name of the exception class
	 * @param e exception to be added
	 */
	public void addException(String className, Exception e) {
		errors.put(className, e);
	}

	/**
	 * on failure, you can get an exception based on the classname
	 * @pre success = false
	 *
	 * @param className name of the exception class
	 * @return exception from the server
	 */
	public Exception getException(String className) {
		if(errors.containsKey(className))
			return errors.get(className);
		return null;
	}
}
