package client;

import java.util.ArrayList;
import java.util.LinkedList;

public class CacheManager {
    private LinkedList<CacheData> lvlOne;
    private ArrayList<CacheData> lvlTwo;

    CacheManager() {
        lvlOne = new LinkedList<>();
        lvlOne.addFirst(new CacheData("2+2",4));
        lvlOne.addFirst(new CacheData("3+3",6));
        lvlTwo = new ArrayList<>();
        lvlTwo.add(new CacheData("50*2",100));
        lvlTwo.add(new CacheData("80+9",89));
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

    public void writeToLvlOne(CacheData data) {
        if(lvlOne.size() == 10) {
            CacheData last = lvlOne.getLast();
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
            writeToLvlOne(data);
        } else {
            CacheData data = lvlTwo.remove(index);
            writeToLvlOne(data);
        }
    }

    public void showCache() {

        System.out.println("Cache lvlOne:");
        for (CacheData aLvlOne : lvlOne) {
            aLvlOne.show();
        }

        System.out.println("Cache lvlTwo: ");
        for (CacheData aLvlTwo : lvlTwo) {
            aLvlTwo.show();
        }
        System.out.println("\n");

    }
}



