package xyz.aunto.acorn.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Objects;

public class CustomConfig {
    private static File file;
    private static FileConfiguration config;

    public static void load(){
        // Config file loading. This should NEVER need to be changed!
        File file = new File(Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Acorn")).getDataFolder(), "config.yml");

        if(!file.exists()){
            Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("Acorn")).saveDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return config;
    }

    /*public static void save(){
        try {
            config.save(file);
        } catch(IOException e) {
            // Something has gone very, very wrong.
        }
    }*/
}
