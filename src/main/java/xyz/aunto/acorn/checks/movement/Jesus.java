package xyz.aunto.acorn.checks.movement;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import xyz.aunto.acorn.files.ViolationHandler;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import xyz.aunto.acorn.files.CustomConfig;

import java.util.Objects;

/**
 *
 * Detects when players attempt to walk on water.
 *
 * @author sysollie
 *
 */

public class Jesus extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        if(!CustomConfig.get().getBoolean("jesus.enabled")) return;

        // This makes stuff WAY less messy
        Player player = event.getPlayer();
        Vector velocity = player.getVelocity();
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

        // Check the player's velocity
        if(velocity.getY() > 0) return;

        // If the person is near/on land, ignore them
        if(CustomConfig.get().getBoolean("fly.ignore-near-water")){
            if(player.getWorld().getBlockAt(toX, toY, toZ).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY, toZ).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY, toZ-1).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX, toY, toZ-1).getType() != Material.WATER) return;

            if(player.getWorld().getBlockAt(toX, toY-1, toZ).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-1, toZ).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-1, toZ-1).getType() != Material.WATER) return;
            if(player.getWorld().getBlockAt(toX, toY-1, toZ-1).getType() != Material.WATER) return;
        }

        // If the player is inside or on these blocks, we must assume they are not walking on water
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.COBWEB) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.LADDER) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.VINE) return;

        // Ignore anyone who is currently in a vehicle
        if(player.isInsideVehicle()) return;

        // Ignore the player if they might be on/near a boat
        if(player.getNearbyEntities(3, 2, 3).stream().anyMatch(e -> e.getType() == EntityType.BOAT)) return;

        // Don't flag people who are swimming
        if(player.isSwimming()) return;

        // Final check
        if(player.getWorld().getBlockAt(fromX, fromY-1, fromZ).getType() == Material.WATER && player.isOnGround()){
            jesusEvent(player, event);
            flag(player, "jesus", "walk on water", 1);
        }
    }
}