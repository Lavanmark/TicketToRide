package com.example.groundcontrol;

import com.floorcorn.tickettoride.IGameDTO;

/**
 * Created by Kaylee on 4/14/2017.
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
        if(data != null && data.contains("GameID\":-1"))
            data = data.replace("GameID\":-1", "GameID\":"+String.valueOf(id));
    }
}
