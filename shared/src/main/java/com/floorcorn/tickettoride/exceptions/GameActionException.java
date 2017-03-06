package com.floorcorn.tickettoride.exceptions;

/**
 * Created by Tyler on 2/7/17.
 */

/**
 * A GameActionException is thrown when trying to do something related to a Player's move and it
 * fails.
 */
public class GameActionException extends Exception {
	public GameActionException(String message) {
		super(message);
	}
}
