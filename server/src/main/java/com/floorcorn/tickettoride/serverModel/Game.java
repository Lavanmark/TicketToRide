package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.User;

import java.util.ArrayList;

/**
 * Created by Tyler on 2/2/2017.
 */

public class Game extends IGame{

	private static int nextID = 0;

	public Game(IGame game) {
		this.gameID = game.getGameID();
		this.gameSize = game.getGameSize();
		this.name = game.getName();
		this.playerList = new ArrayList<Player>(game.getPlayerList());
		this.finished = game.isFinsihed();
	}

	public Game(String name, int size) {
		createID();
		this.name = name;
		if(size < 2) size = 2;
		if(size > 5) size = 5;
		this.gameSize = size;
		this.playerList = new ArrayList<Player>();
	}

	private void createID() {
		gameID = nextID++;
	}
}
