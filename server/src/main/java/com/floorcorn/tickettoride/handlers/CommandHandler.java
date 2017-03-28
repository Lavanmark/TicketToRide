package com.floorcorn.tickettoride.handlers;


import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.SerializerException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/24/2017.
 * @author Michael
 */

public class CommandHandler extends HandlerBase {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
	    Corn.log(Level.FINEST, "Command Handler");
        try {
            String token = getAuthenticationToken(httpExchange);
            if(token == null || token.isEmpty()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
	            Corn.log(Level.FINEST, "BADDDD");
                return;
            }

            String reqBody = getRequestBody(httpExchange);
            if(reqBody == null || reqBody.isEmpty()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
	            Corn.log(Level.FINEST, "BADDDD 22222");
	            return;
            }

            ICommand cmd = Serializer.getInstance().deserializeCommand(reqBody);

	        Results results;
	        try {
				if (cmd == null)
					throw new SerializerException("Serializer returned null");
		        ArrayList<ICommand> commands = ServerFacade.getInstance().doCommand(new User(token), cmd);
		        results = new Results(true, commands);
		        Corn.log("Command executed and " + commands.size() + " commands returned to client.");
	        } catch(BadUserException | GameActionException | SerializerException e) {
		        Corn.log(Level.SEVERE, e.getMessage());
		        e.printStackTrace();
		        results = new Results(false, e);
	        }

	        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
	        sendResponseBody(httpExchange, results);
        } catch(Exception e) {
	        Corn.log(Level.SEVERE, e.getMessage());
	        e.printStackTrace();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }
}
