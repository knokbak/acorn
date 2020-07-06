package xyz.aunto.acorn.checks.combat;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import xyz.aunto.acorn.files.ViolationHandler;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.lang.*;

/**
 *
 * Detects when players attempt to hit an entity that is outside of their FOV.
 *
 * @author sysollie
 * @work-in-progress
 *
 */

public class OutOfVisionHits extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        // This makes stuff WAY less messy
        Entity defender = event.getEntity();
        Entity damager = event.getDamager();
        Player player = Bukkit.getPlayerExact(damager.getName());
        if(player == null) return;

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        double dX = defender.getLocation().getX();
        //double dY = defender.getLocation().getY();
        double dZ = defender.getLocation().getZ();
        double aX = player.getLocation().getX();
        //double aY = player.getLocation().getY();
        double aZ = player.getLocation().getZ();

        // Calculate the distance between the entities
        //double dist = Math.sqrt(((aX-dX)*(aX-dX))+((aY-dY)*(aY-dY))+((aZ-dZ)*(aZ-dZ)));

        // If it isn't an attack from a hit, ignore
        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) return;

        // Calculate the attack yaw
        double result = (Math.acos((dZ-aZ)/Math.sqrt(((dX-aX)*(dX-aX))+((dZ-aZ)*(dZ-aZ))))*180)/3.14159;

        // Get the abs of the provided and calculated yaw
        double abs = Math.abs(result-player.getLocation().getYaw());

        Bukkit.broadcastMessage("Yaw: " + result + "  Player: " + player.getLocation().getYaw());
        if(abs >= 10) Bukkit.broadcastMessage("Invalid hit!");

        Vector real = event.getEntity().getLocation().toVector().subtract(player.getEyeLocation().toVector());
        Vector pvec = player.getEyeLocation().getDirection();
        double diff = real.dot(pvec);
        Bukkit.broadcastMessage("Diff: " + diff);
    }
}