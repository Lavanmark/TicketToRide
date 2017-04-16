package com.example.groundcontrol;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class CommandDAO implements ICommandDAO {
   // private DatabaseReference mDatabase;
   // private String mUserId;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    @Override
    public boolean create(ICommandDTO dto) {

        return false;
    }

    @Override
    public boolean update(ICommandDTO dto) {
        return false;
    }

    @Override
    public List<ICommandDTO> getAll() {
        return null;
    }

    @Override
    public boolean delete(ICommandDTO dto) {
        return false;
    }

    @Override
    public List<ICommandDTO> getAllForGame(int gameID) {
        return null;
    }

    @Override
    public boolean deleteAllForGame(int gameID) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}
