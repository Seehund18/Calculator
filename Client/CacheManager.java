import java.util.ArrayList;
import java.util.LinkedList;

public class CacheManager {

    private static LinkedList<CacheData> lvlOne;
    private static ArrayList<CacheData> lvlTwo;
    private CacheCleaner cleaner;

    CacheManager() {

        lvlOne = new LinkedList<>();
        lvlTwo = new ArrayList<>();
        CacheCleaner cleaner = new CacheCleaner(lvlTwo);

        Thread threadClean = new Thread(cleaner);
        threadClean.setDaemon(true);
        threadClean.start();
    }

    public double getFirstResult() {
        return lvlOne.peekFirst().getResult();
    }

    public boolean isExpressionInCache(String value) {

        int index = 0;
        for (CacheData i: lvlOne) {
            if (i.getExpression().equals(value)) {
                moveToBeginning(index, true);
                return true;
            }
            index++;
        }

        index = 0;
        for (CacheData i: lvlTwo) {
            if (i.getExpression().equals(value)) {
                moveToBeginning(index, false);
                return true;
            }
            index++;
        }
        return false;
    }

    public void writeToCache(CacheData data) {

        if(lvlOne.size() == 10) {
            CacheData last = lvlOne.getLast();
            lvlOne.removeLast();
            writeToLvlTwo(last);
        }
        lvlOne.addFirst(data);
    }

    private void writeToLvlTwo(CacheData data) {
        lvlTwo.add(data);
    }

    private void moveToBeginning(int index, boolean isFirstLevel) {

        if (isFirstLevel) {
            CacheData data = lvlOne.remove(index);
            writeToCache(data);
        } else {
            CacheData data = lvlTwo.remove(index);
            writeToCache(data);
        }
    }

    public static void showCache() {

        System.out.println("Cache lvlOne:");
        int i = 1;
        for (CacheData aLvlOne : lvlOne) {
            System.out.print(i++ + ")");
            aLvlOne.show();
        }

        System.out.println();

        System.out.println("Cache lvlTwo: ");
        i = 1;
        for (CacheData aLvlTwo : lvlTwo) {
            System.out.print(i++ + ") ");
            aLvlTwo.show();
        }
        System.out.println("\n");
    }
}