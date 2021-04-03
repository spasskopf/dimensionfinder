package me.spasskopf.dimensionfinder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Test Class
 */
@Deprecated
public class Test {


    private static final int AMOUNT = 2000;

    /**
     * Test Method
     *
     * @param args program arguments
     */
    public static void main(String[] args) {
        final char[] chars = getChars();
        final int length = 40;
        final int cl = chars.length;


        int[] last = new int[length];
        Arrays.fill(last, cl - 1);
        StringIterator it = new StringIterator(chars, length, last);
        FinderTask task = new FinderTask(it, 3);

        task.startSearching();
        System.out.println("Finished searching!");
        task.found.forEach(System.out::println);
    }

    private static double durchschnitt(List<Long> time) {
        double value = 0;
        for (Long aLong : time) {
            value += aLong;
        }
        return value / (double) time.size();
    }

    private static char[] getChars() {
        return new char[]{
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '};
    }

}
