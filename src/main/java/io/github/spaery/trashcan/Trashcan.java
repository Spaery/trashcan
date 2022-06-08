package io.github.spaery.trashcan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Trashcan makes it so that when a chest is renamed to 'Trashcan' 
 * any item in its inventory will be deleted after a configurable amount of time
 */
public class Trashcan extends JavaPlugin
{
    private static Trashcan plugin;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable(){
        plugin = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TrashcanListener(), plugin);
    }

    @Override
    public void onDisable(){
        HandlerList.unregisterAll(plugin);
    }

    public FileConfiguration getDefaultConfig(){
        return config;
    }

    public static Trashcan getPlugin(){
        return plugin;
    }
}
