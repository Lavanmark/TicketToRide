package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.IGameDTO;

/**
 * Created by Michael on 4/12/2017.
 */

public class GameDTO implements IGameDTO {
    private String data;
    private int ID;

    @Override
    public String getData() {
        return data;
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
