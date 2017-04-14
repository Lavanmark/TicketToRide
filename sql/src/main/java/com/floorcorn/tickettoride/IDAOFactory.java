package com.floorcorn.tickettoride;

public interface IDAOFactory {

    IUserDAO getUserDAOInstance();
    ICommandDAO getCommandDAOInstance();
    IGameDAO getGameDAOInstance();

}
