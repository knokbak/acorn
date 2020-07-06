package xyz.aunto.acorn.checks.movement;

import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
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
 * Detects when players try to fly whilst inside a boat.
 *
 * @author sysollie
 *
 */

public class BoatFly extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        if(!CustomConfig.get().getBoolean("boatfly.enabled")) return;

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

        if(!player.isInsideVehicle()) return;

        if(Objects.requireNonNull(player.getVehicle()).getType() == EntityType.BOAT){
            Block below = player.getWorld().getBlockAt(toX, toY-1, toZ);
            if(below.getType() != Material.AIR) return;
            if(fromY >= toY) return;

            if(CustomConfig.get().getBoolean("boatfly.enabled")){
                boatFlyEvent(player, event);
                flag(player, "boatfly", "fly in a boat", 1);
            }
        }
    }
}