package me.spasskopf.dimensionfinder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * Characters the text can contain. Change this if you want to
     */
    public static final char[] CHARS = new char[]{
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            // 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', ' '
    };
    /**
     * What hashed IDs to search for. Change this if you want to
     */
    public static final int[] DIMENSION_IDS = new int[]{0, 1, 2};

    /**
     * The Path of the file, where the results get saved in.
     */
    public static final Path RESULT_PATH = Path.of("result_" + new SimpleDateFormat("yyyy.MM.dd__hh.mm").format(new Date()) + ".txt");

    /**
     * Main Method
     *
     * @param args the program arguments
     */
    public static void main(final String[] args) {
        final int threads;
        final int textLength;


        try {
            final Scanner scanner = new Scanner(System.in);
            System.out.printf("Enter the number of threads you want to use. You have %d threads available...%n", Runtime.getRuntime().availableProcessors());
            threads = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the text-length. Higher value = more combinations = takes much longer!");
            textLength = Integer.parseInt(scanner.nextLine());

        } catch (final NumberFormatException e) {
            //User error
            System.err.println("This is not a valid number! Try again please");
            e.printStackTrace();
            System.exit(-1);
            return;
        }

        final Map<Integer, List<String>> result = startSearching(threads, textLength, DIMENSION_IDS);


        System.out.println("Printing results:");
        System.out.println("=".repeat(50));
        result.forEach((integer, strings) -> System.out.printf("Strings for Hash %d: %s%n", integer, Arrays.toString(strings.toArray(new String[0]))));
        System.out.println("=".repeat(50));


        try (final BufferedWriter writer = Files.newBufferedWriter(RESULT_PATH)) {
            for (final Map.Entry<Integer, List<String>> entry : result.entrySet()) {
                writer.write(entry.getKey() + ": ");
                writer.write(Arrays.toString(entry.getValue().toArray()) + System.lineSeparator());
            }
            System.out.printf("Saved results as %s%n", RESULT_PATH.toAbsolutePath().toString());
        } catch (final IOException e) {
            System.err.println("Could not save results as File!");
            e.printStackTrace();
        }

        System.out.println("Program finished. Press Enter to exit...");
        new Scanner(System.in).next();
        System.out.println("Bye!");
        System.exit(0);
    }

    /**
     * Starts searching for Strings that match one of the given targetDimensionIds when hashed
     * <p>
     * Formula for calculating the number of unique combinations:<br>
     * <pre>
     *         numberOfCharacters pow length
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

        final long startTime = System.currentTimeMillis();
        //Thread safe
        final Map<Integer, List<String>> occurences = new ConcurrentHashMap<>();
        //Initialize Map, so there is no need for null-checking afterwards
        for (final int i : targetDimensionIds) {
            occurences.put(i, Collections.synchronizedList(new ArrayList<>()));
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
            task.found.forEach(((integer, strings) -> occurences.get(integer).addAll(strings)));
            System.out.println("Finished!");
        }
        System.out.println("Starting threads now...");
        for (final Thread thread : threads) {
            thread.start();
        }

        try {
            //Round to one decimal
            final DecimalFormat df = new DecimalFormat("###.##");
            //Progressbar
            StringBuilder progress;
            //Wait for the tasks to finish
            boolean finished;


            do {
                double searched = possibleCombinations - threads.length * stringsPerThread;

                progress = new StringBuilder("Progress: [");

                finished = true;
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

                    if (i != threads.length - 1) {
                        progress.append(" | ");
                    } else {
                        progress.append("] ");
                    }

                    searched += finder[i].getSearched();

                }

                progress.append("-> ").append(df.format(searched * 100 / possibleCombinations));
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
            task.found.forEach(((integer, strings) -> occurences.get(integer).addAll(strings)));
        }

        System.out.printf("Searching Took %s seconds!%n", (System.currentTimeMillis() - startTime) / 1000);

        return occurences;
    }


}
