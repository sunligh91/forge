package com.github.sunligh91.forge.main.command;

import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import com.github.sunligh91.forge.main.utils.ClearSourceUtil;
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

import java.util.*;

public class ForgeItemCommandExecutor implements CommandExecutor {

    private Plugin plugin;

    public ForgeItemCommandExecutor(Plugin plugin) {
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p1 = (Player) sender;
            if (args.length>0) {
                if ("open".equals(args[0])) {
                    Inventory inventory = Bukkit.createInventory(p1, 9, ForgeItemTextLoader.PLUGIN_GUI_TITLE);
                    makeGlass(inventory);

                    inventory.setItem(8, getItemStack(Material.SIGN, "§a锻造", "§c请将要锻造的材料放入左侧空白区域"));
                    p1.closeInventory();
                    p1.openInventory(inventory);
                }

                if ("add".equals(args[0])){
                    if (args.length>1 && !args[1].equals("")) {
                        if(sender.isOp()) {
                            Inventory inventory = Bukkit.createInventory(p1, 9, ForgeItemTextLoader.PLUGIN_GUI_TITLE + "（添加模式）");
                            makeGlass(inventory);
                            inventory.setItem(8, getItemStack(Material.SIGN, "§a添加锻造记录：" + args[1], "§c请依次放入锻造材料，锻造产出物品，最后点击我"));
                            p1.closeInventory();
                            p1.openInventory(inventory);
                        } else {
                            sender.sendMessage("你没有权限使用此命令！");
                        }
                    }
                }
            }
        }
        return true;
    }

    private void makeGlass(Inventory inventory){
        for (int i = 0; i < 9; ++i) {
            inventory.setItem(i, getItemStack(Material.THIN_GLASS, "§8无效区域"));
        }
        ClearSourceUtil.clear(inventory);
        inventory.setItem(6, null);
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
