package com.floorcorn.tickettoride;

import java.util.List;

public interface IDAOFactory {

    IUserDAO getUserDAOInstance();
    ICommandDAO getCommandDAOInstance();
    IGameDAO getGameDAOInstance();

}
