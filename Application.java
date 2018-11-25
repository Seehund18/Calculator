// Класс приложения

import client.Client;
import server.Server;

public class Application implements Runnable {

    private Server server;
    private Client client;

    public Application() {
        client = new Client();
        server = new Server();
    }

    @Override
    public void run() {
        client.run();
    }
}
