package com.floorcorn.tickettoride;

public interface IUserDTO extends IDTO{
    String getUserName();
    String getPassword();
    String getFullName();
    void setUserName(String userName);
    void setPassword(String password);
    void setFullName(String fullName);
}
