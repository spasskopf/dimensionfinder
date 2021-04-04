import me.spasskopf.dimensionfinder.StringIterator;

import java.util.Arrays;

/**
 * Test Class
 * Deprecated because test class
 */
@Deprecated
public class Test {


    private static final int AMOUNT = 2000;

    /**
     * Test Method
     *
     * @param args program arguments
     */
    public static void main(final String[] args) {
        final char[] chars = {'0', '1', '2'};
        final int LENGTH = 5;
        System.out.println("Math.pow(chars.length, LENGTH) = " + Math.pow(chars.length, LENGTH));

        final StringIterator it = StringIterator.getIteratorFromOffset(20, LENGTH, chars);
        System.out.println("it.position = " + Arrays.toString(it.getPosition()));
    }

    private static char[] getChars() {
        return new char[]{
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                // 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '
        };
    }

}
