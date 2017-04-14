package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.SerializerException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/2/2017.
 */

public class CreateGameHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Create Game Handler");
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

			GameInfo gi = Serializer.getInstance().deserializeGameInfo(reqBody);

			// Joseph commented this out because I think we want to return a Results(false...)
//			if(gi == null) {
//				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
//				return;
//			}

			Results results;
			try {
				if (gi == null)
					throw new SerializerException("Serializer returned null");
				gi = ServerFacade.getInstance().createGame(new User(token), gi.getName(), gi.getGameSize());
				results = new Results(true, gi);
				Corn.log("Created game with id: " + gi.getGameID());
			} catch(BadUserException | SerializerException | GameCreationException e) {
				Corn.log(Level.SEVERE, e.getMessage());
				results = new Results(false, e);
			}

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, results);
		} catch(Exception e) {
			Corn.log(Level.SEVERE, e.getMessage());
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
