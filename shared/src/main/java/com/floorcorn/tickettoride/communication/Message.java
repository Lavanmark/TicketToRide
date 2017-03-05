package com.floorcorn.tickettoride.communication;

import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.Player;

/**
 * Created by Michael on 3/3/2017.
 */

public class Message {

    private String content;
    private int gameID = Game.NO_GAME_ID;
    private int playerID = Player.NO_PLAYER_ID;
	//TODO could give the message a playername instead and then it is auto added to the chat?

    private Message(){}
    public Message(String content) {
        this.content = content;
    }
	public Message(String content, int gameID, int playerID) {
		this.content = content;
		this.gameID = gameID;
		this.playerID = playerID;
	}

    public void setGameID(int id){
        this.gameID = id;
    }

    public int getGameID(){
        return this.gameID;
    }

    public void setMessageContent(String content){
        this.content = content;
    }

    public String getMessageContent(){
        return content;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }
}
