package com.floorcorn.tickettoride;

import java.util.List;

public interface IDAO {

    boolean startTransaction();
    boolean endTransaction(boolean commit);
    boolean connect();
    boolean clear();
    boolean create(IDTO dto);
    boolean update(IDTO dto);
    List<IDTO> getAll();
    boolean delete(IDTO dto);
}
