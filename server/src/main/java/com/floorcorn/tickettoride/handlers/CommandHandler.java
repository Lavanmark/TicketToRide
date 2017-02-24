package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.interfaces.ICommand;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Michael on 2/24/2017.
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

            ICommand c = Serializer.getInstance().deserializeCommand(reqBody);

            //TODO: check for exceptions
            Results results = c.execute();
            //TODO: generate list of commands to be returned

        } catch(Exception e) {
            e.printStackTrace();
            httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
        }
    }
}
