package xyz.aunto.acorn.checks.combat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import xyz.aunto.acorn.files.ViolationHandler;

import java.util.HashMap;

public class FastBow extends ViolationHandler implements Listener {
    HashMap<String, Integer> shots = new HashMap<>();
    HashMap<String, Long> shotsTime = new HashMap<>();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerShoot(EntityShootBowEvent event){
        long timeNow = System.currentTimeMillis() / 1000L;
        Player player = Bukkit.getPlayer(event.getEntity().getUniqueId());
        if(player == null) return;

        // Do some weird stuff (I can't remember what this does to be honest)
        if(shotsTime.containsKey(player.getName())){
            if(shotsTime.get(player.getName())+1 >= timeNow){
                int oldCPS = shots.get(player.getName());
                shots.put(player.getName(), oldCPS+1);
            } else {
                if(shots.get(player.getName()) >= 15){
                    Bukkit.broadcastMessage("Too many shots!  SPS: " + shots.get(player.getName()));
                }

                shots.remove(player.getName());
                shotsTime.remove(player.getName());
            }
        } else {
            shots.remove(player.getName());
            shots.put(player.getName(), 1);
            shotsTime.put(player.getName(), timeNow);
        }
    }
}