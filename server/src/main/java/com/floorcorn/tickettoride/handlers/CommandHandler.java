package com.floorcorn.tickettoride.handlers;


import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

/**
 * Created by Tyler on 2/24/2017.
 * @author Michael
 */

public class CommandHandler extends HandlerBase {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            String token = getAuthenticationToken(httpExchange);
            if(token == null || token.isEmpty()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
                return;
            }

            String reqBody = getRequestBody(httpExchange);
            if(reqBody == null || reqBody.isEmpty()) {
                httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
                return;
            }

            ICommand cmd = Serializer.getInstance().deserializeCommand(reqBody);

	        Results results = null;
	        try {
		        ArrayList<ICommand> commands = ServerFacade.getInstance().doCommand(new User(token), cmd);
		        results = new Results(true, commands);
	        } catch(BadUserException | GameActionException e) {
		        e.printStackTrace();
		        results = new Results(false, e);
	        }

	        httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
	        sendResponseBody(httpExchange, results);
        } catch(Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }
}
