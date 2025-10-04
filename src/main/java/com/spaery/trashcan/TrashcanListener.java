package com.spaery.trashcan;

import org.bukkit.Bukkit;
import org.bukkit.block.Barrel;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class TrashcanListener implements Listener {
    Trashcan plugin = Trashcan.getPlugin();
    FileConfiguration config = plugin.getDefaultConfig();

    /**
     * The function which catches the interaction event and passes the inventory to nameCheck()
     * @param event Interaction event
     */
    @EventHandler
    public void chestInteraction(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        if ((inv.getType() == InventoryType.CHEST) || (inv.getType() == InventoryType.BARREL)){
            nameCheck(inv,event);
        }
    }

    /**
     * The function which detects hopper transfer events and passes event to nameCheck()
     * @param event
     */
    @EventHandler
    public void hopperInteraction(InventoryMoveItemEvent event) {
        if (event.getDestination().getType().equals(InventoryType.CHEST) || event.getDestination().getType().equals(InventoryType.BARREL)) {
            nameCheck(event.getDestination(),event);
        }
    }

    /**
     * The function checks the name of the inventory which triggered the event
     * if the inventory's name is what's configured in config.yml, then it moves on
     * to the deleteItems() function. Otherwise, it ignores the event. For double chests,
     * both sides of the chest have to be named the configured name.
     * @param inv triggered inventory
     * @param event triggered event
     */
    public void nameCheck(Inventory inv, Event event){
        String trashcanName = config.getString("NameOfChest").strip();

        if (inv.getHolder() instanceof Chest chest){ // Single chest handler
            try {
                if (chest.getCustomName().equals(trashcanName)) {
                    deleteItems(inv, event);
                }
            } catch (NullPointerException ignored) {
            }
        } else if (inv.getHolder() instanceof DoubleChest dchest) { // Double chest handler
            Chest newChestLeft = (Chest) dchest.getLeftSide();
            Chest newChestRight = (Chest) dchest.getRightSide();
            try {
                if (newChestLeft.getCustomName().equals(trashcanName) && newChestRight.getCustomName().equals(trashcanName)){
                    deleteItems(inv, event);
                }
            } catch (NullPointerException ignored){
            }
        } else if (inv.getHolder() instanceof Barrel barrel) {
            try {
                if (barrel.getCustomName().equals(trashcanName)) {
                    deleteItems(inv, event);
                }
            } catch (NullPointerException ignored){
            }
        }
    }

    /**
     * Deletes the items in the closed inventory after the time
     * specified with TimeBeforeDeletion in config.yml if the
     * inventory is not empty
     * @param inv the inventory passed from nameCheck()
     */
    public void deleteItems(Inventory inv, Event e){
        inv.getStorageContents();
        if (e.getEventName().equals("PaperInventoryMoveItemEvent")){
            inv.clear();
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> inv.clear(), 20L * config.getInt("TimeBeforeDeletion"));
        }
    }
}
