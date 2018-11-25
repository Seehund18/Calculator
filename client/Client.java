package client;

import client.GUI.CalculatorGUI;
import client.GUI.StartingGUI;

public class Client implements Runnable {

    private CacheManager cache;
    private StartingGUI start;
    private CalculatorGUI calc;

    public Client() {
        cache = new CacheManager();
        calc = new CalculatorGUI(cache);
        start = new StartingGUI(calc);

    }

    @Override
    public void run() {
        start.setVisible(true);
    }
}
