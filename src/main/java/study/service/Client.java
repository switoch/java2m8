package study.service;

public class Client {

    private String name;
    private long id;

    public Client(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }
}
