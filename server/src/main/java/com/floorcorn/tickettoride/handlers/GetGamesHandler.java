package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/2/2017.
 */

public class GetGamesHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Get Games Handler");
		try {
			String token = getAuthenticationToken(httpExchange);
			if(token == null || token.isEmpty()) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}

			Results results;
			try {
				Set<GameInfo> games = ServerFacade.getInstance().getGames(new User(token));
				results = new Results(true, games);
				Corn.log("Game list of " + games.size() + " returned to client.");
			} catch(BadUserException e) {
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
