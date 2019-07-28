package com.github.sunligh91.forge.main.command;

import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ForgeItemCommandExecutor implements CommandExecutor {

    private Plugin plugin;

    public ForgeItemCommandExecutor(Plugin plugin) {
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p1 = (Player) sender;
            Inventory inventory = Bukkit.createInventory(p1,9, ForgeItemTextLoader.PLUGIN_GUI_TITLE);
            for(int i = 0; i < 9; ++i) {
                inventory.setItem(i, getItemStack(Material.THIN_GLASS, "§8无效区域"));
            }
            inventory.setItem(3, null);
            inventory.setItem(5, getItemStack(Material.SIGN, "§a鉴定", "§c请将要坚定的物品放入左侧空白区域"));
            p1.closeInventory();
            p1.openInventory(inventory);
        }
        return true;
    }

    private ItemStack getItemStack(Material material, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        List<String> loreList = new ArrayList<>();
        Collections.addAll(loreList, lore);
        itemMeta.setLore(loreList);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private void itemCheck(Inventory inventory){
        Set<String> keys = plugin.getConfig().getKeys(false);
        for (String key : keys) {

        }
    }
}
