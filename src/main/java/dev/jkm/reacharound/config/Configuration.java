package dev.jkm.reacharound.config;

import org.bukkit.configuration.file.FileConfiguration;

/**
 * Represents the configuration of the border
 */
public class Configuration {
    /**
     * The bounds of the world, i.e. the points at which the player will be teleported
     */
    public final Bounds bounds;

    /**
     * The number of chunks to preload on the other side of the border, when the player is near the border
     * 
     * Set to 0 to disable preloading
     */
    public final int preloadChunkDistance;

    /**
     * The interval between checks for each player, in milliseconds
     */
    public final int checkInterval;

    private Configuration(Bounds bounds, int preloadChunkDistance, int checkInterval) {
        this.bounds = bounds;
        this.preloadChunkDistance = preloadChunkDistance;
        this.checkInterval = checkInterval;
    }

    /**
     * Loads the configuration from the given FileConfiguration
     * @param config The FileConfiguration to load from
     * @return The loaded configuration
     */
    public static Configuration load(FileConfiguration config) {
        Bounds bounds = new Bounds(
                config.getInt("bounds.minX"),
                config.getInt("bounds.minZ"),
                config.getInt("bounds.maxX"),
                config.getInt("bounds.maxZ")
        );

        int preloadChunkDistance = config.getInt("preload-chunk-distance");

        int checkInterval = config.getInt("check-interval");

        return new Configuration(bounds, preloadChunkDistance, checkInterval);
    }
}
