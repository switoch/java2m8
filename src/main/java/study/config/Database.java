package study.config;

import org.flywaydb.core.Flyway;
import study.properties.PropertyReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final Database INSTANCE = new Database();
    private Connection Connection;

    private Database() {
        //Initialize connection with DB
        try {
            String connectionUrl = PropertyReader.getConnectionUrlForPostgres();
            String username = PropertyReader.getUserForPostgres();
            String password = PropertyReader.getPasswordForPostgres();
            this.Connection = DriverManager.getConnection(connectionUrl, username, password);
            flywayMigration(connectionUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException("Create connection exception");
        }
    }

    public static Database getInstance() {
        return INSTANCE;
    }

    public Connection getPostgresConnection() {
        return Connection;
    }

    /* Flyway */
    private void flywayMigration(String connectionUrl, String username, String password) {
        Flyway flyway = Flyway.configure().dataSource(connectionUrl, username, password).load();
        flyway.migrate();
    }

}
