package xyz.aunto.acorn.checks.movement;

import org.bukkit.event.EventPriority;
import org.bukkit.potion.PotionEffectType;
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
 * Detects when players try to go too fast.
 *
 * @author sysollie
 *
 */

public class Speed extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        if(!CustomConfig.get().getBoolean("speed.enabled")) return;

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

        // If the player is gliding, ignore them
        if(player.isGliding()) return;

        // If the player is flying, also ignore them
        if(player.isFlying()) return;

        // Make sure the player has actually moved - If they haven't, return
        if(toX == fromX && toY == fromY && toZ == fromZ) return;

        // If the player is in the air and is going down, ignore
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.AIR && fromY > toY) return;

        // See if the user has the SPEED or LEVITATION potion effect
        if(player.hasPotionEffect(PotionEffectType.SPEED) || player.hasPotionEffect(PotionEffectType.LEVITATION)) return;

        // If the user is swimming or flying, disable
        if(CustomConfig.get().getBoolean("disable-whilst-fly-swim")){
            if(player.isSwimming()) return;
            if(player.isFlying()) return;
        }

        // If the player is riptiding, return
        if(player.isRiptiding()) return;

        // If the player is inside a vehicle like a boat, ignore them
        if(player.isInsideVehicle()) return;

        // Calculate the speed
        double speedRes = event.getFrom().distance(event.getTo()) * 20;
        int speed = (int)speedRes;

        // Take action if needed
        if(CustomConfig.get().getBoolean("speed.enabled")){
            if(CustomConfig.get().getInt("speed.allowed-speed") <= speed){
                speedEvent(player, event);
                flag(player, "speed", "go faster than usual", 1);
            }
        }
    }
}