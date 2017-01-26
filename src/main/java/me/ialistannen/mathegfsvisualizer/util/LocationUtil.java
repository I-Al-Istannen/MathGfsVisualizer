package me.ialistannen.mathegfsvisualizer.util;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Some methods to help with Locations
 */
public class LocationUtil {

    /**
     * Gets a block relative to the one the player is looking at
     *
     * @param player The Player
     * @param modifierX The Modifier on the x axis
     * @param modifierY The Modifier on the y axis
     * @param modifierZ The Modifier on the z axis
     *
     * @return The modified Location
     */
    public static Location getRelativeToTargetBlock(Player player, double modifierX, double modifierY,
                                                    double modifierZ) {
        Block targetBlock = player.getTargetBlock((Set<Material>) null, 100);
        if (targetBlock == null || targetBlock.getType() == Material.AIR) {
            return null;
        }
        return targetBlock.getLocation().add(modifierX, modifierY, modifierZ);
    }
}
