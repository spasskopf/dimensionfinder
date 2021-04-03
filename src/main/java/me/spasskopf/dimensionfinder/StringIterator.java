package me.spasskopf.dimensionfinder;

import java.util.Arrays;
import java.util.Iterator;

/**
 * An Iterator which allows you to get all possible combinations of a string as iterator
 * <p>
 * Uses an Integer Array internally.
 */
public class StringIterator implements Iterator<String> {
    /**
     * All possible Characters<br>
     * For Example:<br>
     * If you want this String:<br>
     * First possible combination: "aaa"<br>
     * Last possible combination: "ccc"<br>
     * And between that "aab", "aac", "aba", "abb", "abc", "aca", ...<br>
     * There are only three characters: 'a', 'b' and 'c'<br>
     * So {@code possibleChars} is {@code new char[]{'a', 'b', 'c'}}
     */
    final char[] possibleChars;

    /**
     * The String as int[].<br>
     * value 0 is qual to the char with index zero in {@link #possibleChars}<br>
     * <br>
     * For Example:<br>
     * possibleChars is {@code char[]{'a', 'b', 'c'}}<br>
     * so position {@code {0, 0, 0}} would be "a a a"<br>
     * Position {@code {1, 2, 0}} would be "b c a"<br>
     * Position {@code {2, 2, 2}} would be "c c c"<br>
     */
    final int[] position;

    /**
     * The last String in the iterator. This value is not going to be in the iterator!
     */
    final int[] target;

    /**
     * The maximum amount of each element
     */
    final int max;

    /**
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param target        the last value
     * @param position      the starting position
     * @throws IllegalArgumentException if position and target length are not equal
     */
    public StringIterator(char[] possibleChars, int[] position, int[] target) throws IllegalArgumentException {
        this.possibleChars = possibleChars;
        this.position = position;
        this.max = possibleChars.length - 1;
        this.target = target;
        if (position.length != target.length) {
            throw new IllegalArgumentException(String.format("Position length is not the same as target length! (%d / %d)", position.length, target.length));
        }

    }

    /**
     * Constructs a StringIterator from the given String
     *
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param string        the string
     * @param target        the last value
     * @throws IllegalArgumentException if the string contains characters not in the list
     */
    public StringIterator(char[] possibleChars, String string, int[] target) throws IllegalArgumentException {
        this.possibleChars = possibleChars;
        this.max = possibleChars.length - 1;

        this.position = new int[string.length()];
        for (int i = 0; i < position.length; i++) {
            position[i] = possibleChars[indexOf(string.charAt(i), possibleChars)];
        }
        this.target = target;

    }

    /**
     * Constructs a new StringIterator with position Zero
     *
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param length        the length of the string
     * @param target        the last element
     */
    public StringIterator(char[] possibleChars, int length, int[] target) {
        this(possibleChars, new int[length], target);
    }


    @Override
    public boolean hasNext() {
        for (int i = 0; i < position.length; i++) {
            if (position[i] != target[i]) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String next() {
        //Initialize before incrementing
        final String asString = asString();
        position[position.length - 1]++;

        if (position[position.length - 1] > max) {
            //Reset Digit
            position[position.length - 1] = 0;
            //Increment the other digits
            for (int i = position.length - 2; i >= 0; i--) {
                position[i]++;
                //If the element is not max, no need to increment other digits
                if (position[i] <= max) {
                    break;
                } else {
                    //Reset Digit
                    position[i] = 0;
                }
            }
        }


        return asString;
    }

    /**
     * @return The current value as String
     */
    public String asString() {
        final StringBuilder builder = new StringBuilder(position.length);
        for (int i : position) {
            builder.append(possibleChars[i]);
        }
        return builder.toString();
    }

    /**
     * @param character     the character to look for
     * @param possibleChars the array with all characters
     * @return the first index of the character in the array
     * @throws IllegalArgumentException if the array does not contain the character
     */
    private int indexOf(char character, char[] possibleChars) throws IllegalArgumentException {
        for (int i = 0; i < possibleChars.length; i++) {
            if (possibleChars[i] == character) {
                return i;
            }
        }
        throw new IllegalArgumentException("Array does not contain Character!");
    }
}
