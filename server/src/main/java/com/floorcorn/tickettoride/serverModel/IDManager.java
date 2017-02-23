package com.floorcorn.tickettoride.serverModel;

/**
 * Created by Tyler on 2/22/2017.
 */

public class IDManager {
	private static int nextUserID = 0;
	private static int nextGameID = 0;

	public static int getNextUserID() {
		return nextUserID++;
	}

	public static int getNextGameID() {
		return nextGameID++;
	}
}
