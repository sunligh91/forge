package com.github.sunligh91.forge.listener;


import com.github.sunligh91.forge.gui.GuiUtil;
import com.github.sunligh91.forge.main.ForgeItems;
import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import com.github.sunligh91.forge.main.utils.ClearSourceUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;
import org.hamcrest.internal.ArrayIterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckListener implements Listener {

    private ForgeItems plugin;

    public CheckListener(ForgeItems plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void guiClicked(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player && event.getRawSlot() >= 0) {
            if (event.getInventory().getTitle().contains(ForgeItemTextLoader.PLUGIN_GUI_TITLE)){
                if (event.getInventory().getTitle().contains(ForgeItemTextLoader.PLUGIN_GUI_TITLE + "（添加模式）")){
                    GuiUtil.playerClickedGuiAddMode(event,plugin);
                } else {
                    GuiUtil.playerClickedGui(event, plugin);
                }
            }

        }
    }

    @EventHandler
    public void guiClosed(InventoryCloseEvent event){
        if (event.getPlayer() instanceof Player) {
            Inventory inventory = event.getInventory();
            if (StringUtils.isNotEmpty(inventory.getTitle())) {
                if(inventory.getTitle().contains(ForgeItemTextLoader.PLUGIN_GUI_TITLE)) {
                    ItemStack[] items = {inventory.getItem(0),inventory.getItem(1),inventory.getItem(2),inventory.getItem(3),inventory.getItem(4),inventory.getItem(6)};
                    ClearSourceUtil.clear(inventory);
                    inventory.setItem(6, null);
                    for (ItemStack itemStack : items) {
                        if (itemStack!=null) {
                            event.getPlayer().getWorld().dropItem(event.getPlayer().getLocation(), itemStack);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void guiItemPut(InventoryDragEvent event){

    }
}
