package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.GameChatLog;
import com.floorcorn.tickettoride.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Michael on 3/3/2017.
 */

public class ChatManager {

    private Map<Integer, GameChatLog> gameMessageLists;

    public ChatManager(){
        this.gameMessageLists = new HashMap<Integer, GameChatLog>();
    }

    public void addMessage(Message message){
        int gameID = message.getGameID();
        gameMessageLists.get(gameID).addMessage(message);
    }

    public GameChatLog getMessages(int gameID){
        return gameMessageLists.get(gameID);
    }

    public void addGameChatLog(int gameID, GameChatLog chatLog)
    {
        gameMessageLists.put(gameID, chatLog);
    }
}
