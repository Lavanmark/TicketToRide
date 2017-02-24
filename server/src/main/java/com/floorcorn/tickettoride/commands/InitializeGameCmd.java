package com.floorcorn.tickettoride.commands;

/**
 * Created by Tyler on 2/23/2017.
 */

public class InitializeGameCmd implements ICommand {
	private int commandID = -1;
	@Override
	public CmdType getType() {
		return null;
	}

	@Override
	public int getCmdID() {
		return commandID;
	}

	@Override
	public int getGameID() {
		return 0;
	}

	@Override
	public void execute() {

	}
}
