package io.github.spaery.trashcan;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
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
            nameCheck(inv);
        }
    }
    /**
     * Verifys the name of the chest is the name specified by NameOfChest in config.yml
     * all checks that if its a double chest that both chest are named correctly
     * @param inv the inventory passed by the interaction event in chestInteraction()
     */
    public void nameCheck(Inventory inv){
        String trashcanName = config.getString("NameOfChest").strip();
        try {
            Chest chest = (Chest) inv.getHolder();
            if(chest.getCustomName().equals(trashcanName)){
                deleteItems(inv);
            }
        // If the above try statement throws a ClassCastException that means
        // the chest is a double chest
        } catch (ClassCastException e) {
            DoubleChest chest = (DoubleChest) inv.getHolder();
            Chest newChestLeft = (Chest) chest.getLeftSide();
            Chest newChestRight = (Chest) chest.getRightSide();
            try {
                if(newChestLeft.getCustomName().equals(trashcanName) && newChestRight.getCustomName().equals(trashcanName)){
                    deleteItems(inv);
                }
            // If the above try statement throws a NullPointerException then 
            // one of the two halves are not renamed
            } catch (NullPointerException f) {
                return;
            }
        // If the try statement throws a NullPointerException then
        // the chest is not renamed
        } catch (NullPointerException e){
            return;
        }
    }
    /**
     * Deletes the items in the closed inventory after the time
     * specified with TimeBeforeDeletion in config.yml if the
     * inventory is not empty
     * @param inv the inventory passed from nameCheck()
     */
    public void deleteItems(Inventory inv){
        if(!inv.isEmpty()){
            Bukkit.getScheduler().runTaskLater(plugin, new Runnable(){
                @Override
                public void run() {
                    inv.clear();          
                }
            }, 20*config.getInt("TimeBeforeDeletion"));
        }
    }
}

