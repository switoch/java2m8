package study;

import study.config.Database;
import study.service.Client;
import study.service.ClientService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        Connection connection = Database.getInstance().getPostgresConnection();
        ClientService clientService = new ClientService(connection);
        clientService.create("Bob Rick");
        System.out.println("id 6: " + clientService.getById(6));
        clientService.setName(6, "Bob C. Rick");
        List<Client> clients = clientService.listAll();
        System.out.println("array size = " + clients.size());
        for (Client client : clients) {
            System.out.println("client: " + client.getName() + ", id = " + client.getId());
        }
        clientService.deleteById(6);

    }
}
