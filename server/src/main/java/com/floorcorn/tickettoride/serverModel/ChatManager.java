package com.floorcorn.tickettoride.serverModel;

import com.floorcorn.tickettoride.communication.GameChatLog;
import com.floorcorn.tickettoride.communication.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 3/3/2017.
 */

public class ChatManager {

    private Map<Integer, GameChatLog> gameMessageLists;

    public ChatManager(){
        this.gameMessageLists = new HashMap<Integer, GameChatLog>();
    }

    public GameChatLog addMessage(Message message){
        int gameID = message.getGameID();
        if(gameMessageLists.containsKey(gameID))
            gameMessageLists.get(gameID).addMessage(message);
        return getMessages(message.getGameID());
    }

    public GameChatLog getMessages(int gameID){
        if(gameMessageLists.containsKey(gameID))
            return gameMessageLists.get(gameID);
        return null;
    }

    public void addGameChatLog(int gameID, GameChatLog chatLog)
    {
        gameMessageLists.put(gameID, chatLog);
    }
    
}
