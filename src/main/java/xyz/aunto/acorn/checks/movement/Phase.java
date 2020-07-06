package xyz.aunto.acorn.checks.movement;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import xyz.aunto.acorn.files.ViolationHandler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.aunto.acorn.files.CustomConfig;

import java.util.Objects;

/**
 *
 * Detects when players try to phase through a block.
 *
 * @author sysollie
 * @unstable
 *
 */

public class Phase extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        if(!CustomConfig.get().getBoolean("phase.enabled")) return;

        // This makes stuff WAY less messy
        Player player = event.getPlayer();
        int fromX = event.getFrom().getBlockX();
        int fromY = event.getFrom().getBlockY();
        int fromZ = event.getFrom().getBlockZ();
        int toX = Objects.requireNonNull(event.getTo()).getBlockX();
        int toY = Objects.requireNonNull(event.getTo()).getBlockY();
        int toZ = Objects.requireNonNull(event.getTo()).getBlockZ();

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        // Make sure the player has actually moved - If they haven't, return
        if(toX == fromX && toY == fromY && toZ == fromZ) return;

        // If the player is currently set to flying, we don't want to check them
        if(player.isFlying()) return;

        // Detect if the player is phasing through a block
        Block block = player.getWorld().getBlockAt(toX, toY, toZ);
        if(block.isPassable()) return;

        // Make sure they aren't in sand or gravel
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.SAND) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.GRAVEL) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.AIR) return;

        // Ignore users when they are inside a door
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.ACACIA_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.BIRCH_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.DARK_OAK_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.IRON_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.JUNGLE_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.OAK_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.SPRUCE_DOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.ACACIA_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.BIRCH_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.DARK_OAK_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.IRON_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.JUNGLE_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.OAK_TRAPDOOR) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.SPRUCE_TRAPDOOR) return;

        // Final check
        if(player.getWorld().getBlockAt(toX, toY-1, toZ).getType() != Material.WATER && player.getWorld().getBlockAt(toX, toY-1, toZ).getType() != Material.LAVA){
            phaseEvent(player, event);
            flag(player, "phase", "phase through a block", 1);
        }
    }
}