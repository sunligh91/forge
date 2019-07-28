package com.github.sunligh91.forge.listener;


import com.github.sunligh91.forge.gui.GuiUtil;
import com.github.sunligh91.forge.main.ForgeItem;
import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CheckListener implements Listener {

    private ForgeItem plugin;

    public CheckListener(ForgeItem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void guiClicked(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getRawSlot() >= 0) {
            if (event.getInventory().getTitle().startsWith(ForgeItemTextLoader.PLUGIN_GUI_TITLE)){
                GuiUtil.playerClickedGui(event,plugin);
            }
        }
    }

    @EventHandler
    public void guiClosed(InventoryCloseEvent event){
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (inventory.getTitle().startsWith(ForgeItemTextLoader.PLUGIN_GUI_TITLE)){
                ItemStack item = inventory.getItem(3);
                if (item != null){
                    event.getPlayer().getInventory().setItem(event.getPlayer().getInventory().firstEmpty(),item);
                    inventory.setItem(3,null);
                }
            }
        }
    }

    @EventHandler
    public void guiItemPut(InventoryDragEvent event){

    }
}
