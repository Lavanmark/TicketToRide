package com.floorcorn.tickettoride.commands;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Created by Tyler on 2/23/2017.
 */

@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="@cmdName")
public abstract class ICommand {
	protected int commandID = -1;
	protected CmdType type;
	protected int gameID = -1;

	public CmdType getType() {
		return type;
	}

	public int getCmdID() {
		return commandID;
	}

	public int getGameID() {
		return gameID;
	}

	public abstract void execute();
}
