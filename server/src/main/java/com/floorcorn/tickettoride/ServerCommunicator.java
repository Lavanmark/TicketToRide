package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.handlers.CommandHandler;
import com.floorcorn.tickettoride.handlers.CreateGameHandler;
import com.floorcorn.tickettoride.handlers.GetChatHandler;
import com.floorcorn.tickettoride.handlers.GetCommandsHandler;
import com.floorcorn.tickettoride.handlers.GetGameHandler;
import com.floorcorn.tickettoride.handlers.GetGamesHandler;
import com.floorcorn.tickettoride.handlers.JoinGameHandler;
import com.floorcorn.tickettoride.handlers.LeaveGameHandler;
import com.floorcorn.tickettoride.handlers.LoginHandler;
import com.floorcorn.tickettoride.handlers.RegisterHandler;
import com.floorcorn.tickettoride.handlers.SendChatHandler;
import com.floorcorn.tickettoride.interfaces.IServer;
import com.floorcorn.tickettoride.log.Corn;
import com.sun.net.httpserver.HttpServer;

import java.io.FileNotFoundException;
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
		Corn.log("Initializing HTTP Server");
		try {
			server = HttpServer.create(new InetSocketAddress(Integer.parseInt(port)), MAX_WAITING_CONNECTIONS);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		server.setExecutor(null); // use the default executor
		
		Corn.log("Loading plugin");
		PluginFactory factory = new PluginFactory();
		try {
			factory.loadPlugins();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Corn.log("Creating contexts");
		createContexts();

		Corn.log("Starting server on port: " + port);
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
		server.createContext(IServer.GET_COMMANDS, new GetCommandsHandler());
		server.createContext(IServer.SEND_COMMAND, new CommandHandler());
		server.createContext(IServer.GET_CHAT, new GetChatHandler());
		server.createContext(IServer.SEND_CHAT, new SendChatHandler());
	}

	public static void main(String[] args) {
		String port = "8080";
		if(args.length == 1)
			port = args[0];
		new Corn("server.log");
		new ServerCommunicator(port);
	}
}
