package com.floorcorn.tickettoride;

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

public class UserDAO implements IUserDAO {

    private String databaseURL;
    private Connection connection;

    public UserDAO(String databaseURL){
        this.databaseURL = databaseURL;
    }
    @Override
    public boolean create(IUserDTO dto) {
        String sql = "INSERT INTO Users(Username, Password, FullName) VALUES(?,?,?)";

        try(PreparedStatement preparedStatement = this.connection.prepareStatement(sql)){
            preparedStatement.setString(1, dto.getUserName());
            preparedStatement.setString(2, dto.getPassword());
            preparedStatement.setString(3, dto.getFullName());

            preparedStatement.executeUpdate();
            String idSql = "SELECT last_insert_rowid()";
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(idSql);
            int id = resultSet.getInt("last_insert_rowid()");
            dto.setID(id);

        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Updates a user in the users table.
     * @param dto a dto with the new data and the same auto generated id for the
     *            user to be updated.
     * @return boolean indicating whether or not update occured.
     */
    @Override
    public boolean update(IUserDTO dto) {
        String sql = "UPDATE users SET Username = ? ,"
                + " Password = ?,"
                + " FullName = ? "
                + " WHERE ID = ?";

        try(PreparedStatement preparedStatement = this.connection.prepareStatement(sql)){
            preparedStatement.setString(1, dto.getUserName());
            preparedStatement.setString(2, dto.getPassword());
            preparedStatement.setString(3, dto.getFullName());
            preparedStatement.setInt(4, dto.getID());
            preparedStatement.executeUpdate();
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Method to return all Users in the users table
     * @return a list of UserDTO objects representing all users currently stored in table.
     */
    @Override
    public List<IUserDTO> getAll() {
        String sql = "SELECT ID, Username, Password, FullName FROM users ORDER BY ID";

        try(Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)){
            return parseResultsSet(resultSet);
        } catch (SQLException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private List<IUserDTO> parseResultsSet(ResultSet resultSet) throws SQLException {
        ArrayList<IUserDTO> dtoList = new ArrayList<>();
        while(resultSet.next()){
            UserDTO dto = new UserDTO();
            dto.setID(resultSet.getInt("ID"));
            dto.setUserName(resultSet.getString("Username"));
            dto.setPassword(resultSet.getString("Password"));
            dto.setFullName(resultSet.getString("FullName"));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public boolean delete(IUserDTO dto) {
        String sql = "DELETE FROM users WHERE ID = ?";

        try(PreparedStatement statement = this.connection.prepareStatement(sql)){
            statement.setInt(1, dto.getID());

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
        String sql_drop = "DROP TABLE IF EXISTS users";
        String sql_create = "CREATE TABLE users (\n"
                + " ID integer PRIMARY KEY AUTOINCREMENT,\n"
                + " Username text, \n"
                + " Password text, \n"
                + " FullName text\n"
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
