package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.handlers.CommandHandler;
import com.floorcorn.tickettoride.handlers.CreateGameHandler;
import com.floorcorn.tickettoride.handlers.GetGameHandler;
import com.floorcorn.tickettoride.handlers.GetGamesHandler;
import com.floorcorn.tickettoride.handlers.JoinGameHandler;
import com.floorcorn.tickettoride.handlers.LeaveGameHandler;
import com.floorcorn.tickettoride.handlers.LoginHandler;
import com.floorcorn.tickettoride.handlers.RegisterHandler;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class ServerCommunicator {

	private HttpServer server;

	private final int MAX_WAITING_CONNECTIONS = 12;

	/**
	 * Creates an HttpServer on provided port
	 *
	 * @param port used as the post the server listens on.
	 */
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
		server.createContext(IServer.LOGIN, new LoginHandler());
		server.createContext(IServer.REGISTER, new RegisterHandler());
		server.createContext(IServer.GET_GAME, new GetGameHandler());
		server.createContext(IServer.GET_GAMES, new GetGamesHandler());
		server.createContext(IServer.CREATE_GAME, new CreateGameHandler());
		server.createContext(IServer.LEAVE_GAME, new LeaveGameHandler());
		server.createContext(IServer.JOIN_GAME, new JoinGameHandler());
		server.createContext(IServer.COMMAND, new CommandHandler());
	}

	public static void main(String[] args) {
		String port = "8080";
		if(args.length == 1)
			port = args[0];
		new ServerCommunicator(port);
	}
}
