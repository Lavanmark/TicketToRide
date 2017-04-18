package com.example.groundcontrol;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaylee on 4/14/2017.
 */

public class CommandDAO implements ICommandDAO {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    public boolean create(ICommandDTO dto) {
        String gameID = new StringBuffer(dto.getGameID()).toString();
        String ID = new StringBuffer(dto.getID()).toString();
        //String string = dto.getData();

        myRef.child(gameID).child(ID).setValue(dto);
        return true;
    }

    @Override
    public boolean update(ICommandDTO dto) {
        String gameID = new StringBuffer(dto.getGameID()).toString();
        String ID = new StringBuffer(dto.getID()).toString();

        myRef.child(gameID).child(ID).setValue(dto);
        return true;
    }

    @Override
    public List<ICommandDTO> getAll() {
        String gameID = new StringBuffer(dto.getGameID()).toString();
        List<ICommandDTO> list = new ArrayList<ICommandDTO>();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child: dataSnapshot.getChildren()){

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        })
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
