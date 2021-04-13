package me.spasskopf.dimensionfinder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Main class
 */
public class DimensionFinder {

    /**
     * Help text
     */
    public static final String HELP_TEXT = """
            Available Options:
                 -threads={num}
                    required: yes
                    Number of Threads you want to use
                    example: -threads=16

                 -length={num}
                    required: yes
                    Length of the output text
                    example: -length=6

                 -IDs={num1};{num2};....
                    required: no (defaults to 1,2,3)
                    What IDs you want to search for. Separated with ','
                    example: -IDs=1;2;3;4;5;6

                -chars=characters
                    required: no (defaults to lowercase alphabet + space + 0-9)</td>
                    The Characters the text can contain.
                     NOTE: Can only be the LAST PARAMETER!!! Otherwise it would be difficult to separate characters/options
                    example: -chars=abcdefgh132465798 ABCDE

            """;
    /**
     * Characters the text can contain. Change this if you want to
     */
    public static char[] CHARS = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            // 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '
    };
    /**
     * What hashed IDs to search for. Change this if you want to
     */
    public static int[] DIMENSION_IDS = new int[]{0, 1, 2};

    /**
     * The Path of the file, where the results get saved in.
     */
    public static final Path RESULT_PATH = Paths.get("result_" + new SimpleDateFormat("yyyy.MM.dd__hh.mm").format(new Date()) + ".txt");

    /**
     * Main Method
     * <p>
     * Providing zero arguments or specifying not all required arguments wil ask the user to inout values!
     * Arguments:
     * {num}: any whole (0,1,2,3, ...) number
     *
     * <table>
     *
     *     <th>Argument</th>
     *     <th>Required</th>
     *     <th>Description</th>
     *     <th>Example</th>
     *     <tr>
     *         <td>-threads={num}</td>
     *         <td>yes</td>
     *         <td>Number of Threads you want to use</td>
     *         <td>-threads=16</td>
     *     </tr>
     *     <tr>
     *         <td>-length={num}</td>
     *         <td>yes</td>
     *         <td>Length of the output text</td>
     *         <td>-length=6</td>
     *     </tr>
     *     <tr>
     *         <td>-IDs={num1};{num2};....</td>
     *         <td>no (defaults to 1,2,3)</td>
     *         <td>What IDs you want to search for. Separated with ','</td>
     *         <td>-IDs=1;2;3;4;5;6</td>
     *     </tr>
     *
     *     <td>-chars=characters</td>
     *     <td>no (defaults to lowercase alphabet + space + 0-9)</td>
     *     <td>The Characters the text can contain. NOTE: Can only be the LAST PARAMETER!!! Otherwise it would be difficult to separate characters/options</td>
     *     <td>-chars=abcdefgh132465798 ABCDE</td>
     *
     * </table>
     *
     * @param args the program arguments
     */
    public static void main(final String[] args) {
        int threads = -1;
        int textLength = -1;


        if (args.length != 0) {
            for (String arg : args) {
                arg = arg.toLowerCase(Locale.ROOT);
                if (arg.startsWith("-threads=")) {
                    final String substring = arg.substring("-threads=".length());
                    try {
                        final int anInt = Integer.parseInt(substring);
                        if (anInt <= 0) {
                            System.err.println("Thread count must not be less than one!");
                            System.exit(-1);
                        }
                        threads = anInt;
                    } catch (NumberFormatException e) {
                        System.err.println(substring + " is not a valid number! Try again please");
                        System.exit(-1);
                    }

                } else if (arg.startsWith("-length=")) {
                    final String substring = arg.substring("-length=".length());
                    try {
                        final int anInt = Integer.parseInt(substring);
                        if (anInt <= 0) {
                            System.err.println("Length must not be less than one!");
                            System.exit(-1);
                        }
                        textLength = anInt;
                    } catch (NumberFormatException e) {
                        System.err.println(substring + " is not a valid number! Try again please");
                        System.exit(-1);
                    }

                } else if (arg.startsWith("-ids=")) {
                    final String substring = arg.substring("-ids=".length());
                    String current = "";
                    try {
                        final String[] split = substring.split(";");
                        DIMENSION_IDS = new int[split.length];
                        for (int i = 0; i < split.length; i++) {
                            current = split[i];
                            DIMENSION_IDS[i] = Integer.parseInt(split[i]);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println(current + " is not a valid number! Try again please");
                        System.exit(-1);
                    }
                } else if (arg.startsWith("-chars=")) {
                    final String joined = String.join(" ", args);
                    String substring = joined.substring(joined.indexOf("-chars=") + "-chars=".length());
                    System.out.println("[INFO] Character option is used! Treating " + substring + " as Character Array!");
                    CHARS = substring.toCharArray();
                } else if (arg.startsWith("-help")) {
                    System.out.println(HELP_TEXT);
                } else {
                    System.err.println("Unrecognized Option " + arg + " use -help for help!");
                }
            }
        }


        if (threads == -1 || textLength == -1) {

            try {
                final Scanner scanner = new Scanner(System.in);

                if (threads == -1) {
                    System.out.printf("Enter the number of threads you want to use. You have %d threads available...%n", Runtime.getRuntime().availableProcessors());
                    threads = Integer.parseInt(scanner.nextLine());
                }
                if (textLength == -1) {
                    System.out.println("Enter the text-length. Higher value = more combinations = takes much longer!");
                    textLength = Integer.parseInt(scanner.nextLine());
                }

            } catch (final NumberFormatException e) {
                //User error
                System.err.println("This is not a valid number! Try again please");
                e.printStackTrace();
                System.exit(-1);
                return;
            }
        }

        final Map<Integer, List<String>> result = startSearching(threads, textLength, DIMENSION_IDS);


        System.out.println("Printing results:");
        System.out.println("==================================================");
        result.forEach((integer, strings) -> System.out.printf("Strings for Hash %d: %s%n", integer, Arrays.toString(strings.toArray(new String[0]))));
        System.out.println("==================================================");



        try (
                final BufferedWriter writer = Files.newBufferedWriter(RESULT_PATH)) {
            for (final Map.Entry<Integer, List<String>> entry : result.entrySet()) {
                writer.write(entry.getKey() + ": ");
                writer.write(Arrays.toString(entry.getValue().toArray()) + System.lineSeparator());
            }
            System.out.printf("Saved results as %s%n", RESULT_PATH.toAbsolutePath().toString());
        } catch (
                final IOException e) {
            System.err.println("Could not save results as File!");
            e.printStackTrace();
        }

        System.out.println("Program finished. Press Enter to exit...");
        new

                Scanner(System.in).

                next();
        System.out.println("Bye!");
        System.exit(0);
    }

    /**
     * Starts searching for Strings that match one of the given targetDimensionIds when hashed
     * <p>
     * Formula for calculating the number of unique combinations:<br>
     * <pre>
     * numberOfCharacters pow length
     * </pre>
     * In this method:
     * <pre>
     * Math.pow(chars.length, textLength)
     * </pre>
     *
     * @param threadCount        the number of threads. see {@link Runtime#availableProcessors()} for more information
     * @param textLength         the length of the text. High length = Many combinations = Takes very very long
     * @param targetDimensionIds the dimensionIDs we are looking for
     * @return a Map containing the inputted dimensions IDs as Keys and a List with all Strings as Object
     * @throws IllegalArgumentException the length of targetDimensionIDs is zero, or any other parameter is zero
     */
    //Suppress Constant Conditions because IntelliJ thinks @Range throws an exception but it does not...
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private static Map<Integer, List<String>> startSearching(@Range(from = 1, to = Integer.MAX_VALUE) final int threadCount,
                                                             @Range(from = 1, to = Integer.MAX_VALUE) final int textLength,
                                                             @Range(from = 1, to = Integer.MAX_VALUE) final int... targetDimensionIds) {
        if (threadCount <= 0 || textLength <= 0 || targetDimensionIds.length <= 0 || CHARS.length <= 0) {
            throw new IllegalArgumentException("Wrong parameters!");
        }

        System.out.printf(
                "Starting search!" + "\n" +
                        "=======================" + "\n" +
                        "   Parameters:" + "\n" +
                        "   ThreadCount   : %d" + "\n" +
                        "   Text length   : %d" + "\n" +
                        "   Dimension IDs : %s" + "\n" +
                        "   Characters    : %s" + "\n" +
                        "=======================" + "\n",
                threadCount,
                textLength,
                Arrays.toString(targetDimensionIds),
                Arrays.toString(CHARS));


        final long startTime = System.currentTimeMillis();
        //Thread safe
        final Map<Integer, List<String>> occurrences = new ConcurrentHashMap<>();
        //Initialize Map, so there is no need for null-checking afterwards
        for (final int i : targetDimensionIds) {
            occurrences.put(i, Collections.synchronizedList(new ArrayList<>()));
        }
        //All worker-Threads
        final Thread[] threads = new Thread[threadCount];
        final FinderTask[] finder = new FinderTask[threadCount];

        //Calculate the number of combinations so the work can be split up
        final double possibleCombinations = Math.pow(DimensionFinder.CHARS.length, textLength);
        //How many values each thread should check.
        //Note: this value is rounded down to prevent values like 5.354 which would be hard to implement
        final double stringsPerThread = Math.floor(possibleCombinations / threadCount);

        for (int i = 0; i < threads.length; i++) {
            final StringIterator iterator = StringIterator.getIteratorFromOffset(stringsPerThread * i, textLength, DimensionFinder.CHARS);
            finder[i] = new FinderTask(iterator, stringsPerThread, targetDimensionIds);
            //Create Variable, so there is no need for i to be final
            final FinderTask task = finder[i];
            threads[i] = new Thread(task::startSearching);
            threads[i].setName(String.valueOf(i));
            threads[i].setPriority(Thread.NORM_PRIORITY + 3);
        }
        //Because the value has been rounded down, there might be values which will not be checked in the findertask.
        if (threads.length * stringsPerThread < possibleCombinations) {
            System.out.println("There are combinations, which would be uncovered due to number-rounding. Testing them on the Main-Thread now!");
            final StringIterator iterator = StringIterator.getIteratorFromOffset(stringsPerThread * threads.length, textLength, DimensionFinder.CHARS);
            //Start searching
            final FinderTask task = new FinderTask(iterator, possibleCombinations - threads.length * stringsPerThread, targetDimensionIds);
            task.startSearching();
            //Add the values
            task.found.forEach(((integer, strings) -> occurrences.get(integer).addAll(strings)));
            System.out.println("Finished!");
        }
        System.out.println("Starting threads now...");
        for (final Thread thread : threads) {
            thread.start();
        }

        try {
            //Round to one decimal
            final DecimalFormat df = new DecimalFormat("###.##");
            df.setMinimumFractionDigits(2);
            df.setMaximumFractionDigits(2);

            //Progressbar
            StringBuilder progress;
            //Wait for the tasks to finish
            boolean finished;

            do {
                double searched = possibleCombinations - threads.length * stringsPerThread;

                progress = new StringBuilder("Progress: [");

                finished = true;

                //Add Thread-Status to the progressbar
                for (int i = 0; i < threads.length; i++) {
                    if (threads[i].isAlive()) {
                        finished = false;
                    }
                    progress.append("Thread ").append(threads[i].getName()).append(": ");

                    //Thread is alive?
                    if (threads[i].isAlive()) {
                        progress.append("alive ");
                    } else {
                        progress.append("stopped ");
                    }
                    //Percent searched
                    progress.append(df.format(finder[i].getSearched() * 100 / finder[i].getAmount()))
                            .append("%");

                    //Add Separator
                    if (i != threads.length - 1) {
                        progress.append(" | ");
                    } else {
                        progress.append("] ");
                    }

                    searched += finder[i].getSearched();

                }

                progress.append("-> ")
                        .append(df.format(searched * 100 / possibleCombinations))
                        .append("%");
                //Carriage return.
                progress.append("\r");
                System.out.print(progress.toString());

                TimeUnit.SECONDS.sleep(1);
            } while (!finished);

        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Finished searching!");
        //Add the values
        for (final FinderTask task : finder) {
            task.found.forEach(((integer, strings) -> occurrences.get(integer).addAll(strings)));
        }

        System.out.printf("Searching Took %s seconds!%n", (System.currentTimeMillis() - startTime) / 1000);

        return occurrences;
    }


}
