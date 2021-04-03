package me.spasskopf.dimensionfinder;

import me.spasskopf.dimensionfinder.minecraft.DimensionHashHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A class which represents a task.
 * This task searches a certain range of Strings
 */
public class FinderTask {


    private final StringIterator iterator;
    /**
     * The Dimension ID we are looking for
     */
    private final int targetDimensionID;
    /**
     * A List with all matching strings
     */
    final List<String> found = new ArrayList<>();


    /**
     * Creates a new Findertask
     * @param iterator the Stringiterator which will be used
     * @param targetDimensionID the dimension ID we are looking for
     */
    public FinderTask(StringIterator iterator, int targetDimensionID) {
        this.iterator = iterator;
        this.targetDimensionID = targetDimensionID;
    }

    /**
     * Starts searching for the dimensionID
     */
    public void startSearching() {
        while (iterator.hasNext()) {
            final String next = iterator.next();
            if (DimensionHashHelper.getHash(next) == targetDimensionID) {
                found.add(next);
                System.out.println("Found Matching ID! String: " + next);
            }
        }
        System.out.println("Thread " + Thread.currentThread().getName() + " finished searching! Strings found: " + found.size());

    }
}
