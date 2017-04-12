package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;

import java.util.List;

/**
 * Created by Michael on 4/12/2017.
 */

public class CommandDAO implements ICommandDAO {
    public CommandDAO(){

    }
    @Override
    public boolean create(ICommandDTO dto) {
        return false;
    }

    @Override
    public boolean update(ICommandDTO dto) {
        return false;
    }

    @Override
    public List<ICommandDTO> getAll() {
        return null;
    }

    @Override
    public boolean delete(ICommandDTO dto) {
        return false;
    }

    @Override
    public boolean startTransaction() {
        return false;
    }

    @Override
    public boolean endTransaction(boolean commit) {
        return false;
    }

    @Override
    public boolean connect() {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}
