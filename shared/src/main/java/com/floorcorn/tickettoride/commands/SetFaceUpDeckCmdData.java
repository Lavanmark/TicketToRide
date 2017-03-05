package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 3/4/2017.
 */

public abstract class SetFaceUpDeckCmdData extends ICommand {

	TrainCard[] faceUpDeck = new TrainCard[5];

	@Override
	public boolean forPlayer(User user) {
		return true;
	}

	public TrainCard[] getFaceUpDeck() {
		return faceUpDeck;
	}
}
