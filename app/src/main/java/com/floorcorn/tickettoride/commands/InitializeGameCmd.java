package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 2/23/2017.
 */

public class InitializeGameCmd extends InitializeGameCmdData {

	@Override
	public ICommand getCmdFor(User user) {
		return this;
	}

	@Override
	public void execute(IClient client) {
		client.setPlayerList(players);
	}
}
