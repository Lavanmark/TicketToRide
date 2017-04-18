package com.floorcorn.tickettoride.commands;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 2/23/2017.
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="@cmdName")
public abstract class ICommand {
	public static final int NO_CMD_ID = -1;

	protected int commandID = NO_CMD_ID;
	protected int gameID = Game.NO_GAME_ID;

	public int getCmdID() {
		return commandID;
	}
	public void setCmdID(int commandID) {
		this.commandID = commandID;
	}

	public int getGameID() {
		return gameID;
	}

	//TODO update everywhere to use this.
	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public abstract boolean forPlayer(User user);
	public abstract ICommand getCmdFor(User user);
	public abstract boolean execute(Game game);
}
