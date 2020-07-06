package xyz.aunto.acorn.checks.player;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.aunto.acorn.files.ViolationHandler;
import xyz.aunto.acorn.files.CustomConfig;

public class InteractReach extends ViolationHandler implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event){
        if(!CustomConfig.get().getBoolean("boatfly.enabled")) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        // Do some things
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();

        // If the block is passable, ignore it
        if(block.isPassable()) return;

        // Get the block and player's location
        double pX = player.getLocation().getX();
        double pY = player.getLocation().getY();
        double pZ = player.getLocation().getZ();
        double bX = block.getLocation().getX();
        double bY = block.getLocation().getY();
        double bZ = block.getLocation().getZ();

        // Calculate the reach
        double reach = Math.sqrt(((pX-bX)*(pX-bX))+((pY-bY)*(pY-bY))+((pZ-bZ)*(pZ-bZ)));

        // Check the reach
        if(reach > CustomConfig.get().getInt("interactreach.limit")){
            interactReachEvent(player, event);
            flag(player, "interactreach", "interact with blocks from too far away", 1);
        }
    }
}
