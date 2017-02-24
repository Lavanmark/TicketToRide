package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Set;

/**
 * Created by Tyler on 2/23/2017.
 */

public class GetCommandsHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			String token = getAuthenticationToken(httpExchange);
			if(token == null || token.isEmpty()) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}

			Results results = null;
			try {
				Set<GameInfo> games = ServerFacade.getInstance().getGames(new User(token));
				System.out.println(games.size());
				results = new Results(true, games);
			} catch(BadUserException e) {
				e.printStackTrace();
				results = new Results(false, e);
			}

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			System.out.println("headers sent");
			sendResponseBody(httpExchange, results);
			System.out.println("body sent");
		} catch(IOException e) {
			e.printStackTrace();
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
