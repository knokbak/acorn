package xyz.aunto.acorn.checks.combat;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import xyz.aunto.acorn.files.ViolationHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.aunto.acorn.files.CustomConfig;

/**
 *
 * Detects when players attempt to hit entities from too far away.
 *
 * @author sysollie
 *
 */

public class Reach extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(!CustomConfig.get().getBoolean("reach.enabled")) return;

        // This makes stuff WAY less messy
        Entity defender = event.getEntity();
        Entity damager = event.getDamager();
        Player player = Bukkit.getPlayerExact(damager.getName());
        if(player == null) return;

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        double aX = player.getLocation().getX();
        double aY = player.getLocation().getY();
        double aZ = player.getLocation().getZ();
        double dX = defender.getLocation().getX();
        double dY = defender.getLocation().getY();
        double dZ = defender.getLocation().getZ();

        // Calculate the reach of the hit
        double reach = Math.sqrt(((aX-dX)*(aX-dX))+((aY-dY)*(aY-dY))+((aZ-dZ)*(aZ-dZ)));

        // Check to see if the player is hitting from too far away
        if(reach > (CustomConfig.get().getInt("reach.reach-limit")+1)){
            reachEvent(player, event);
            flag(player, "reach", "attack entities from too far away", 1);
        }
    }
}