package com.floorcorn.tickettoride;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Michael on 4/12/2017.
 */

public class RelationalDAOFactory implements IDAOFactory {

    /**************** Strings for creating and connecting to database **************************/
    private final String DATABASE_URL = "jdbc:sqlite:." +
                                                File.separator + "plugins" +
                                                File.separator + "db" +
                                                File.separator + "database.db";
    private final String SQL_CREATE_USERS = "CREATE TABLE IF NOT EXISTS users (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                + " Username text, \n"
                + " Password text, \n"
                + " FullName text \n"
                + ");";
    private final String SQL_CREATE_CHECKPOINTS = "CREATE TABLE IF NOT EXISTS checkpoints (\n" +
                " GameID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n" +
                " Data text\n" +
                ");";
    private final String SQL_CREATE_DELTAS = "CREATE TABLE IF NOT EXISTS deltas (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                + " GameID integer NOT NULL, \n"
                + " CmdID Integer NOT NULL, \n"
                + " Data text\n"
                + ");";
    
    static Connection connection;
    
    /******************************************************************************************/

    public RelationalDAOFactory(){
        File f = new File("." + File.separator + "plugins" + File.separator + "db");
        if(!f.exists())
            f.mkdirs();
        
        startTransaction();
        this.createDatabaseTables();
        endTransaction(true);
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
        try {
            assert(connection == null);
            connection = DriverManager.getConnection(DATABASE_URL);
            connection.setAutoCommit(false);
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public boolean endTransaction(boolean commit) {
        if(connection != null){
            try{
                if(commit){
                    connection.commit();
                }else{
                    connection.rollback();
                }
            }catch(SQLException e){
                System.err.println("Could not end transaction");
                e.printStackTrace();
                return false;
            }finally{
                safeClose(connection);
                connection = null;
            }
            return true;
        }
        return false;
    }
    
    private void createDatabaseTables(){
        try(Connection connection = DriverManager.getConnection(DATABASE_URL);
            Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_USERS);
            statement.execute(SQL_CREATE_CHECKPOINTS);
            statement.execute(SQL_CREATE_DELTAS);
        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }
    
    public static void safeClose(Connection conn){
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                
            }
        }
    }
    
    public static void safeClose(Statement stmt){
        if(stmt != null){
            try{
                stmt.close();
            }catch(SQLException e){
                
            }
        }
    }
    
    public static void safeClose(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (SQLException e) {
                // ...
            }
        }
    }
    
    public static void safeClose(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (SQLException e) {
                // ...
            }
        }
    }
}
