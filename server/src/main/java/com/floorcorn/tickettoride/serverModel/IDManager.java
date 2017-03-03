package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.log.Corn;

import java.util.logging.Level;

/**
 * Created by Tyler on 2/22/2017.
 */

class IDManager {
	private static int nextUserID = 0;
	private static int nextGameID = 0;

	static int getNextUserID() {
		Corn.log(Level.FINEST, "New User id: " + nextUserID + " has been used");
		return nextUserID++;
	}

	static int getNextGameID() {
		Corn.log(Level.FINEST, "New Game id: " + nextGameID + " has been used");
		return nextGameID++;
	}
}
