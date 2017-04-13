package com.floorcorn.tickettoride.relational;

import com.floorcorn.tickettoride.ICommandDAO;
import com.floorcorn.tickettoride.ICommandDTO;

import java.io.Writer;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael on 4/12/2017.
 */

public class CommandDAO implements ICommandDAO {
    String databaseURL;
    Connection connection;

    public CommandDAO(String databaseURL){
        this.databaseURL = databaseURL;
    }
    @Override
    public boolean create(ICommandDTO dto) {
        String sql = "INSERT INTO deltas(GameID, CmdID, Data) VALUES(?,?,?)";

        try(PreparedStatement prepStmnt = this.connection.prepareStatement(sql)){
            prepStmnt.setInt(1,dto.getGameID());
            prepStmnt.setInt(2,dto.getID());
            prepStmnt.setString(3, dto.getData());

            prepStmnt.executeUpdate();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Method to update a command in the deltas table.
     * @param dto a dto with the new data and the GameID and CmdID for
     *            a command that already exists in the table.
     * @return boolean indicating whether or not the update occured.
     */
    @Override
    public boolean update(ICommandDTO dto) {
        String sql = "UPDATE deltas SET Data = ?"
                + " WHERE GameID = ? AND"
                + " CmdID = ?";

        try(PreparedStatement pstmt = this.connection.prepareStatement(sql)){

            pstmt.setString(1, dto.getData());
            pstmt.setInt(2, dto.getGameID());
            pstmt.setInt(3, dto.getID());

            pstmt.executeUpdate();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Method to return all entries in the deltas table
     * @return a list of ICommandDTO objects representing the deltas in the table.
     */
    @Override
    public List<ICommandDTO> getAll() {
        String sql = "SELECT GameID, CmdID, Data FROM deltas";
        try(Statement stmt = this.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(sql)){
            return parseResultSet(resultSet);
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private List<ICommandDTO> parseResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<ICommandDTO> dtoList = new ArrayList<>();
        while(resultSet.next()){
            CommandDTO dto = new CommandDTO();
            dto.setGameID(resultSet.getInt("GameID"));
            dto.setID(resultSet.getInt("CmdID"));
            dto.setData(resultSet.getString("Data"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean delete(ICommandDTO dto) {
        String sql = "DELETE FROM deltas WHERE GameID = ? AND CmdID = ?";

        try(PreparedStatement statement = this.connection.prepareStatement(sql)){
            statement.setInt(1, dto.getGameID());
            statement.setInt(2, dto.getID());

            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean startTransaction() {
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e)
        {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean endTransaction(boolean commit) {
        try {
            if (commit) {
                this.connection.commit();
            } else {
                this.connection.rollback();
            }
            this.connection.setAutoCommit(true);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean connect() {
        try {
            this.connection = makeConnection();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean clear() {
        String sql_drop = "DROP TABLE IF EXISTS deltas";
        String sql_create = "CREATE TABLE deltas (\n"
                + " GameID integer PRIMARY KEY NOT NULL,\n"
                + " CmdID Integer NOT NULL, \n"
                + " Data text\n"
                + ");";
        try(Statement stmt_drop = this.connection.createStatement();
            Statement stmt_create = this.connection.createStatement()){
            stmt_drop.execute(sql_drop);
            stmt_create.execute(sql_create);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    private Connection makeConnection() throws SQLException {
        Connection connection = null;
        connection = DriverManager.getConnection(databaseURL);
        return connection;
    }
}
