package com.floorcorn.tickettoride;

public interface ICommandDTO extends IDTO{

    int getGameID();
    String getData();
    void setGameID(int gameID);
    void setData(String data);
}
