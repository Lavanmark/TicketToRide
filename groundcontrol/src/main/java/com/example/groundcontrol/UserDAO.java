package com.example.groundcontrol;

import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.IUserDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class UserDAO implements IUserDAO {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    public boolean create(IUserDTO dto) {
        return false;
    }

    @Override
    public boolean update(IUserDTO dto) {
        return false;
    }

    @Override
    public List<IUserDTO> getAll() {
        return null;
    }

    @Override
    public boolean delete(IUserDTO dto) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}
