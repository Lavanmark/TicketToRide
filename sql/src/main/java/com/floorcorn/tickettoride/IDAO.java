package com.floorcorn.tickettoride;

public interface IDAO {

    boolean startTransaction();
    boolean endTransaction(boolean commit);
    boolean connect();
    boolean clear();

}
