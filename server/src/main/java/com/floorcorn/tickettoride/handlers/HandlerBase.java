package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.Results;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Created by Tyler on 1/31/2017.
 */

public abstract class HandlerBase implements HttpHandler {
	protected String handleRequest(HttpExchange exchange) {

		return null;
	}

	protected void handleResponse(HttpExchange exchange, Results result) {

	}
}
