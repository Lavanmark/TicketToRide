package com.floorcorn.tickettoride;

import java.util.List;

public interface IGameDAO extends IDAO {

    boolean create(IGameDTO dto);
    boolean update(IGameDTO dto);
    List<IGameDTO> getAll();
    boolean delete(IGameDTO dto);

}
