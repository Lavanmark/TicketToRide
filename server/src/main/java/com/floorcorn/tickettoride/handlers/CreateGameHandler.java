package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.User;
import com.floorcorn.tickettoride.serverModel.Game;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Tyler on 2/2/2017.
 */

public class CreateGameHandler extends HandlerBase {

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

			IGame iGame = Serializer.getInstance().deserializeGame(reqBody);

			if(iGame == null) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}

			iGame = ServerFacade.getInstance().createGame(new User(token), iGame.getName(), iGame.getGameSize());

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, new Results(true, iGame));
		} catch(IOException e) {
			e.printStackTrace();
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}