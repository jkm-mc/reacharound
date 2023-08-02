package dev.jkm.reacharound.config;

/**
 * Represents the bounds of the world
 */
public class Bounds {
    /**
     * Minimum X coordinate
     */
    public final int minX;

    /**
     * Minimum Z coordinate
     */
    public final int minZ;

    /**
     * Maximum X coordinate
     */
    public final int maxX;

    /**
     * Maximum Z coordinate
     */
    public final int maxZ;

    public Bounds(int minX, int minZ, int maxX, int maxZ) {
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }
}
