package com.spaery.trashcan;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

public final class Trashcan extends JavaPlugin {

    private static Trashcan plugin;
    FileConfiguration config = this.getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Trashcan name: "+config.getString("NameOfChest").strip());
        plugin = this;
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new TrashcanListener(), plugin);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Trashcan trashed!");
        HandlerList.unregisterAll(plugin);
    }

    public FileConfiguration getDefaultConfig(){
        return config;
    }

    public static Trashcan getPlugin(){
        return plugin;
    }
}
