package dev.jkm.reacharound.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents the configuration of the border
 */
public class Configuration {
    /**
     * The bounds of the world, i.e. the points at which the player will be teleported
     */
    public static Bounds bounds;

    /**
     * The number of chunks to preload on the other side of the border, when the player is near the border
     * 
     * Set to 0 to disable preloading
     */
    public static int preloadChunkDistance;

    /**
     * The interval between checks for each player, in milliseconds
     */
    public static int checkInterval;

    /**
     * Loads the configuration from the given FileConfiguration
     * @param config The FileConfiguration to load from
     */
    public static void load(FileConfiguration config) {
        bounds = new Bounds(
                config.getInt("bounds.minX"),
                config.getInt("bounds.minZ"),
                config.getInt("bounds.maxX"),
                config.getInt("bounds.maxZ")
        );

        preloadChunkDistance = config.getInt("preload-chunk-distance");

        checkInterval = config.getInt("check-interval");
    }
}
