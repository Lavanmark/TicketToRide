package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.IDAOFactory;
import com.floorcorn.tickettoride.IGameDAO;
import com.floorcorn.tickettoride.IUserDAO;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Michael on 4/12/2017.
 */

public class RelationalDAOFactory implements IDAOFactory {

    /**************** Strings for creating and connecting to database **************************/
    private static final String DATABASe_URL = "jdbc:sqlite:." +
                                                File.separator + "sql" +
                                                File.separator + "database.db";
    private static final String SQL_CREATE_USERS = "CREATE TABLE IF NOT EXISTS users (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                + " Username text, \n"
                + " Password text, \n"
                + " FullName text \n"
                + ");";
    private static final String SQL_CREATE_CHECKPOINTS = "CREATE TABLE IF NOT EXISTS checkpoints (\n" +
                " GameID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                " Data text\n" +
                ");";
    private static final String SQL_CREATE_DELTAS = "CREATE TABLE IF NOT EXISTS deltas (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                + " GameID integer NOT NULL, \n"
                + " CmdID Integer NOT NULL, \n"
                + " Data text\n"
                + ");";
    /******************************************************************************************/

    public RelationalDAOFactory(){
        if(connectToDatabase())
            this.createDatabaseTables();
    }
    @Override
    public IUserDAO getUserDAOInstance() {
        return new UserDAO(DATABASe_URL);
    }

    @Override
    public ICommandDAO getCommandDAOInstance() {
        return new CommandDAO(DATABASe_URL);
    }

    @Override
    public IGameDAO getGameDAOInstance() {
        return new GameDAO(DATABASe_URL);
    }

    private boolean connectToDatabase(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(DATABASe_URL);
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex){
                System.out.println(ex.getMessage());
                return false;
            }
        }
        return true;
    }
    private void createDatabaseTables(){
        try(Connection connection = DriverManager.getConnection(DATABASe_URL);
            Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_USERS);
            statement.execute(SQL_CREATE_CHECKPOINTS);
            statement.execute(SQL_CREATE_DELTAS);
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
}
