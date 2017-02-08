package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Serializer;
import com.floorcorn.tickettoride.ServerFacade;
import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.IUser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by Tyler on 2/2/2017.
 */

public class RegisterHandler extends HandlerBase {

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		try {
			String reqBody = getRequestBody(httpExchange);
			if(reqBody == null || reqBody.isEmpty()) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, -1);
				return;
			}
			IUser userInfo = Serializer.getInstance().deserializeUser(reqBody);
			try {
				userInfo = ServerFacade.getInstance().register(userInfo);
			} catch(UserCreationException uce) {
				httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
				sendResponseBody(httpExchange, new Results(false, uce));
				return;
			}
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			sendResponseBody(httpExchange, new Results(true, userInfo));

		} catch(IOException e) {
			e.printStackTrace();
			httpExchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
	}
}
