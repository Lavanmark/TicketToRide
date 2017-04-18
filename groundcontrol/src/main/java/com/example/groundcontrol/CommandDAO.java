package com.example.groundcontrol;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class CommandDAO implements ICommandDAO {
    private static final String CMD_FOLDER = FileSystemDAOFactory.FILEHEAD + "cmdz/";
    
    public CommandDAO() {
        File f = new File(CMD_FOLDER);
        if(!f.exists())
            f.mkdir();
    }
    
    @Override
    public boolean create(ICommandDTO dto) {
        File f = new File(CMD_FOLDER + dto.getGameID() + "/" + dto.getID() + ".gcmd");
        if(f.exists())
            return false;
        try {
            if((f.getParentFile().exists() || f.getParentFile().mkdirs()) && f.createNewFile()) {
                FileSystemDAOFactory.writeString(dto.getData(), f);
                return true;
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    @Override
    public boolean update(ICommandDTO dto) {
        File f = new File(CMD_FOLDER + dto.getGameID() + "/" + dto.getID() + ".gcmd");
        if(f.exists()) {
            try {
                if(f.createNewFile()) {
                    FileSystemDAOFactory.writeString(dto.getData(), f);
                    return true;
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    @Override
    public List<ICommandDTO> getAll() {
        ArrayList<ICommandDTO> cmds = new ArrayList<>();
        File cdir = new File(CMD_FOLDER);
        for(File dir : cdir.listFiles()) {
            for(File f : dir.listFiles()) {
                if(f.isFile()) {
                    CommandDTO dto = new CommandDTO();
                    dto.setData(FileSystemDAOFactory.readString(f));
                    dto.setGameID(Integer.parseInt(dir.getName()));
                    dto.setID(Integer.parseInt(f.getName().split("\\.")[0]));
                    cmds.add(dto);
                }
            }
        }
        return cmds;
    }
    
    @Override
    public boolean delete(ICommandDTO dto) {
        File f = new File(CMD_FOLDER + dto.getGameID() + "/" + dto.getID() + ".gcmd");
        return f.exists() && f.delete();
    }
    
    @Override
    public List<ICommandDTO> getAllForGame(int gameID) {
        ArrayList<ICommandDTO> cmds = new ArrayList<>();
        File gdir = new File(CMD_FOLDER + gameID + "/");
        if(gdir.exists() && gdir.isDirectory()) {
            for(File f : gdir.listFiles()) {
                if(f.isFile()) {
                    CommandDTO dto = new CommandDTO();
                    dto.setData(FileSystemDAOFactory.readString(f));
                    dto.setGameID(gameID);
                    dto.setID(Integer.parseInt(f.getName().split("\\.")[0]));
                    cmds.add(dto);
                }
            }
        }
        return cmds;
    }
    
    @Override
    public boolean deleteAllForGame(int gameID) {
        File gdir = new File(CMD_FOLDER + gameID + "/");
        boolean success = true;
        for(File f : gdir.listFiles()) {
           if(!f.delete())
               success = false;
        }
        return success;
    }
    
    @Override
    public boolean clear() {
        File cdir  = new File(CMD_FOLDER);
        boolean success = true;
        for(File dir : cdir.listFiles()) {
            for(File f : dir.listFiles()) {
                if(f.isFile()) {
                    if(!f.delete())
                        success = false;
                }
            }
            if(!dir.delete())
            	success = false;
        }
        return success;
    }
}
