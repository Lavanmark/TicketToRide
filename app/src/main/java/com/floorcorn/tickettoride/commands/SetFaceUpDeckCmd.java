package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 3/4/2017.
 */

public class SetFaceUpDeckCmd extends SetFaceUpDeckCmdData {
	@Override
	public ICommand getCmdFor(User user) {
		return this;
	}

	@Override
	public void execute(IClient client) {
		client.setFaceUpDeck(this.faceUpDeck);
	}
}
