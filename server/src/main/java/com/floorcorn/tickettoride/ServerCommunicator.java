package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.handlers.LoginHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerCommunicator {

	private HttpServer server;

	private final int MAX_WAITING_CONNECTIONS = 12;

	public ServerCommunicator(String port){
		System.out.println("Initializing HTTP Server");
		try {
			server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), MAX_WAITING_CONNECTIONS);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		server.setExecutor(null); // use the default executor

		System.out.println("Creating contexts");
		createContexts();

		System.out.println("Starting server on port: " + port);
		server.start();
	}

	private void createContexts() {
		server.createContext("/login", new LoginHandler());
		server.createContext("/register", new LoginHandler());
		server.createContext("/getGames", new LoginHandler());
		server.createContext("/leaveGame", new LoginHandler());
		server.createContext("/joinGame", new LoginHandler());
	}

	public static void main(String[] args) {
		String port = "8080";
		if(args.length == 1)
			port = args[0];
		new ServerCommunicator(port);
	}
}
