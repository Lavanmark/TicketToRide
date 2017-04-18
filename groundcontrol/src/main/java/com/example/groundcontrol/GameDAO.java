package com.example.groundcontrol;

import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IGameDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class GameDAO implements IGameDAO {
    private static final String GAMES_FOLDER = FileSystemDAOFactory.FILEHEAD + "games/";
    
    public GameDAO() {
        File f = new File(GAMES_FOLDER);
        if(!f.exists())
            f.mkdir();
    }
	
	@Override
	public boolean create(IGameDTO dto) {
		int id = FileSystemDAOFactory.getNextGameID();
		File f = new File(GAMES_FOLDER + id + ".game");
		if(f.exists())
			return false;
		try {
			if(f.createNewFile()) {
				dto.setID(id);
				FileSystemDAOFactory.writeString(dto.getData(), f);
				return true;
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean update(IGameDTO dto) {
		File f = new File(GAMES_FOLDER + dto.getID() + ".game");
		if(f.exists()) {
			FileSystemDAOFactory.writeString(dto.getData(), f);
			return true;
		}
		return false;
	}
	
	@Override
	public List<IGameDTO> getAll() {
		ArrayList<IGameDTO> games = new ArrayList<>();
		File udir = new File(GAMES_FOLDER);
		for(File f : udir.listFiles()) {
			if(f.isFile()) {
				GameDTO dto = new GameDTO();
				dto.setData(FileSystemDAOFactory.readString(f));
				dto.setID(Integer.parseInt(f.getName().split("\\.")[0]));
				games.add(dto);
			}
		}
		return games;
	}
	
	@Override
	public boolean delete(IGameDTO dto) {
		File f = new File(GAMES_FOLDER + dto.getID() + ".game");
		return f.exists() && f.delete();
	}
	
	@Override
	public boolean clear() {
		File file  = new File(GAMES_FOLDER);
		boolean success = true;
		for(File f : file.listFiles()) {
			if(f.isFile()) {
				if(!f.delete())
					success = false;
			}
		}
		return success;
	}
}
