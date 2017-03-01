package com.floorcorn.tickettoride.serverModel;

/**
 * Created by Tyler on 2/22/2017.
 */

class IDManager {
	private static int nextUserID = 0;
	private static int nextGameID = 0;

	static int getNextUserID() {
		return nextUserID++;
	}

	static int getNextGameID() {
		return nextGameID++;
	}
}
