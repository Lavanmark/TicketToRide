package com.example.groundcontrol;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;
import com.floorcorn.tickettoride.IDAOFactory;
import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IGameDTO;
import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.IUserDTO;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Tyler on 4/17/2017.
 */

public class FileSystemDAOFactory implements IDAOFactory {
	
	static final String FILEHEAD = "./plugins/.GCDB/";
	private static final String IDS = "id.id";
	
	static int getNextGameID() {
		File f = new File(FILEHEAD + IDS);
		int gameID = -1;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> map = mapper.readValue(readString(f), new TypeReference<Map<String, Integer>>(){});
			if(map.containsKey("GameID")) {
				gameID = map.get("GameID");
				map.put("GameID", gameID+1);
				writeString(mapper.writeValueAsString(map), f);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return gameID;
	}
	static int getNextUserID() {
		File f = new File(FILEHEAD + IDS);
		int userID = -1;
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Integer> map = mapper.readValue(readString(f), new TypeReference<Map<String, Integer>>(){});
			if(map.containsKey("UserID")) {
				userID = map.get("UserID");
				map.put("UserID", userID+1);
				writeString(mapper.writeValueAsString(map), f);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return userID;
	}
	
	static void writeString(String str, File file) {
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(str);
			fw.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	static String readString(File file) {
		try {
			Scanner s = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while(s.hasNextLine()) {
				sb.append(s.nextLine());
			}
			s.close();
			return sb.toString();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public FileSystemDAOFactory(){
		File f  = new File(FILEHEAD);
		if(!f.exists())
			f.mkdirs();
		f = new File(FILEHEAD + IDS);
		if(!f.exists()) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				Map<String, Integer> map = new HashMap<>();
				map.put("GameID", 0);
				map.put("UserID", 0);
				writeString(mapper.writeValueAsString(map), f);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public IUserDAO getUserDAOInstance() {
		return new UserDAO();
	}
	
	@Override
	public ICommandDAO getCommandDAOInstance() {
		return new CommandDAO();
	}
	
	@Override
	public IGameDAO getGameDAOInstance() {
		return new GameDAO();
	}
	
	@Override
	public IUserDTO getUserDTOInstance() {
		return new UserDTO();
	}
	
	@Override
	public ICommandDTO getCommandDTOInstance() {
		return new CommandDTO();
	}
	
	@Override
	public IGameDTO getGameDTOInstance() {
		return new GameDTO();
	}
	
	@Override
	public boolean startTransaction() {
		return true;
	}
	
	@Override
	public boolean endTransaction(boolean commit) {
		return true;
	}
}
