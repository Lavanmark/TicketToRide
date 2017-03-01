package com.floorcorn.tickettoride.commands;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.User;

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
	public void setCmdID(int commandID) {
		this.commandID = commandID;
	}

	public int getGameID() {
		return gameID;
	}

	public abstract boolean forPlayer(User user);
	public abstract ICommand getCmdFor(User user);
	public abstract void execute(IClient client);
}
