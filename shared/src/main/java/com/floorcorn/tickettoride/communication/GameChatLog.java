package com.floorcorn.tickettoride.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 3/3/2017.
 */

public class GameChatLog {
    private List<Message> messageList;

    public GameChatLog(){
        messageList = new ArrayList<>();
    }

    public void addMessage(Message m){
        messageList.add(m);
    }

    public List<Message> getRecentMessages(){
        List<Message> recentMessages = new ArrayList<>();
        for(int i = (messageList.size() > 20? messageList.size() - 20 : 0); i < messageList.size(); i++){
            recentMessages.add(messageList.get(i));
        }
        return recentMessages;
    }
}
