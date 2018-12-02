import java.io.IOException;

public class MainClient {

    private static CacheManager cache;
    private static StartingGUI start;
    private static  CalculatorGUI calc;
    private static final Object lock = new Object();


    private static void clientInitialize() {

        cache = new CacheManager();
        calc = new CalculatorGUI(cache, lock);
        start = new StartingGUI(calc, lock);
    }

    public static void main(String[] args) throws InterruptedException {

        if(!ClientServerConnector.connectToServer()) {
            System.out.println("Не удалось подключиться к серверу");
            return;
        }

        clientInitialize();
        start.setVisible(true);

        // Дополнительный поток, которого ждёт поток main, чтобы закрыться.
        // По итогу, main дождёться пока все графические интерфейсы закроются.
        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
        t.join();

        System.out.println("Клиент закрывается");
        ClientServerConnector.sendRequest(ClientServerConnector.CLOSE);
        try {
            ClientServerConnector.closeClient();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Клиент закрыт");

    }


}
