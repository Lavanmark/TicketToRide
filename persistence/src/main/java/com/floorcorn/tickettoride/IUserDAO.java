package com.floorcorn.tickettoride;

import java.util.List;

public interface IUserDAO extends IDAO {
    boolean create(IUserDTO dto);
    boolean update(IUserDTO dto);
    List<IUserDTO> getAll();
    boolean delete(IUserDTO dto);
}
