package com.floorcorn.tickettoride;

import java.util.List;

public interface ICommandDAO extends IDAO{

    boolean create(ICommandDTO dto);
    boolean update(ICommandDTO dto);
    List<ICommandDTO> getAll();
    boolean delete(ICommandDTO dto);
}
