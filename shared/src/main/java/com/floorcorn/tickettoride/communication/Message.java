package com.floorcorn.tickettoride.communication;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;

/**
 * Created by Michael on 3/3/2017.
 */

public class Message {

    private String content;
    private int gameID = Game.NO_GAME_ID;
    private String playerName = "";

    private Message(){}
    public Message(String content) {
        this.content = content;
    }
	public Message(String content, int gameID, String playerName) {
		this.content = content;
		this.gameID = gameID;
		this.playerName = playerName;
	}

    public void setGameID(int id){
        this.gameID = id;
    }

    public int getGameID(){
        return this.gameID;
    }

    public String getMessageContent(){
        return content;
    }

    @Override
    public String toString() {
        return playerName + ": " + content;
    }
}
