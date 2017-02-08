package com.floorcorn.tickettoride.communication;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;

import java.util.HashMap;

public class Results {
	private boolean success;

	private Object result = null;
	private HashMap<String, Exception> errors = null;
	private BadUserException badUserException = null;
	private GameActionException gameActionException = null;
	private UserCreationException userCreationException = null;

	public Results(boolean success, Object result) {
		this.success = success;
		errors = new HashMap<String, Exception>();
		if(success) {
			this.result = result;
		} else {
			result = null;
			errors.put(result.getClass().getSimpleName(), (Exception)result);
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getResult(){
		return result;
	}

	public void addException(String className, Exception e) {
		errors.put(className, e);
	}

	public Exception getException(String className) {
		if(errors.containsKey(className))
			return errors.get(className);
		return null;
	}
}
