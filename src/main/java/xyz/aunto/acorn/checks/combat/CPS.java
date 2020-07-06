package xyz.aunto.acorn.checks.combat;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import xyz.aunto.acorn.files.ViolationHandler;
import xyz.aunto.acorn.files.CustomConfig;

import java.util.HashMap;

/**
 *
 * Detects unusually high CPS.
 *
 * @author sysollie
 *
 */

public class CPS extends ViolationHandler implements Listener {
    HashMap<String, Integer> cps = new HashMap<>();
    HashMap<String, Long> cpsTime = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInteract(PlayerInteractEvent event){
        if(!CustomConfig.get().getBoolean("cps.enabled")) return;

        long timeNow = System.currentTimeMillis() / 1000L;
        Player player = event.getPlayer();

        // If the player bypasses checks, ignore them
        if(player.hasPermission("acorn.bypass")) return;

        // If the player isn't left clicking, don't check them
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;

        // Do some weird stuff (I can't remember what this does to be honest)
        if(cpsTime.containsKey(player.getName())){
            if(cpsTime.get(player.getName())+1 >= timeNow){
                int oldCPS = cps.get(player.getName());
                cps.put(player.getName(), oldCPS+1);
            } else {
                if(cps.get(player.getName()) >= CustomConfig.get().getInt("cps.cps-limit")){
                    cpsEvent(player);
                    flag(player, "cps", "maintain an impossible click speed (" + cps.get(player.getName()) + "cps)", 1);
                }

                cps.remove(player.getName());
                cpsTime.remove(player.getName());
            }
        } else {
            cps.remove(player.getName());
            cps.put(player.getName(), 1);
            cpsTime.put(player.getName(), timeNow);
        }
    }
}
