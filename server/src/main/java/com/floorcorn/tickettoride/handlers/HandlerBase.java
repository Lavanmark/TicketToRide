package com.floorcorn.tickettoride.handlers;

import com.floorcorn.tickettoride.communication.Results;
import com.floorcorn.tickettoride.Serializer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by Tyler on 1/31/2017.
 */

public abstract class HandlerBase implements HttpHandler {

	protected String getAuthenticationToken(HttpExchange exchange){
		String token = null;
		if(exchange.getRequestHeaders().containsKey("Authentication")) {
			List<String> result = exchange.getRequestHeaders().get("Authentication");
			if(result.size() > 0)
				token = result.get(0);
		}
		return token;
	}

	protected String getRequestBody(HttpExchange exchange) throws IOException {
		InputStream is = exchange.getRequestBody();
		InputStreamReader reader = new InputStreamReader(is);
		StringBuilder sb = new StringBuilder();
		int ch;
		while((ch = reader.read()) != -1){
			sb.append((char)ch);
		}
		return sb.toString();
	}

	protected void sendResponseBody(HttpExchange exchange, Results result) throws IOException {
		OutputStream os = exchange.getResponseBody();
		OutputStreamWriter writer = new OutputStreamWriter(os);
		writer.write(Serializer.getInstance().serialize(result));
		writer.close();
		os.close();
	}
}
