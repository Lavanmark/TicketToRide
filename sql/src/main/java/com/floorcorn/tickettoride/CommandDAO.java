package com.floorcorn.tickettoride;

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


    @Override
    public boolean create(ICommandDTO dto) {
        String sql = "INSERT INTO deltas(GameID, CmdID, Data) VALUES(?,?,?)";
    
        PreparedStatement prepStmnt = null;
        ResultSet resultSet = null;
        try{
            prepStmnt = RelationalDAOFactory.connection.prepareStatement(sql);
            prepStmnt.setInt(1,dto.getGameID());
            prepStmnt.setInt(2,dto.getID());
            prepStmnt.setString(3, dto.getData());

            if(prepStmnt.executeUpdate() == 1) {
    
                String idSql = "SELECT last_insert_rowid()";
                Statement stmt = RelationalDAOFactory.connection.createStatement();
                resultSet = stmt.executeQuery(idSql);
                int id = resultSet.getInt("last_insert_rowid()");
                dto.setID(id);
                return true;
            }
            return false;

        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }finally {
            RelationalDAOFactory.safeClose(prepStmnt);
            RelationalDAOFactory.safeClose(resultSet);
        }
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

        try(PreparedStatement pstmt = RelationalDAOFactory.connection.prepareStatement(sql)){

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
        String sql = "SELECT GameID, CmdID, Data FROM deltas ORDER BY CmdID";
        try(Statement stmt = RelationalDAOFactory.connection.createStatement();
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

        try(PreparedStatement statement = RelationalDAOFactory.connection.prepareStatement(sql)){
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
    public List<ICommandDTO> getAllForGame(int gameID) {
        String sql = "SELECT GameID, CmdID, Data FROM deltas WHERE GameID = ? ORDER BY CmdID";
        try(PreparedStatement stmt = RelationalDAOFactory.connection.prepareStatement(sql)){
            stmt.setInt(1, gameID);
            
            ResultSet resultSet = stmt.executeQuery();
            return parseResultSet(resultSet);
        }catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }
    
    @Override
    public boolean deleteAllForGame(int gameID) {
        String sql = "DELETE FROM deltas WHERE GameID = ?";
    
        try(PreparedStatement statement = RelationalDAOFactory.connection.prepareStatement(sql)){
            statement.setInt(1, gameID);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }


    @Override
    public boolean clear() {
        String sql_drop = "DROP TABLE IF EXISTS deltas";
        String sql_create = "CREATE TABLE deltas (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT NOT NULL, \n"
                + " GameID integer NOT NULL, \n"
                + " CmdID Integer NOT NULL, \n"
                + " Data text \n"
                + ");";
        try(Statement stmt_drop = RelationalDAOFactory.connection.createStatement();
            Statement stmt_create = RelationalDAOFactory.connection.createStatement()){
            stmt_drop.execute(sql_drop);
            stmt_create.execute(sql_create);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
