package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.IDAOFactory;
import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IUserDAO;

/**
 * Created by Michael on 4/12/2017.
 */

public class RelationalDAOFactory implements IDAOFactory {

    //TODO: Set database url string
    private static final String DATABASe_URL = "";

    @Override
    public IUserDAO getUserDAOInstance() {
        return null;
    }

    @Override
    public ICommandDAO getCommandDAOInstance() {
        return new CommandDAO(DATABASe_URL);
    }

    @Override
    public IGameDAO getGameDAOInstance() {
        return null;
    }
}
