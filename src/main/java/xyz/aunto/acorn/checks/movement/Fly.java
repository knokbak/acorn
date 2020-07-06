package xyz.aunto.acorn.checks.movement;

import org.bukkit.block.Block;
import org.bukkit.event.EventPriority;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import xyz.aunto.acorn.files.ViolationHandler;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;
import xyz.aunto.acorn.files.CustomConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 *
 * Detects when players attempt to fly illegally.
 *
 * @author sysollie
 *
 */

public class Fly extends ViolationHandler implements Listener {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Acorn");
    HashMap<String, Double> yAxisLog = new HashMap<>();
    HashMap<String, Integer> yAxisVL = new HashMap<>();
    HashMap<String, Long> onGround = new HashMap<>();

    // Used to ignore players when they are attacked
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(!CustomConfig.get().getBoolean("fly.enabled")) return;

        if(event.getEntity() instanceof Player){
            Player player = ((Player) event.getEntity()).getPlayer();

            long timeNow = System.currentTimeMillis() / 1000L;
            assert player != null;
            player.setMetadata("ACORN-attacked", new FixedMetadataValue(plugin, timeNow+2500));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onMove(PlayerMoveEvent event){
        if(!CustomConfig.get().getBoolean("fly.enabled")) return;

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

        // This is just important
        long timeNow = System.currentTimeMillis() / 1000L;

        // If the player is gliding, ignore them
        if(player.isGliding()) return;

        // If the player is currently on ground, let's store that
        if(player.isOnGround()) onGround.put(player.getName(), timeNow);

        // Make sure the player has actually moved - If they haven't, return
        if(toX == fromX && toY == fromY && toZ == fromZ && Objects.equals(CustomConfig.get().getString("fly.mode"), "light")) return;

        // If the player is on the ground, they're obviously not flying
        if(player.isOnGround()) return;

        // If the player is currently set to flying, we don't want to check them
        if(player.isFlying()) return;

        // If the player is currently set to swimming, don't check them
        if(player.isSwimming()) return;

        // If the player is inside or on these blocks, we must assume they are not flying
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.COBWEB) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.LADDER) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.VINE) return;
        if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.SCAFFOLDING) return;

        // Ignore anyone who is currently in a vehicle
        if(player.isInsideVehicle()) return;

        // Ignore anyone who is riptiding
        if(player.isRiptiding()) return;

        // Disable this check if the user is near water
        if(CustomConfig.get().getBoolean("fly.ignore-near-water")){
            if(player.getWorld().getBlockAt(toX, toY, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY, toZ-1).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX, toY, toZ-1).getType() == Material.WATER) return;

            if(player.getWorld().getBlockAt(toX, toY-1, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-1, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-1, toZ-1).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX, toY-1, toZ-1).getType() == Material.WATER) return;

            if(player.getWorld().getBlockAt(toX, toY-2, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-2, toZ).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX-1, toY-2, toZ-1).getType() == Material.WATER) return;
            if(player.getWorld().getBlockAt(toX, toY-2, toZ-1).getType() == Material.WATER) return;
        }

        // Ignore the player if they might be on/near a boat
        if(player.getNearbyEntities(3, 3, 3).stream().anyMatch(e -> e.getType() == EntityType.BOAT)) return;

        // If the player was on ground 1 second ago, they've probably jumped
        if(onGround.containsKey(player.getName())){
            long whenOnGround = onGround.get(player.getName());
            if(whenOnGround+CustomConfig.get().getInt("fly.Y-level-monitoring.ground-log-life") >= timeNow) return;
        }

        // See if the user is trying to fly or just jumping
        Block below = player.getWorld().getBlockAt(toX, toY, toZ);
        Block below1 = player.getWorld().getBlockAt(toX, toY-1, toZ);
        Block below2 = player.getWorld().getBlockAt(toX, toY-2, toZ);
        Block below3 = player.getWorld().getBlockAt(toX, toY-3, toZ);

        if(below2.getType() == Material.AIR && below3.getType() == Material.WATER){
            // If the player has just gone off a ledge, don't check them right now
            Block toBlock = player.getWorld().getBlockAt(toX, toY, toZ);
            Block fromBlock = player.getWorld().getBlockAt(fromX, fromY, fromZ);
            if(toBlock.getType() == Material.AIR && !fromBlock.isPassable()){
                // IMPORTANT - Prevents falling players from being punished
                player.setMetadata("ACORN-falling", new FixedMetadataValue(plugin, timeNow+800));
                return;
            }

            boolean isFalling = false;
            List<MetadataValue> fallingValues = player.getMetadata("ACORN-falling");

            for(MetadataValue v : fallingValues){
                if(v.getOwningPlugin() == plugin){
                    long value = v.asLong();

                    if(value >= timeNow){
                        isFalling = true;
                    } else {
                        player.removeMetadata("ACORN-falling", plugin);
                    }
                }
            }

            if(isFalling) return;

            // Check the player's velocity
            if(velocity.getY() > 0) return;

            // If the player isn't going up, they are probably not flying or are just a stupid hacker
            if(fromY > toY) return;

            // If the user was recently attacked, assume they aren't flying
            boolean shouldFlag = true;
            List<MetadataValue> values = player.getMetadata("ACORN-attacked");

            for(MetadataValue v : values){
                if(v.getOwningPlugin() == plugin){
                    long value = v.asLong();

                    if(value >= timeNow){
                        shouldFlag = false;
                    } else {
                        player.removeMetadata("ACORN-attacked", plugin);
                    }
                }
            }

            // Flag the player
            if(shouldFlag){
                if(CustomConfig.get().getBoolean("fly.enabled")) {
                    event.setCancelled(true);
                    flag(player, "fly", "fly above water", 1);
                }
            }
        } else
        if(below1.getType() == Material.AIR && below2.getType() == Material.AIR){
            // If the player has just gone off a ledge, don't check them right now
            Block toBlock = player.getWorld().getBlockAt(toX, toY, toZ);
            Block fromBlock = player.getWorld().getBlockAt(fromX, fromY, fromZ);
            if(toBlock.getType() == Material.AIR && !fromBlock.isPassable()){
                // IMPORTANT - Prevents falling players from being punished
                player.setMetadata("ACORN-falling", new FixedMetadataValue(plugin, timeNow+800));
                return;
            }

            boolean isFalling = false;
            List<MetadataValue> fallingValues = player.getMetadata("ACORN-falling");

            for(MetadataValue v : fallingValues){
                if(v.getOwningPlugin() == plugin){
                    long value = v.asLong();

                    if(value >= timeNow){
                        isFalling = true;
                    } else {
                        player.removeMetadata("ACORN-falling", plugin);
                    }
                }
            }

            if(isFalling) return;

            // Check the player's velocity
            if(velocity.getY() > 0) return;

            // If the player isn't going up, they are probably not flying or are just a stupid hacker
            if(fromY > toY) return;

            // If the user was recently attacked, assume they aren't flying
            boolean shouldFlag = true;
            List<MetadataValue> values = player.getMetadata("ACORN-attacked");

            for(MetadataValue v : values){
                if(v.getOwningPlugin() == plugin){
                    long value = v.asLong();

                    if(value >= timeNow){
                        shouldFlag = false;
                    } else {
                        player.removeMetadata("ACORN-attacked", plugin);
                    }
                }
            }

            // Flag the player
            if(shouldFlag){
                if(CustomConfig.get().getBoolean("fly.enabled")) {
                    flyEvent(player, event);
                    flag(player, "fly", "fly illegally", 1);
                }
            }
        } else
        if(below.getType() == Material.AIR && !player.isOnGround() && event.getFrom().getY() == event.getTo().getY() && CustomConfig.get().getBoolean("fly.Y-level-monitoring.enabled")){
            if(yAxisLog.containsKey(player.getName())){
                if(yAxisLog.get(player.getName()) != event.getTo().getY()){
                    yAxisLog.remove(player.getName());
                    yAxisVL.remove(player.getName());
                    return;
                }

                int VL = 0;
                if(yAxisVL.containsKey(player.getName())) VL = yAxisVL.get(player.getName());
                yAxisVL.put(player.getName(), VL+1);

                if(VL >= CustomConfig.get().getInt("fly.Y-level-monitoring.vl-required")){
                    flyEvent(player, event);
                    flag(player, "fly", "maintain a suspicious Y level", 1);
                }
            } else {
                yAxisLog.put(player.getName(), event.getTo().getY());
            }
        }
    }
}