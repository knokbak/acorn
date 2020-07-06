package xyz.aunto.acorn;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import xyz.aunto.acorn.files.CustomConfig;

import xyz.aunto.acorn.checks.combat.*;
import xyz.aunto.acorn.checks.movement.*;
import xyz.aunto.acorn.checks.player.*;
import xyz.aunto.acorn.checks.world.*;

import org.bukkit.plugin.java.JavaPlugin;

public final class Acorn extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Acorn is attempting to start up.");
        getLogger().info("Attempting to load the config file (config.yml)...");

        // Run config file loading
        CustomConfig.load();

        // General logging
        if(CustomConfig.get() != null) getLogger().info("Config file (config.yml) has been loaded!");
        assert CustomConfig.get() != null;
        if(CustomConfig.get().getBoolean("dev")) getLogger().warning("YOU ARE RUNNING ACORN IN DEV MODE - DO NOT DO THIS UNLESS YOU KNOW WHAT YOU ARE DOING!");

        // Create event listeners
        if(CustomConfig.get().getBoolean("dev")) getLogger().info("Attempting to register event listeners...");
        getServer().getPluginManager().registerEvents(new Fly(), this);
        getServer().getPluginManager().registerEvents(new Jesus(), this);
        getServer().getPluginManager().registerEvents(new Speed(), this);
        getServer().getPluginManager().registerEvents(new BoatFly(), this);
        getServer().getPluginManager().registerEvents(new Phase(), this);
        getServer().getPluginManager().registerEvents(new Reach(), this);
        getServer().getPluginManager().registerEvents(new CPS(), this);
        getServer().getPluginManager().registerEvents(new AntiKockback(), this);
        getServer().getPluginManager().registerEvents(new NoFallDamage(), this);
        //getServer().getPluginManager().registerEvents(new FastBow(), this);
        getServer().getPluginManager().registerEvents(new InteractReach(), this);
        getServer().getPluginManager().registerEvents(new FastPlace(), this);
        if(CustomConfig.get().getBoolean("dev")) getLogger().info("Registered event listeners!");

        // Register permissions
        if(CustomConfig.get().getBoolean("dev")) getLogger().info("Attempting to register permissions...");
        getServer().getPluginManager().addPermission(new Permission("acorn.notify"));
        getServer().getPluginManager().addPermission(new Permission("acorn.notify.dev"));
        if(CustomConfig.get().getBoolean("dev")) getLogger().info("Registered permissions!");

        // Finish
        getLogger().info("Acorn has started and is now monitoring actions and users.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().warning("Disable request detected! If you are not reloading plugins or stopping the server, this is a bug!");
        getLogger().info("Disable command submitted.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("acorn")){
            if(!(sender instanceof Player)){
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lACORN  &7This server is running &eAcorn Anti-Cheat&7.\n&6&lACORN  &7Looking for the config file? &eplugins/Acorn/config.yml&7.\n&6&lACORN\n&6&lACORN  &7Commands:\n&6&lACORN  &d/acorn&7, &d/acorn-reload&7, &d/acorn-checks\n&6&lACORN\n&6&lACORN  &7Version: &cBETA - 1.0\n&6&lACORN  &7GitHub: &ehttps://github.com/sysollie/acorn"));

            return true;
        } else
        if(cmd.getName().equalsIgnoreCase("acorn-reload")){
            if(!(sender instanceof Player)){
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            if(!sender.hasPermission("acorn.command.reload")){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lACORN  &cYou must have the &eacorn.command.reload &cpermission to reload the Acorn configuration!"));
                return true;
            }

            CustomConfig.load();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lACORN  &aConfiguration reload request submitted!"));
            return true;
        } else
        if(cmd.getName().equalsIgnoreCase("acorn-checks")){
            if(!(sender instanceof Player)){
                sender.sendMessage("This command can only be run by a player.");
                return true;
            }

            if(!sender.hasPermission("acorn.command.checks")){
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lACORN  &cYou must have the &eacorn.command.checks &cpermission to reload the Acorn configuration!"));
                return true;
            }

            String message = "&6&lACORN  &7Checks:\n";

            if(CustomConfig.get().getBoolean("fly.enabled")){
                message += "&aFly&7, ";
            } else message += "&cFly&7, ";

            if(CustomConfig.get().getBoolean("fly.Y-level-monitoring.enabled")){
                message += "&aY Level Monitoring&7, ";
            } else message += "&cYLevelMonitoring&7, ";

            if(CustomConfig.get().getBoolean("boatfly.enabled")){
                message += "&aBoatFly&7, ";
            } else message += "&cBoatFly&7, ";

            if(CustomConfig.get().getBoolean("phase.enabled")){
                message += "&aPhase&7, ";
            } else message += "&cPhase&7, ";

            if(CustomConfig.get().getBoolean("jesus.enabled")){
                message += "&aJesus&7, ";
            } else message += "&cJesus&7, ";

            if(CustomConfig.get().getBoolean("speed.enabled")){
                message += "&aSpeed&7, ";
            } else message += "&cSpeed&7, ";

            if(CustomConfig.get().getBoolean("reach.enabled")){
                message += "&aReach&7, ";
            } else message += "&cReach&7, ";

            if(CustomConfig.get().getBoolean("cps.enabled")){
                message += "&aCPS&7, ";
            } else message += "&cCPS&7, ";

            if(CustomConfig.get().getBoolean("fastplace.enabled")){
                message += "&aFastPlace&7, ";
            } else message += "&cFastPlace&7, ";

            if(CustomConfig.get().getBoolean("fastplace.enabled")){
                message += "&aFastBow&7, ";
            } else message += "&cFastBow&7, ";

            if(CustomConfig.get().getBoolean("interactreach.enabled")){
                message += "&aInteract Reach&7, ";
            } else message += "&cInteract Reach&7, ";

            message += "&eFastBow&7, ";

            if(CustomConfig.get().getBoolean("knockback.enabled")){
                message += "&aAntiKnockback";
            } else message += "&cAntiKnockback";

            CustomConfig.load();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            return true;
        }

        return false;
    }
}
