package com.floorcorn.tickettoride.communication;

/**
 * Created by Tyler on 2/23/2017.
 */

public class CommandRequest {
	private int gameID = -1;
	private int lastCommandID = -1;

	public CommandRequest(int gameID, int lastCommandID) {
		this.gameID = gameID;
		this.lastCommandID = lastCommandID;
	}

	public int getGameID() {
		return gameID;
	}

	public int getLastCommandID() {
		return lastCommandID;
	}
}
