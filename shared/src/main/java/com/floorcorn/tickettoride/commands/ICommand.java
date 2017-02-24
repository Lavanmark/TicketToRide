package com.floorcorn.tickettoride.commands;

/**
 * Created by Tyler on 2/23/2017.
 */

public interface ICommand {
	CmdType getType();
	int getCmdID();
	int getGameID();
	void execute();
}
