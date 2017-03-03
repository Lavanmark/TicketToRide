package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/13/17.
 */

public class GetGameHandler extends HandlerBase {
	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Get Games Handler");
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

			if(gi == null) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}

			Results results;
			try {
				Game game = ServerFacade.getInstance().getGame(new User(token), gi.getGameID());
				results = new Results(true, game);
				Corn.log("Game: " + game.getGameID() + " returned to user.");
			} catch(BadUserException e) {
				Corn.log(Level.SEVERE, e.getStackTrace());
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
