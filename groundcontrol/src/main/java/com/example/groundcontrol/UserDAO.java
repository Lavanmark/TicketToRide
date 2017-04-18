package com.example.groundcontrol;

import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.IUserDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class UserDAO implements IUserDAO {
    private static final String USERS_FOLDER = FileSystemDAOFactory.FILEHEAD + "users/";

    public UserDAO() {
        File f = new File(USERS_FOLDER);
        if(!f.exists())
            f.mkdir();
    }
    
    
    
    @Override
    public boolean create(IUserDTO dto) {
	    int id = FileSystemDAOFactory.getNextUserID();
        File f = new File(USERS_FOLDER + id + ".usr");
	    if(f.exists())
	    	return false;
	    try {
		    if(f.createNewFile()) {
			    dto.setID(id);
			    String string = Serializer.getInstance().serialize(dto);
			    FileSystemDAOFactory.writeString(string, f);
			    return true;
		    }
	    } catch(IOException e) {
		    e.printStackTrace();
	    }
	    return false;
    }

    @Override
    public boolean update(IUserDTO dto) {
	    File f = new File(USERS_FOLDER + dto.getID() + ".usr");
	    if(f.exists()) {
		    try {
			    if(f.createNewFile()) {
				    String string = Serializer.getInstance().serialize(dto);
				    FileSystemDAOFactory.writeString(string, f);
				    return true;
			    }
		    } catch(IOException e) {
			    e.printStackTrace();
		    }
	    }
	    return false;
    }

    @Override
    public List<IUserDTO> getAll() {
	    ArrayList<IUserDTO> users = new ArrayList<>();
	    File udir = new File(USERS_FOLDER);
	    for(File f : udir.listFiles()) {
		    if(f.isFile()) {
			    String str = FileSystemDAOFactory.readString(f);
			    users.add(Serializer.getInstance().deserializeUserDTO(str));
		    }
	    }
	    return users;
    }

    @Override
    public boolean delete(IUserDTO dto) {
	    File f = new File(USERS_FOLDER + dto.getID() + ".usr");
	    return f.exists() && f.delete();
    }

    @Override
    public boolean clear() {
        File file  = new File(USERS_FOLDER);
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
