package me.spasskopf.dimensionfinder.minecraft;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Class which generates the Dimension ID for the given input String
 */
@SuppressWarnings("UnstableApiUsage")
public class DimensionHashHelper {

    /**
     * Mojang's Salt String. Why did they even choose this?
     */
    public static final String SALT = ":why_so_salty#LazyCrypto";

    /**
     * An Instance of the Hash Function.
     */
    public static final HashFunction HASH_FUNCTION = Hashing.sha256();

    /**
     * Returns the dimension ID for the given String
     *
     * @param name the String (in minecraft the Book and Quill's Content)
     * @return the dimension ID
     */
    public static int getHash(final String name) {
        return HASH_FUNCTION.hashString(name + SALT, StandardCharsets.UTF_8).asInt() & Integer.MAX_VALUE;
    }
}
