package io.github.spaery.trashcan;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.configuration.file.FileConfiguration;


public class TrashcanListener implements Listener {
    Trashcan plugin = Trashcan.getPlugin();
    FileConfiguration config = plugin.getDefaultConfig();
    /**
     * The function which catches the interaction event and passes the inventory to nameCheck()
     * @param event Interaction event
     */
    @EventHandler
    public void chestInteraction(InventoryCloseEvent event){
        Inventory inv = event.getInventory();
        if(inv.getType() == InventoryType.CHEST){
            nameCheck(inv,event);
        } 
    }

    /**
     * The function which detects hopper transfer event passes event to nameCheck()
     * @param event
     */
    @EventHandler
    public void hopperInteraction(InventoryMoveItemEvent event){
        if(event.getDestination().getType().equals(InventoryType.CHEST)){
            nameCheck(event.getDestination(), event);
        }
    }
    /**
     * Verifies the name of the chest is the name specified by NameOfChest in config.yml
     * all checks that if it's a double chest that both chest are named correctly
     * @param inv the inventory passed by the interaction event in chestInteraction() or hopperInteraction()
     */
    public void nameCheck(Inventory inv, Event event){
        String trashcanName = config.getString("NameOfChest").strip();
        if(inv.getHolder() instanceof Chest chest){
            try {
                if(chest.getCustomName().equals(trashcanName)){
                    deleteItems(inv,event);
                }
            } catch (NullPointerException ignored) {
            }
            
        } else if(inv.getHolder() instanceof DoubleChest dchest) {
            Chest newChestLeft = (Chest) dchest.getLeftSide();
            Chest newChestRight = (Chest) dchest.getRightSide();
            try {
                if(newChestLeft.getCustomName().equals(trashcanName) && newChestRight.getCustomName().equals(trashcanName)){
                    deleteItems(inv,event);
                }
            } catch (NullPointerException ignored) {
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
        if(e.getEventName().equals("InventoryMoveItemEvent")){
            inv.clear();
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, () -> inv.clear(), 20L * config.getInt("TimeBeforeDeletion"));
        }
    }
}