package com.example.groundcontrol;

import com.floorcorn.tickettoride.ICommandDTO;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class CommandDTO implements ICommandDTO {
    @Override
    public int getGameID() {
        return 0;
    }

    @Override
    public String getData() {
        return null;
    }

    @Override
    public void setGameID(int gameID) {

    }

    @Override
    public void setData(String data) {

    }

    @Override
    public int getID() {
        return 0;
    }

    @Override
    public void setID(int id) {

    }
}
