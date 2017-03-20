package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.Board;
import com.floorcorn.tickettoride.model.TrainCard;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by Tyler on 3/4/2017.
 */

public class SetFaceUpDeckCmd extends SetFaceUpDeckCmdData {

	private SetFaceUpDeckCmd(){}
	public SetFaceUpDeckCmd(TrainCard[] faceup) {
		this.faceUpDeck = faceup;
	}

	@Override
	public ICommand getCmdFor(User user) {
		return this;
	}

	@Override
	public void execute(IClient client) {
		TrainCard card = null;
		try {
			for(int i = 0; i < Board.FACEUP_DECK_SIZE; i++)
				if(faceUpDeck[i] == null)
					faceUpDeck[i] = client.getGame().getBoard().drawFromTrainCardDeck();
		} catch(GameActionException e) {
			e.printStackTrace();
		}

		client.setFaceUpDeck(faceUpDeck);
	}
}
