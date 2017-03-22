package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.commands.ICommand;
import com.floorcorn.tickettoride.communication.CommandRequest;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/23/2017.
 */

public class GetCommandsHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Get Commands Handler");
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

			CommandRequest cr = Serializer.getInstance().deserializeCommandRequest(reqBody);

			Results results;
			try {
				ArrayList<ICommand> commands = ServerFacade.getInstance().getCommandsSince(new User(token), cr.getGameID(), cr.getLastCommandID());
				results = new Results(true, commands);
				Corn.log("Returning " + commands.size() + " commands to client.");
			} catch(BadUserException | GameActionException e) {
				Corn.log(Level.SEVERE, e.getMessage());
				results = new Results(false, e);
			}

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, results);
		} catch(IOException e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
