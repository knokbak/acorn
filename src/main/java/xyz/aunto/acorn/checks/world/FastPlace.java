package xyz.aunto.acorn.checks.world;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import xyz.aunto.acorn.files.CustomConfig;
import xyz.aunto.acorn.files.ViolationHandler;

import java.util.HashMap;

public class FastPlace extends ViolationHandler implements Listener {
    HashMap<String, Long> last = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent event){
        if(!CustomConfig.get().getBoolean("fastplace.enabled")) return;

        // This makes stuff less messy
        long time = System.currentTimeMillis();
        Player player = event.getPlayer();
        Block block = event.getBlock();

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        // If the block isn't passable, ignore
        if(block.isPassable()) return;

        // If there isn't already an entry, create one and return
        if(!last.containsKey(player.getName())){
            last.put(player.getName(), time);
            return;
        }

        // If the placement was too early, do stuff
        if(last.get(player.getName())+CustomConfig.get().getInt("fastplace.cooldown") > time){
            long since = time-last.get(player.getName());

            fastPlaceEvent(player, event);
            flag(player, "fastplace", "place blocks too fast (last placement " + since + "ms ago)", 1);
        }

        // Update the entry
        last.put(player.getName(), time);
    }
}