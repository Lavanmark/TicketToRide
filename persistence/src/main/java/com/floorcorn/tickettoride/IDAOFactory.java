package com.floorcorn.tickettoride;

public interface IDAOFactory {

    IUserDAO getUserDAOInstance();
    ICommandDAO getCommandDAOInstance();
    IGameDAO getGameDAOInstance();
    
    
    IUserDTO getUserDTOInstance();
    ICommandDTO getCommandDTOInstance();
    IGameDTO getGameDTOInstance();
    
    boolean startTransaction();
    boolean endTransaction(boolean commit);
}
