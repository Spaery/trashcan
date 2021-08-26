package io.github.spaery.trashcan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Trashcan makes it so that when a chest is renamed to 'Trashcan' 
 * any item in it's inventory will be deleted after a configurable amount of time
 */
public class Trashcan extends JavaPlugin
{
    Trashcan plugin = this;
    FileConfiguration config = plugin.getConfig();
    @Override
    public void onEnable(){
        plugin.saveDefaultConfig();
        
    }
    @Override
    public void onDisable(){

    }
    public FileConfiguration getDefaultConfig(){
        return config;
    }
}
