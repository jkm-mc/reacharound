package dev.jkm.reacharound;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import dev.jkm.reacharound.config.Configuration;

public class ReachAroundPlugin extends JavaPlugin implements Listener {
    /**
     * The last time each player was checked for being near the border
     */
    private HashMap<UUID, Long> lastCheckTime = new HashMap<>();

    private Configuration configuration;

    @Override
    public void onEnable() {
        FileConfiguration config = getConfig();

        configuration = Configuration.load(config);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        long currentTime = System.currentTimeMillis();

        // Check if enough time has passed since the last check for this player
        if (lastCheckTime.containsKey(playerUUID) &&
                currentTime - lastCheckTime.get(playerUUID) < configuration.checkInterval) {
            return;
        }

        lastCheckTime.put(playerUUID, currentTime);

        Location to = event.getTo();

        World world = player.getWorld();
        int maxX = getMaxX(world);
        int maxZ = getMaxZ(world);

        if (configuration.preloadChunkDistance > 0) {
            preloadChunks(world, to, maxX, maxZ);
        }

        teleportForBorderIfNecessary(player, to, maxX, maxZ);
    }

    /**
     * Teleports the player to the other side of the border if they have crossed the border
     * @param player The player to teleport
     * @param to The location the player is moving to
     * @param maxX The maximum X coordinate for the world
     * @param maxZ The maximum Z coordinate for the world
     */
    private void teleportForBorderIfNecessary(Player player, Location to, int maxX, int maxZ) {
        boolean hasChanged = false;

        if (to.getBlockX() > maxX) {
            to.setX(0);
            hasChanged = true;
        } else if (to.getBlockX() < 0) {
            to.setX(maxX);
            hasChanged = true;
        }
        
        if (to.getBlockZ() > maxZ) {
            to.setZ(0);
            hasChanged = true;
        } else if (to.getBlockZ() < 0) {
            to.setZ(maxZ);
            hasChanged = true;
        }        
    
        if (hasChanged) {
            player.teleport(to);
        }
    }

    /**
     * Gets the maximum X coordinate for the given world
     * @param world The world to get the maximum X coordinate for
     * @return The maximum X coordinate for the given world
     */
    private int getMaxX(World world) {
        String worldName = world.getName();

        if (worldName.equals("world")) {
            return configuration.bounds.maxX;
        } else if (worldName.equals("world_nether")) {
            return configuration.bounds.maxX / 8;
        }
        
        return -1;
    }

    /**
     * Gets the maximum Z coordinate for the given world
     * @param world The world to get the maximum Z coordinate for
     * @return The maximum Z coordinate for the given world
     */
    private int getMaxZ(World world) {
        String worldName = world.getName();

        if (worldName.equals("world")) {
            return configuration.bounds.maxZ;
        } else if (worldName.equals("world_nether")) {
            return configuration.bounds.maxZ / 8;
        }
        
        return -1;
    }

    /**
     * Preload chunks on the other side of the border, when the player is near the border
     * 
     * @param world The world to preload chunks in
     * @param loc The location of the player
     * @param maxX The maximum X coordinate for the world
     * @param maxZ The maximum Z coordinate for the world
     */
    private void preloadChunks(World world, Location loc, int maxX, int maxZ) {
        int chunkX = loc.getBlockX() >> 4;
        int chunkZ = loc.getBlockZ() >> 4;
    
        if (chunkX >= (maxX >> 4) - configuration.preloadChunkDistance) {
            for (int z = -configuration.preloadChunkDistance; z <= configuration.preloadChunkDistance; z++) {
                world.getChunkAt(0, chunkZ + z).load();
            }
        }
    
        if (chunkX <= configuration.preloadChunkDistance) {
            for (int z = -configuration.preloadChunkDistance; z <= configuration.preloadChunkDistance; z++) {
                world.getChunkAt((maxX >> 4), chunkZ + z).load();
            }
        }
    
        if (chunkZ >= (maxZ >> 4) - configuration.preloadChunkDistance) {
            for (int x = -configuration.preloadChunkDistance; x <= configuration.preloadChunkDistance; x++) {
                world.getChunkAt(chunkX + x, 0).load();
            }
        }
    
        if (chunkZ <= configuration.preloadChunkDistance) {
            for (int x = -configuration.preloadChunkDistance; x <= configuration.preloadChunkDistance; x++) {
                world.getChunkAt(chunkX + x, (maxZ >> 4)).load();
            }
        }
    }
}
