package xyz.aunto.acorn.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.aunto.acorn.files.ViolationHandler;
import xyz.aunto.acorn.files.CustomConfig;

import java.util.Objects;

/**
 *
 * Detects when players try to aviod kockback.
 *
 * @author sysollie
 *
 */

public class AntiKockback extends ViolationHandler implements Listener {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Acorn");

    // The knockback check MUST have the final say in the pipeline due to the way it works
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!CustomConfig.get().getBoolean("knockback.enabled")) return;

        Entity entity = event.getEntity();
        Player player = Bukkit.getPlayer(entity.getUniqueId());
        if(player == null) return;

        int bX = player.getLocation().getBlockX();
        int bY = player.getLocation().getBlockY();
        int bZ = player.getLocation().getBlockZ();
        Location loc = player.getLocation();

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        // If the player is inside a vehicle, probably want to ignore them
        if(player.isInsideVehicle()) return;

        // If the player is now dead, they can't take knockback can they?
        if(player.isDead()) return;

        // If the player is blocking attacks, also ignore them
        if(player.isBlocking()) return;

        // If they are flying also return
        if(player.isFlying()) return;

        // If they are riptiding, return
        if(player.isRiptiding()) return;

        // If they are in a cobweb, return
        if(player.getWorld().getBlockAt(bX, bY, bZ).getType() == Material.COBWEB) return;

        // If there is a block beside or above the player, ignore
        if(!player.getWorld().getBlockAt(bX, bY, bZ).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX-1, bY, bZ).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX-1, bY, bZ-1).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX, bY, bZ-1).isPassable()) return;

        if(!player.getWorld().getBlockAt(bX, bY+1, bZ).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX-1, bY+1, bZ).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX-1, bY+1, bZ-1).isPassable()) return;
        if(!player.getWorld().getBlockAt(bX, bY+1, bZ-1).isPassable()) return;

        // Create the task
        new BukkitRunnable(){
            @Override
            public void run(){
                // Get the distance between the last location and new location
                double dif = player.getLocation().distanceSquared(loc);

                // If the player is dead, ignore
                if(player.isDead()) return;

                // If the player is in a new world, also ignore them
                if(!player.getWorld().getName().equals(Objects.requireNonNull(loc.getWorld()).getName())) return;

                // If the player's difference is larger than 0, then they have taken knockback
                if(dif > CustomConfig.get().getInt("knockback.flag")) return;

                // They're not taking knockback!
                knockbackEvent(player);
                flag(player, "knockback", "alter proper velocity", 1);
            }
        }.runTaskLater(plugin, CustomConfig.get().getInt("knockback.time"));
    }
}
