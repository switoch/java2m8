package study.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientService {

    private static final String INSERT_STRING = "INSERT INTO client (id, name) OVERRIDING SYSTEM VALUE VALUES (? ,?)";
    private static final String SELECT_BY_ID_STRING = "SELECT id, name FROM client WHERE id = ?";
    private static final String SELECT_ALL_STRING = "SELECT * FROM client";
    private static final String UPDATE_USER_STRING = "UPDATE client SET name = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM client WHERE id = ?";

    private static final String SELECT_LAST_USER_STRING = "SELECT * FROM client WHERE id IN (SELECT MAX(id) FROM client)";

    private Connection connection;
    private PreparedStatement insertStatement;
    private PreparedStatement selectByStatement;
    private PreparedStatement selectAllStatement;
    private PreparedStatement updateStatement;
    private PreparedStatement deleteStatement;
    private PreparedStatement selectLastStatement;
    private Client client;

    public ClientService(Connection connection) {
        this.connection = connection;
        try {
            this.insertStatement = connection.prepareStatement(INSERT_STRING);
            this.selectByStatement = connection.prepareStatement(SELECT_BY_ID_STRING);
            this.selectAllStatement = connection.prepareStatement(SELECT_ALL_STRING);
            this.updateStatement = connection.prepareStatement(UPDATE_USER_STRING);
            this.deleteStatement = connection.prepareStatement(DELETE_USER);
            this.selectLastStatement = connection.prepareStatement(SELECT_LAST_USER_STRING);
        } catch (SQLException e) {
            System.out.println("User Service construction exception. Reason: " + e.getMessage());
        }
    }

    // Add a new client, return his id
    public long create(String name) throws SQLException {
        try {
            long id = 0;
            ResultSet resultSet = this.selectLastStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("id") + 1;
            }
            this.insertStatement.setString(2, name);
            this.insertStatement.setLong(1, id);
            return this.insertStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Create a user exception. Reason: " + e.getMessage());
            return -1;
        }
    }

    // Return client's name by id
    public String getById(long id) {
        try {
            this.selectByStatement.setLong(1, id);
            ResultSet resultSet = this.selectByStatement.executeQuery();
            if (resultSet.next()) {
                this.client = new Client(resultSet.getString("name"),
                        resultSet.getLong("id"));
            }
            return this.client.getName();
        } catch (SQLException e) {
            System.out.println("Select user exception. Reason: " + e.getMessage());
            return null;
        }
    }

    //Set new name to a client with defined id
    public void setName(long id, String name) {
        try {
            this.updateStatement.setString(1, name);
            this.updateStatement.setLong(2, id);
            this.updateStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Update user exception. Reason: " + e.getMessage());
        }
    }

    // Delete a client with defined id
    public void deleteById(long id) {
        try {
            this.deleteStatement.setLong(1, id);
            this.deleteStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Delete user exception. Reason: " + e.getMessage());
        }
    }

    //Returns all clients
    public List<Client> listAll() {
        try {
            List<Client> clients = new ArrayList<>();
            ResultSet resultSet = this.selectAllStatement.executeQuery();
            while (resultSet.next()) {
                Client client = new Client(resultSet.getString("name"), resultSet.getLong("id"));
                clients.add(client);
            }
            return clients;
        } catch (SQLException e) {
            System.out.println("Select all user exception. Reason: " + e.getMessage());
            return null;
        }
    }
}
