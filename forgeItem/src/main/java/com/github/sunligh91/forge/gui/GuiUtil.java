package com.github.sunligh91.forge.gui;


import com.github.sunligh91.forge.main.ForgeItem;
import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiUtil {


    public static void playerClickedGui(InventoryClickEvent event, ForgeItem plugin){
        Inventory inventory = event.getInventory();
        String title = inventory.getTitle();
        Player player = (Player)event.getWhoClicked();
        int rawSlot = event.getRawSlot();
        if(ForgeItemTextLoader.PLUGIN_GUI_TITLE.equals(title)){
            try {
                menuClicked(player, rawSlot, inventory,event,plugin);
            } catch (Exception e){
                event.setCancelled(true);
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" " + ForgeItemTextLoader.EQUIP_CHECK_FALSE);
                e.printStackTrace();
            }

        }
    }

    private static void menuClicked(Player player, int rawSlot, Inventory inventory, InventoryClickEvent event, ForgeItem plugin) {
        if (rawSlot == 5) {
            if (inventory.getItem(3) == null) {
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e请在左侧放入要鉴定的物品");
                event.setCancelled(true);
            } else {
                ItemStack item = inventory.getItem(3);
                ItemMeta itemMeta = item.getItemMeta();
                List<String> lore = null;
                try {
                    lore = itemMeta.getLore();
                } catch (NullPointerException e){
                    e.printStackTrace();
                    event.setCancelled(true);
                    return;
                }
                boolean canCheck = false;
                for (int i = 0; i < lore.size(); i++) {
                    String s = lore.get(i);
                    if (s.contains("未鉴定的")){
                        String matcher = getMatcher("[未鉴定的][\\u4e00-\\u9fa5]+", s).substring(4);
                        FileConfiguration config = plugin.getConfig();
                        //获取到对应的物品参数列表
                        MemorySection items = (MemorySection) config.get("items");
                        MemorySection memorySection = (MemorySection)items.get(matcher);
                    }
                }
                //确认是否完成鉴定，如果鉴定错误弹出消息
                if (!canCheck) {
                    player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" " + ForgeItemTextLoader.EQUIP_CHECK_FALSE);
                }
                //终止
                event.setCancelled(true);
            }
        }else if(rawSlot != 3 && rawSlot>=0 && rawSlot<=9 ){
            event.setCancelled(true);
        }
    }

    public static String getMatcher(String regex, String source) {
        Matcher matcher = Pattern.compile(regex).matcher(source);
        String result = "";
        while (matcher.find()) {
            result = matcher.group(0);
        }
        return result;
    }


}
