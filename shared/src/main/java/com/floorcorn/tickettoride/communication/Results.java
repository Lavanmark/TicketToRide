package com.floorcorn.tickettoride.communication;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;

public class Results {
	private boolean success;

	private Object result;
	private BadUserException badUserException = null;
	private GameActionException gameActionException = null;
	private UserCreationException userCreationException = null;

	public Results(boolean success, Object result) {
		this.success = success;
		if(success) {
			this.result = result;
		} else {
			if(result instanceof BadUserException)
				badUserException = (BadUserException)result;
			else if(result instanceof UserCreationException)
				userCreationException = (UserCreationException)result;
			else if(result instanceof GameActionException)
				gameActionException = (GameActionException)result;
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getResult(){
		return result;
	}

	public boolean getException() throws BadUserException, GameActionException, UserCreationException {
		if(success)
			return false;
		if(badUserException != null)
			throw badUserException;
		if(gameActionException != null)
			throw gameActionException;
		if(userCreationException != null)
			throw userCreationException;
		return false;
	}

	public UserCreationException getUserCreationException() {
		return userCreationException;
	}

	public BadUserException getBadUserException() {
		return badUserException;
	}

	public GameActionException getGameActionException() {
		return gameActionException;
	}
}
