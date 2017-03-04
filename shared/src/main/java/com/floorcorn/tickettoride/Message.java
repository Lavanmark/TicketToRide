package com.floorcorn.tickettoride;

/**
 * Created by Michael on 3/3/2017.
 */

public class Message {

    private String content;
    private int gameID;

    public Message(){

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
}
