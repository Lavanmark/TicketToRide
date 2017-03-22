package com.floorcorn.tickettoride.communication;

import com.floorcorn.tickettoride.model.Game;

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

	@Override
	public boolean equals(Object o) {
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Message message = (Message) o;

		if(gameID != message.gameID) return false;
		if(content != null ? !content.equals(message.content) : message.content != null)
			return false;
		return playerName != null ? playerName.equals(message.playerName) : message.playerName == null;

	}

	@Override
	public int hashCode() {
		int result = content != null ? content.hashCode() : 0;
		result = 31 * result + gameID;
		result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
		return result;
	}
}
