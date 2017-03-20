package com.floorcorn.tickettoride.commands;

import com.floorcorn.tickettoride.interfaces.IClient;
import com.floorcorn.tickettoride.model.User;

/**
 * Created by pokemaughan on 3/19/17.
 */

public class DiscardDestinationCmd extends DiscardDestinationCmdData {
    @Override
    public ICommand getCmdFor(User user) {
        return this;
    }

    @Override
    public void execute(IClient client) {

    }
}
