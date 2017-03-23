package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 3/4/2017.
 */

public class SetFaceUpDeckCmd extends SetFaceUpDeckCmdData {

	public SetFaceUpDeckCmd(){}

	@Override
	public ICommand getCmdFor(User user) {
		return this;
	}

	@Override
	public void execute(Game game) {
		game.getBoard().replaceFaceUpCard();
		this.faceUpDeck = game.getBoard().getFaceUpCards();
	}
}
