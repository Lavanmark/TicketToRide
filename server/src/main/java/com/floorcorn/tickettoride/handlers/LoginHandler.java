package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.SerializerException;
import com.floorcorn.tickettoride.log.Corn;
import com.floorcorn.tickettoride.model.User;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.logging.Level;

/**
 * Created by Tyler on 1/31/2017.
 */

public class LoginHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Corn.log(Level.FINEST, "Login Handler");
		try {
			String reqBody = getRequestBody(httpExchange);
			if(reqBody == null || reqBody.isEmpty()) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}
			User userInfo = Serializer.getInstance().deserializeUser(reqBody);

			Results results;
			try {
				if (userInfo == null)
					throw new SerializerException("Serializer returned null");
				userInfo = ServerFacade.getInstance().login(userInfo);
				results = new Results(true, userInfo);
				Corn.log("User " + userInfo.getUsername() + " has logged in.");
			} catch(BadUserException | SerializerException e) {
				Corn.log(Level.SEVERE, e.getMessage());
				e.printStackTrace();
				results = new Results(false, e);
			}

			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, results);
		} catch(Exception e){
			Corn.log(Level.SEVERE, e.getMessage());
			e.printStackTrace();
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
