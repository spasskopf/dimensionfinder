package me.spasskopf.dimensionfinder;

import me.spasskopf.dimensionfinder.minecraft.DimensionHashHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * A class which represents a task.
 * This task searches a certain range of Strings
 */
public class FinderTask {


    private final StringIterator iterator;
    /**
     * how many strings should be searched
     */
    private final double amount;
    /**
     * The Dimension IDs we are looking for
     */
    private final List<Integer> targetDimensionIDs;
    /**
     * A Map with all matching strings
     */
    final Map<Integer, List<String>> found = new ConcurrentHashMap<>();
    /**
     * How many combinations have been searched
     */
    private volatile double searched = 0;


    /**
     * Creates a new Findertask
     *
     * @param iterator          the Stringiterator which will be used
     * @param amount            how many strings should be searched
     * @param targetDimensionID the dimension ID we are looking for
     */
    public FinderTask(final StringIterator iterator, final double amount, final int... targetDimensionID) {
        this.iterator = iterator;
        this.amount = amount;
        this.targetDimensionIDs = new ArrayList<>();
        for (final int i : targetDimensionID) {
            this.targetDimensionIDs.add(i);
        }

    }

    /**
     * Starts searching for the dimensionID
     */
    public void startSearching() {
        //Initialize
        for (final int i : targetDimensionIDs) {
            found.put(i, new ArrayList<>());
        }

        System.out.printf("Thread %s: First Element: %s%n", Thread.currentThread().getName(),iterator.next());

        while (iterator.hasNext() && searched < amount) {
            searched++;
            final String text = iterator.next();
            final int hash = DimensionHashHelper.getHash(text);
            if (targetDimensionIDs.contains(hash)) {
                found.get(hash).add(text);
                System.out.printf("Thread %s found Matching ID! String: %s Hash: %d%n", Thread.currentThread().getName(), text, hash);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished searching! Strings found: " +
                found.entrySet().stream()
                        .filter(x -> !x.getValue().isEmpty())
                        .map(x -> x.getKey() + ": [" + Arrays.toString(x.getValue().toArray(new String[0])) + "]")
                        .collect(Collectors.joining(" "))
        );

    }

    public double getSearched() {
        return searched;
    }

    public double getAmount() {
        return amount;
    }
}
