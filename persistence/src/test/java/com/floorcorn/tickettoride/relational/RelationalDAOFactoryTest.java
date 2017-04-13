package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.IUserDAO;
import com.floorcorn.tickettoride.IUserDTO;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael on 4/13/2017.
 */
public class RelationalDAOFactoryTest {
    RelationalDAOFactory DAOFactory;
    @Before
    public void setup(){
        DAOFactory = new RelationalDAOFactory();
        /*DAOFactory.getCommandDAOInstance().connect();
        DAOFactory.getCommandDAOInstance().clear();
        DAOFactory.getGameDAOInstance().connect();
        DAOFactory.getGameDAOInstance().clear();
        DAOFactory.getUserDAOInstance().connect();
        DAOFactory.getUserDAOInstance().clear();*/
    }
    @Test
    public void getUserDAOInstance() throws Exception {
        IUserDAO userDAO = DAOFactory.getUserDAOInstance();
        userDAO.connect();
        userDAO.clear();

        IUserDTO userDTO = new UserDTO();
        userDTO.setUserName("Bobby");
        userDTO.setPassword("password");
        userDTO.setFullName("Bobby Jones");

        /** Test add user **/
        assertTrue(userDAO.create(userDTO));

        ArrayList<IUserDTO> allUsers = (ArrayList) userDAO.getAll();

        assertTrue(allUsers.size() == 1);
        assertEquals("Bobby", allUsers.get(0).getUserName());
        assertEquals("password", allUsers.get(0).getPassword());
        assertEquals("Bobby Jones", allUsers.get(0).getFullName());

        /** Test update user **/
        userDTO.setUserName("Billy");
        userDTO.setFullName("Billy Jones");
        userDTO.setPassword("passwerd");

        assertTrue(userDAO.update(userDTO));
        assertEquals(1,userDAO.getAll().size());
        assertEquals("Billy", userDAO.getAll().get(0).getUserName());
        assertEquals("Billy Jones", userDAO.getAll().get(0).getFullName());
        assertEquals("passwerd", userDAO.getAll().get(0).getPassword());

        /** Test delete user **/
        assertTrue(userDAO.delete(userDTO));
        assertEquals(0, userDAO.getAll().size());

        /** Test insert 2 users **/
        UserDTO dto = new UserDTO();
        dto.setUserName("Ashley");
        dto.setFullName("Ashley Johnson");
        dto.setPassword("ashleyspassword");

        assertTrue(userDAO.create(dto));
        assertTrue(userDAO.create(userDTO));

        ArrayList<IUserDTO> results = (ArrayList) userDAO.getAll();
        assertEquals(2, results.size());
        assertEquals("Ashley", results.get(0).getUserName());
        assertEquals("Billy", results.get(1).getUserName());

        /** Test clear **/
        assertTrue(userDAO.clear());
        assertEquals(0, userDAO.getAll().size());
    }

    @Test
    public void getCommandDAOInstance() throws Exception {
        ICommandDAO dao = DAOFactory.getCommandDAOInstance();
        dao.connect();
        CommandDTO dto = new CommandDTO();

        /** Test basic single command stored **/
        dto.setData("Test cmd data.");
        dto.setGameID(1);
        dto.setID(1);

        assertTrue(dao.create(dto));
        assertEquals(1, dao.getAll().size());
        assertEquals("Test cmd data.", dao.getAll().get(0).getData());
        assertEquals(1, dao.getAll().get(0).getGameID());
        assertEquals(1, dao.getAll().get(0).getID());

        /** Test clear **/
        assertTrue(dao.clear());
        assertEquals(0, dao.getAll().size());

        /** Test multiple commands inserted **/
        CommandDTO secondDto = new CommandDTO();
        secondDto.setData("This is more data in another command");
        secondDto.setGameID(1);
        secondDto.setID(2);

        assertTrue(dao.create(dto));
        assertTrue(dao.create(secondDto));
        assertEquals(2, dao.getAll().size());
        assertEquals(1, dao.getAll().get(0).getID());
        assertEquals(2, dao.getAll().get(1).getID());
        assertEquals("This is more data in another command", dao.getAll().get(1).getData());

        /** Test update a stored command **/
        secondDto.setData("The data in this command has been updated.");

        assertTrue(dao.update(secondDto));
        assertEquals("The data in this command has been updated.", dao.getAll().get(1).getData());

        /** Test store command with a lot of data **/
        CommandDTO longDatadto = new CommandDTO();
        longDatadto.setData("Dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg");
        longDatadto.setGameID(2);
        longDatadto.setID(1);

        assertTrue(dao.create(longDatadto));
        assertEquals(1, dao.getAll().size());
        assertEquals("Dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" +
                "ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff" +
                "eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" +
                "gggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggggg", dao.getAll().get(0).getData());
        assertEquals(1, dao.getAll().get(0).getGameID());
        assertEquals(1, dao.getAll().get(0).getID());

    }

    @Test
    public void getGameDAOInstance() throws Exception {
        assert(true);
    }

}