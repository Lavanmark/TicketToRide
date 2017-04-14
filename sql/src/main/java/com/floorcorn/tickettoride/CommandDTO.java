package com.floorcorn.tickettoride;

/**
 * Created by Michael on 4/12/2017.
 */

public class CommandDTO implements ICommandDTO {
    private int gameID;
    private int ID;
    private String data;

    @Override
    public int getGameID() {
        return this.gameID;
    }

    @Override
    public String getData() {
        return this.data;
    }

    @Override
    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int getID() {
        return this.ID;
    }

    @Override
    public void setID(int id) {
        this.ID = id;
    }
}
