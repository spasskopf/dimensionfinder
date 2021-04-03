package me.spasskopf.dimensionfinder.minecraft;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * Class which generates the Dimension ID for the given input String
 */
public class DimensionHashHelper {

    /**
     * Mojang's Salt String. Why did they even choose this???
     */
    public static final String SALT = ":why_so_salty#LazyCrypto";

    /**
     * Returns the dimension ID for the given String
     *
     * @param name the String (in minecraft the Book and Quill's Content)
     * @return the dimension ID
     */
    public static int getHash(String name) {
        return Hashing.sha256().hashString(name + SALT, StandardCharsets.UTF_8).asInt() & Integer.MAX_VALUE;
    }
}
