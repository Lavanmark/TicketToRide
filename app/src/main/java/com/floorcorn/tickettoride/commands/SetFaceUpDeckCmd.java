package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Game;
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
	public boolean execute(Game game) {
		try {
			game.getBoard().setFaceUpCards(this.faceUpDeck);
			return true;
		} catch(GameActionException e) {
			e.printStackTrace();
			return false;
		}
	}
}
