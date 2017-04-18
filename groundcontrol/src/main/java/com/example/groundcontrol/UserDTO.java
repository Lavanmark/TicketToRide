package com.example.groundcontrol;

import com.floorcorn.tickettoride.IUserDTO;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class UserDTO implements IUserDTO {
    private String username;
    private String password;
    private String fullName;
    private int ID;

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public void setUserName(String userName) {
        this.username = userName;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
