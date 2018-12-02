import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CacheCleaner implements Runnable {

    private ArrayList<CacheData> lvlTwo;

    CacheCleaner(ArrayList<CacheData> lvlTwo) {
        this.lvlTwo = lvlTwo;
    }

    @Override
    public void run() {
        List a = Collections.synchronizedList(lvlTwo);
        while (true) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (a.size() > 10) {
                a.remove(0);
            }
        }
    }
}
