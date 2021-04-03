package me.spasskopf.dimensionfinder;

import java.util.Iterator;

/**
 * An Iterator which allows you to get all possible combinations of a string as iterator
 * <p>
 * Uses an Integer Array internally.
 */
public class StringIterator implements Iterator<String> {

    /**
     * Constructs a StringIterator with the given offset
     *
     * @param offset     the offset. zero means no offset = default iterator
     * @param textLength the length of the text
     * @return the created iterator
     * @throws IllegalArgumentException if the offset is bigger than the number of possible combinations
     */
    public static StringIterator getIteratorFromOffset(final double offset, final int textLength, final char[] possibleChars) throws IllegalArgumentException {
        assert possibleChars.length > 0;
        assert textLength > 0;


        final int max = possibleChars.length;

        final double[] position = new double[textLength];
        position[textLength - 1] = offset;
        //Only change other digits if the value is bigger than the max value
        //Using possiblechars.length -1 instead of max because max might be 1 when possiblechars.length == 2
        if (offset > possibleChars.length - 1) {
            for (int i = position.length - 1; i >= 0; i--) {
                final double increment = Math.floor(position[i] / max);
                position[i] %= max;

                if (increment != 0 && i > 0) {
                    position[i - 1] += increment;
                } else if (i == 0 && increment != 0) {
                    throw new IllegalArgumentException("Offset is bigger than the number of possible combinations!");
                }
            }
        }
        return new StringIterator(possibleChars, position);
    }

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
    private final char[] possibleChars;

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
    private final double[] position;

    /**
     * The maximum amount of each element
     */
    private final int max;

    /**
     * The length of position
     * Equal to {@code position.length}
     */
    private final int length;

    private boolean hasNext = true;

    /**
     * Constructs a new StringIterator:
     * length = position length
     *
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param position      the starting position
     */
    public StringIterator(final char[] possibleChars, final double[] position) {
        this.possibleChars = possibleChars;
        this.position = position;
        this.max = possibleChars.length - 1;
        this.length = position.length;
    }

    /**
     * Constructs a StringIterator from the given String
     *
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param string        the string
     * @throws IllegalArgumentException if the string contains characters not in the list
     */
    public StringIterator(final char[] possibleChars, final String string) throws IllegalArgumentException {
        this.possibleChars = possibleChars;
        this.max = possibleChars.length - 1;

        this.position = new double[string.length()];
        for (int i = 0; i < position.length; i++) {
            position[i] = possibleChars[indexOf(string.charAt(i), possibleChars)];
        }
        this.length = position.length;
    }

    /**
     * Constructs a new StringIterator with position Zero
     *
     * @param possibleChars all possible characters -> {@link #possibleChars}
     * @param length        the length of the string
     */
    public StringIterator(final char[] possibleChars, final int length) {
        this(possibleChars, new double[length]);
    }


    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public String next() {
        //Initialize before incrementing
        final String asString = asString();
        position[length - 1]++;

        if (position[length - 1] > max) {
            //Reset Digit
            position[position.length - 1] = 0;
            //Increment the other digits
            for (int i = length - 2; i >= 0; i--) {
                position[i]++;
                //If the element is not max, no need to increment other digits
                if (position[i] <= max) {
                    break;
                } else if (i == 0) {
                    //No characters left
                    hasNext = false;
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
        for (final double i : position) {
            builder.append(possibleChars[(int) i]);
        }
        return builder.toString();
    }

    /**
     * @param character     the character to look for
     * @param possibleChars the array with all characters
     * @return the first index of the character in the array
     * @throws IllegalArgumentException if the array does not contain the character
     */
    private int indexOf(final char character, final char[] possibleChars) throws IllegalArgumentException {
        for (int i = 0; i < possibleChars.length; i++) {
            if (possibleChars[i] == character) {
                return i;
            }
        }
        throw new IllegalArgumentException("Array does not contain Character!");
    }

    public double[] getPosition() {
        return position;
    }
}
