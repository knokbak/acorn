package xyz.aunto.acorn.files;

import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;

public class ViolationHandler {
    HashMap<String, Long> flyVL = new HashMap<>();
    HashMap<String, Long> boatflyVL = new HashMap<>();
    HashMap<String, Long> jesusVL = new HashMap<>();
    HashMap<String, Long> phaseVL = new HashMap<>();
    HashMap<String, Long> speedVL = new HashMap<>();
    HashMap<String, Long> reachVL = new HashMap<>();
    HashMap<String, Long> cpsVL = new HashMap<>();
    HashMap<String, Long> knockbackVL = new HashMap<>();
    HashMap<String, Long> ireachVL = new HashMap<>();
    HashMap<String, Long> fplaceVL = new HashMap<>();

    public void flag(Player player, String check, String view, Integer VL){
        if(CustomConfig.get().getBoolean("dev")){
            broadcast("&6&lACORN &8| &b&lDEV  &e" + player.getName() + " &7attempted to &e" + view + "&7.", "acorn.notify.dev");
        }

        if(check.equals("boatfly")){
            int newVL = addVL(player, "boatfly", VL);

            if(newVL == CustomConfig.get().getInt("boatfly.actions.alert") && CustomConfig.get().getInt("boatfly.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("boatfly.actions.kick") && CustomConfig.get().getInt("boatfly.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("boatfly.actions.ban") && CustomConfig.get().getInt("boatfly.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.contains("fly")){
            int newVL = addVL(player, "fly", VL);

            if(newVL == CustomConfig.get().getInt("fly.actions.alert") && CustomConfig.get().getInt("fly.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("fly.actions.kick") && CustomConfig.get().getInt("fly.actions.kick") != 0){
                if(CustomConfig.get().getBoolean("fly.actions.teleport-on-kick")) player.teleport(player.getWorld().getHighestBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).getLocation());
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("fly.actions.ban") && CustomConfig.get().getInt("fly.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("jesus")){
            int newVL = addVL(player, "jesus", VL);

            if(newVL == CustomConfig.get().getInt("jesus.actions.alert") && CustomConfig.get().getInt("jesus.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("jesus.actions.kick") && CustomConfig.get().getInt("jesus.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("jesus.actions.ban") && CustomConfig.get().getInt("jesus.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("phase")){
            int newVL = addVL(player, "phase", VL);

            if(newVL == CustomConfig.get().getInt("phase.actions.alert") && CustomConfig.get().getInt("phase.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("phase.actions.kick") && CustomConfig.get().getInt("phase.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("phase.actions.ban") && CustomConfig.get().getInt("phase.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("speed")){
            int newVL = addVL(player, "speed", VL);

            if(newVL == CustomConfig.get().getInt("speed.actions.alert") && CustomConfig.get().getInt("speed.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("speed.actions.kick") && CustomConfig.get().getInt("speed.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("speed.actions.ban") && CustomConfig.get().getInt("speed.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("reach")){
            int newVL = addVL(player, "reach", VL);

            if(newVL == CustomConfig.get().getInt("reach.actions.alert") && CustomConfig.get().getInt("reach.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("reach.actions.kick") && CustomConfig.get().getInt("reach.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("reach.actions.ban") && CustomConfig.get().getInt("reach.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("cps")){
            int newVL = addVL(player, "cps", VL);

            if(newVL == CustomConfig.get().getInt("cps.actions.alert") && CustomConfig.get().getInt("cps.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("cps.actions.kick") && CustomConfig.get().getInt("cps.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("cps.actions.ban") && CustomConfig.get().getInt("cps.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("knockback")){
            int newVL = addVL(player, "knockback", VL);

            if(newVL == CustomConfig.get().getInt("knockback.actions.alert") && CustomConfig.get().getInt("knockback.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("knockback.actions.kick") && CustomConfig.get().getInt("knockback.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("knockback.actions.ban") && CustomConfig.get().getInt("knockback.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("interactreach")){
            int newVL = addVL(player, "interactreach", VL);

            if(newVL == CustomConfig.get().getInt("interactreach.actions.alert") && CustomConfig.get().getInt("interactreach.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("interactreach.actions.kick") && CustomConfig.get().getInt("interactreach.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("interactreach.actions.ban") && CustomConfig.get().getInt("interactreach.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        } else
        if(check.equals("fastplace")){
            int newVL = addVL(player, "fastplace", VL);

            if(newVL == CustomConfig.get().getInt("fastplace.actions.alert") && CustomConfig.get().getInt("fastplace.actions.alert") != 0){
                announce(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("fastplace.actions.kick") && CustomConfig.get().getInt("fastplace.actions.kick") != 0){
                kick(player, view, check, newVL);
            } else
            if(newVL >= CustomConfig.get().getInt("fastplace.actions.ban") && CustomConfig.get().getInt("fastplace.actions.ban") != 0){
                ban(player, view, check, newVL);
            }
        }
    }

    private Integer addVL(Player player, String check, Integer VL){
        if(VL == null) return 0;

        long timeNow = System.currentTimeMillis() / 1000L;
        int newVL = 0;

        switch(check){
            case "fly": {
                UUID uuid = UUID.randomUUID();
                flyVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : flyVL.keySet()) {
                    long expires = flyVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        flyVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "boatfly": {
                UUID uuid = UUID.randomUUID();
                boatflyVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : boatflyVL.keySet()) {
                    long expires = boatflyVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        boatflyVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "jesus": {
                UUID uuid = UUID.randomUUID();
                jesusVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : jesusVL.keySet()) {
                    long expires = jesusVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        jesusVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "phase": {
                UUID uuid = UUID.randomUUID();
                phaseVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : phaseVL.keySet()) {
                    long expires = phaseVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        phaseVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "speed": {
                UUID uuid = UUID.randomUUID();
                speedVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : speedVL.keySet()) {
                    long expires = speedVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        speedVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "reach": {
                UUID uuid = UUID.randomUUID();
                reachVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : reachVL.keySet()) {
                    long expires = reachVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        reachVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "cps": {
                UUID uuid = UUID.randomUUID();
                cpsVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : cpsVL.keySet()) {
                    long expires = cpsVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        cpsVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "knockback": {
                UUID uuid = UUID.randomUUID();
                knockbackVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : knockbackVL.keySet()) {
                    long expires = knockbackVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        knockbackVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "interactreach": {
                UUID uuid = UUID.randomUUID();
                ireachVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : ireachVL.keySet()) {
                    long expires = ireachVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        ireachVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
            case "fastplace": {
                UUID uuid = UUID.randomUUID();
                fplaceVL.put(player.getName() + " " + uuid, timeNow + CustomConfig.get().getInt("vl-expiry"));

                for (String i : fplaceVL.keySet()) {
                    long expires = fplaceVL.get(i);
                    String username = i.split(" ")[0];

                    if (expires <= timeNow) {
                        fplaceVL.remove(i);
                    } else if (username.equals(player.getName())) newVL++;
                }
                break;
            }
        }

        return newVL;
    }

    private void announce(Player player, String view, String check, Integer VL){
        String message = CustomConfig.get().getString("messages.alert");
        assert message != null;
        message = message.replace("%player%", player.getName());
        message = message.replace("%location%", player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
        message = message.replace("%check%", check);
        message = message.replace("%check_message%", view);
        message = message.replace("%vl%", VL.toString());

        broadcast(message, "acorn.notify");
    }

    private void kick(Player player, String view, String check, Integer VL){
        String message = CustomConfig.get().getString("messages.kickalert");
        assert message != null;
        message = message.replace("%player%", player.getName());
        message = message.replace("%location%", player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
        message = message.replace("%check%", check);
        message = message.replace("%check_message%", view);
        message = message.replace("%vl%", VL.toString());
        broadcast(message, "acorn.notify");

        String kickMessage = CustomConfig.get().getString("messages.kick");
        assert kickMessage != null;
        kickMessage = kickMessage.replace("%player%", player.getName());
        kickMessage = kickMessage.replace("%location%", player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
        kickMessage = kickMessage.replace("%check%", check);
        kickMessage = kickMessage.replace("%check_message%", view);
        kickMessage = kickMessage.replace("%vl%", VL.toString());
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', kickMessage));
    }

    private void ban(Player player, String view, String check, Integer VL){
        String message = CustomConfig.get().getString("messages.banalert");
        assert message != null;
        message = message.replace("%player%", player.getName());
        message = message.replace("%location%", player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
        message = message.replace("%check%", check);
        message = message.replace("%check_message%", view);
        message = message.replace("%vl%", VL.toString());
        broadcast(message, "acorn.notify");

        String banMessage = CustomConfig.get().getString("messages.ban");
        assert banMessage != null;
        banMessage = banMessage.replace("%player%", player.getName());
        banMessage = banMessage.replace("%location%", player.getLocation().getBlockX() + " " + player.getLocation().getBlockY() + " " + player.getLocation().getBlockZ());
        banMessage = banMessage.replace("%check%", check);
        banMessage = banMessage.replace("%check_message%", view);
        banMessage = banMessage.replace("%vl%", VL.toString());
        player.kickPlayer(ChatColor.translateAlternateColorCodes('&', banMessage));
        player.getServer().getBanList(BanList.Type.NAME).addBan(player.getName(), ChatColor.translateAlternateColorCodes('&', banMessage), null, "Acorn Cheat Mitigation");
    }

    public void boatFlyEvent(Player player, PlayerMoveEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("boatfly.actions.cancel");
        int leave = CustomConfig.get().getInt("boatfly.actions.leave");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : boatflyVL.keySet()){
            long expires = boatflyVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                boatflyVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
        if(VL >= leave && leave != 0) player.leaveVehicle();
    }

    public void flyEvent(Player player, PlayerMoveEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("fly.actions.cancel");
        int teleport = CustomConfig.get().getInt("fly.actions.teleport");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : flyVL.keySet()){
            long expires = flyVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                flyVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= teleport && teleport != 0){
            Location location = player.getWorld().getHighestBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).getLocation();
            player.teleport(location);
        } else
        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void jesusEvent(Player player, PlayerMoveEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("jesus.actions.cancel");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : jesusVL.keySet()){
            long expires = jesusVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                jesusVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void phaseEvent(Player player, PlayerMoveEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("phase.actions.cancel");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : phaseVL.keySet()){
            long expires = phaseVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                phaseVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void speedEvent(Player player, PlayerMoveEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("speed.actions.cancel");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : speedVL.keySet()){
            long expires = speedVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                speedVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void reachEvent(Player player, EntityDamageByEntityEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("reach.actions.cancel");
        int kill = CustomConfig.get().getInt("reach.actions.kill");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : reachVL.keySet()){
            long expires = reachVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                reachVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
        if(VL == kill && kill != 0) player.setHealth(0);
    }

    public void cpsEvent(Player player){
        int VL = 1;
        int kill = CustomConfig.get().getInt("cps.actions.kill");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : cpsVL.keySet()){
            long expires = cpsVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                cpsVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL == kill && kill != 0) player.setHealth(0);
    }

    public void knockbackEvent(Player player){
        int VL = 1;
        int kill = CustomConfig.get().getInt("knockback.actions.kill");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : knockbackVL.keySet()){
            long expires = knockbackVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                knockbackVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL == kill && kill != 0) player.setHealth(0);
    }

    public void interactReachEvent(Player player, PlayerInteractEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("interactreach.actions.cancel");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : ireachVL.keySet()){
            long expires = ireachVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                ireachVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void fastPlaceEvent(Player player, BlockPlaceEvent event){
        int VL = 1;
        int cancel = CustomConfig.get().getInt("fastplace.actions.cancel");
        long timeNow = System.currentTimeMillis() / 1000L;

        for(String i : fplaceVL.keySet()){
            long expires = fplaceVL.get(i);
            String username = i.split(" ")[0];

            if(expires <= timeNow){
                fplaceVL.remove(i);
            } else
            if(username.equals(player.getName())) VL++;
        }

        if(VL >= cancel && cancel != 0) event.setCancelled(true);
    }

    public void broadcast(String message, String permission){
        Bukkit.getServer().getOnlinePlayers().forEach(player -> {
            if(player.hasPermission(permission)) player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        });
    }
}