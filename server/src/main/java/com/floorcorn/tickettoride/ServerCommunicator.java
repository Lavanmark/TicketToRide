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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
	public ServerCommunicator(String port, int maxDeltas){
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
		//Set the DAOFactory in the server facade before contexts use the ServerFacade.
		ServerFacade.daoFactory = factory.getDAOFactory();
		ServerFacade.max_commands = maxDeltas;
		ServerFacade.getInstance(); //Loads the database stuff
		
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
		Options options = new Options();
		
		Option portOpt = new Option("p", "port", true, "server host port");
		portOpt.setRequired(false);
		options.addOption(portOpt);
		
		Option deltaOpt = new Option("d", "delta", true, "number of commands to be saved");
		deltaOpt.setRequired(false);
		options.addOption(deltaOpt);
		
		Option logFileOpt = new Option("l", "log", true, "log file name with .log extension");
		logFileOpt.setRequired(false);
		options.addOption(logFileOpt);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd;
		
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("utility-name", options);
			
			System.exit(1);
			return;
		}
		
		//Defaults
		int delta = -1;
		String port = "8080";
		String logFile = "server.log";
		
		//Get arguments
		if(cmd.getOptionValue("port") != null)
			port = cmd.getOptionValue("port");
		
		String deltaStr = cmd.getOptionValue("delta");
		if(deltaStr != null)
			delta = Integer.parseInt(deltaStr);
		if(cmd.getOptionValue("log") != null && cmd.getOptionValue("log").endsWith(".log"))
			logFile = cmd.getOptionValue("log");
		System.out.println("Log File: " + logFile);
		//Create Server and Log
		new Corn(logFile);
		new ServerCommunicator(port, delta);
	}
}
