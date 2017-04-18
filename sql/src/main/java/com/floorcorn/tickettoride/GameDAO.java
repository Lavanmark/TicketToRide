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

public class GameDAO implements IGameDAO {


    @Override
    public boolean create(IGameDTO dto) {
        String sql = "INSERT INTO checkpoints(Data) VALUES(?)";

        try(PreparedStatement statement = RelationalDAOFactory.connection.prepareStatement(sql)){
            statement.setString(1, dto.getData());

            statement.executeUpdate();

            String idSql = "SELECT last_insert_rowid()";
            Statement stmt = RelationalDAOFactory.connection.createStatement();
            ResultSet resultSet = stmt.executeQuery(idSql);
            int id = resultSet.getInt("last_insert_rowid()");
            dto.setID(id);
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(IGameDTO dto) {
        String sql = "UPDATE checkpoints SET Data = ?"
                + " WHERE GameID = ?";

        try(PreparedStatement statement = RelationalDAOFactory.connection.prepareStatement(sql)){
            statement.setString(1, dto.getData());
            statement.setInt(2, dto.getID());

            statement.executeUpdate();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public List<IGameDTO> getAll() {
        String sql = "SELECT GameID, Data FROM Checkpoints ORDER BY GameID";
        try(Statement statement = RelationalDAOFactory.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            return parseResultSet(resultSet);
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private List<IGameDTO> parseResultSet(ResultSet resultSet) throws SQLException {
        ArrayList<IGameDTO> dtoList = new ArrayList<>();
        while(resultSet.next()){
            GameDTO dto = new GameDTO();
            dto.setID(resultSet.getInt("GameID"));
            dto.setData(resultSet.getString("Data"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean delete(IGameDTO dto) {
        String sql = "DELETE FROM checkpoints WHERE GameID = ?";

        try(PreparedStatement statement = RelationalDAOFactory.connection.prepareStatement(sql)){
            statement.setInt(1, dto.getID());

            statement.executeUpdate();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean clear() {
        String sql_drop = "DROP TABLE IF EXISTS checkpoints";
        String sql_create = "CREATE TABLE checkpoints (\n" +
                " GameID integer PRIMARY KEY NOT NULL, \n" +
                " Data text\n" +
                ");";
        try(Statement statement_drop = RelationalDAOFactory.connection.createStatement();
            Statement statement_create = RelationalDAOFactory.connection.createStatement()){
            statement_drop.execute(sql_drop);
            statement_create.execute(sql_create);
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }
}
