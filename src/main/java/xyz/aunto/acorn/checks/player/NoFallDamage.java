package xyz.aunto.acorn.checks.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.aunto.acorn.files.ViolationHandler;

import java.util.HashMap;
import java.util.Objects;

public class NoFallDamage extends ViolationHandler implements Listener {
    Plugin plugin = Bukkit.getPluginManager().getPlugin("Acorn");
    HashMap<String, Double> top = new HashMap<>();
    HashMap<String, Long> lastDamage = new HashMap<>();

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageByBlockEvent(EntityDamageByBlockEvent event){
        // Get the player
        if(!(event.getEntity() instanceof Player)) return;
        Player player = ((Player) event.getEntity()).getPlayer();

        // Get the current time
        long timeNow = System.currentTimeMillis() / 1000L;

        // Log as fall damage
        assert player != null;
        lastDamage.put(player.getName(), timeNow);
    }

    @EventHandler(ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event){
        // This makes stuff WAY less messy
        Player player = event.getPlayer();
        double fromY = event.getFrom().getY();
        double toY = Objects.requireNonNull(event.getTo()).getY();

        // If there isn't already an entry for this player, create one
        if(!top.containsKey(player.getName())){
            top.put(player.getName(), toY);
            return;
        }

        // If the player is on ground, we'll check to see if they took fall damage
        if(player.isOnGround()){
            // Store the log and then delete it
            double log = top.get(player.getName());
            top.remove(player.getName());

            // If the fall distance is smaller than 4 block, ignore
            if(log-toY < 4) return;

            // Create the task
            new BukkitRunnable(){
                @Override
                public void run(){
                    // Get the time
                    long timeNow = System.currentTimeMillis() / 1000L;

                    // If the key doesn't exist, flag them
                    if(lastDamage.containsKey(player.getName())){
                        Bukkit.broadcastMessage("Avoding fall damage! [DE]");
                        return;
                    }

                    // If it actually exists, check it
                    if(lastDamage.get(player.getName())+2 < timeNow){
                        // They're avoiding fall damage
                        Bukkit.broadcastMessage("Avoding fall damage!");
                    }
                }
            }.runTaskLater(plugin, 20);
        } else {
            if(fromY < toY) top.put(player.getName(), toY);
        }
    }
}
