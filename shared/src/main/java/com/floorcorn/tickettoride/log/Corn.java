package com.floorcorn.tickettoride.log;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by Tyler on 3/2/2017.
 */

public class Corn {

	private static Logger logger;
	private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
	private static final Formatter formatter = new Formatter() {
		@Override
		public String format(LogRecord logRecord) {
			StringBuilder sb = new StringBuilder();
			sb.append(df.format(new Date(logRecord.getMillis()))).append(" -");
			sb.append(String.format("%1$8s", logRecord.getLevel())).append(": ");
			sb.append(formatMessage(logRecord));
			sb.append(System.getProperty("line.separator"));
			return sb.toString();
		}
	};

	public Corn(String file) {
		FileHandler fileHandler = null;
		try {
			fileHandler = new FileHandler(file, false);
			fileHandler.setFormatter(formatter);
		} catch(IOException e) {
			e.printStackTrace();
		}

		logger = Logger.getGlobal();

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(formatter);
		consoleHandler.setLevel(Level.ALL);

		if(fileHandler != null)
			logger.addHandler(fileHandler);
		logger.addHandler(consoleHandler);
		logger.setLevel(Level.ALL);
	}

	public static void log(Object o) {
		if(logger != null)
			logger.log(Level.FINE, o.toString());
	}

	public static void log(Level level, Object o) {
		if(logger != null)
			logger.log(level, o.toString());
	}
}
