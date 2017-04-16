package com.example.groundcontrol;

import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IGameDTO;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class GameDAO implements IGameDAO {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");


    @Override
    public boolean create(IGameDTO dto) {
        return false;
    }

    @Override
    public boolean update(IGameDTO dto) {
        return false;
    }

    @Override
    public List<IGameDTO> getAll() {
        return null;
    }

    @Override
    public boolean delete(IGameDTO dto) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }
}
