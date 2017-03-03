package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.Game;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.Player;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

/**
 * Created by Tyler on 2/2/2017.
 */

public class JoinGameHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Join Game Handler");
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

			PlayerInfo player = Serializer.getInstance().deserializePlayerInfo(reqBody);

			if(player == null) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}

			Results results;
			try {
				GameInfo game = ServerFacade.getInstance().joinGame(new User(token), player.getGameID(), player.getColor());
				results = new Results(true, game);
				Corn.log("Player joined game: " + game.getGameID());
			} catch(BadUserException | GameActionException e) {
				Corn.log(Level.SEVERE, e.getStackTrace());
				results = new Results(false, e);
			}

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, results);
		} catch(Exception e) {
			Corn.log(Level.SEVERE, e.getStackTrace());
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
