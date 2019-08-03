package com.github.sunligh91.forge.gui;


import com.github.sunligh91.forge.main.ForgeItems;
import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import com.github.sunligh91.forge.main.utils.ClearSourceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import think.rpgitems.api.RPGItems;
import think.rpgitems.item.RPGItem;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuiUtil {


    public static void playerClickedGui(InventoryClickEvent event, ForgeItems plugin){
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


    public static void playerClickedGuiAddMode(InventoryClickEvent event, ForgeItems plugin){
        Inventory inventory = event.getInventory();
        String title = inventory.getTitle();
        Player player = (Player)event.getWhoClicked();
        int rawSlot = event.getRawSlot();
        if((ForgeItemTextLoader.PLUGIN_GUI_TITLE+"（添加模式）").equals(title)){
            try {
                menuClickedAddMode(player, rawSlot, inventory,event,plugin);
            } catch (Exception e){
                event.setCancelled(true);
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" " + ForgeItemTextLoader.EQUIP_CHECK_FALSE);
                e.printStackTrace();
            }

        }
    }


    private static HashMap<String,Boolean> getSourceMap(Inventory inventory, InventoryClickEvent event){
        HashMap<String,Boolean> map = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            if (inventory.getItem(i) != null && inventory.getItem(i).getItemMeta()!=null){
                RPGItem rpgItem = RPGItems.toRPGItem(inventory.getItem(i));
                if (rpgItem==null){
                    map.put(inventory.getItem(i).getType().getId()+","+i,false);
                } else {
                    map.put(rpgItem.getID()+","+i,true);
                }
            }
        }
        return map;
    }

    private static void menuClicked(Player player, int rawSlot, Inventory inventory, InventoryClickEvent event, ForgeItems plugin) {
        if (rawSlot == 8) {
            HashMap<String,Boolean> map = getSourceMap(inventory,event);
            if (map.size()<3){
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e请在左侧放入足够的锻造材料");
                event.setCancelled(true);
                return;
            }
            FileConfiguration config = plugin.getConfig();
            ConfigurationSection itemsList = config.getConfigurationSection("items");
            Set<String> items = itemsList.getKeys(false);
            int total = 0;
            for (String s : map.keySet()) {
                total = total + Integer.parseInt(s.split(",")[0]);
            }

            Map<String,String> itemMap =new HashMap<>();
            for (String item : items) {
                itemMap.put(item.split(",")[0],item);
            }
            String item = itemMap.get(total + "");
            if (item != null){
                int i = Integer.parseInt(item.split(",")[0]);
                if (i == total){
                    ConfigurationSection itemsListConf = itemsList.getConfigurationSection(item);
                    ArrayList<String> configCache = checkDouble(itemsListConf, map);
                    if (configCache.size() == 0){
                        ClearSourceUtil.clear(inventory);
                        ConfigurationSection value = itemsListConf.getConfigurationSection("value");
                        String val = value.getKeys(false).iterator().next();
                        boolean rpg = value.getBoolean(val);
                        ItemStack itemStack = null;
                        int i1 = Integer.parseInt(val.split(",")[0]);
                        if (rpg){
                            RPGItem rpgItemById = RPGItems.getRPGItemById(i1);
                            itemStack =rpgItemById.toItemStack();
                        } else {
                            System.out.println(Material.getMaterial(i1));
                            itemStack = new ItemStack(Material.getMaterial(i1));
                        }
                        if (inventory.getItem(6)!=null){
                            player.getWorld().dropItem(player.getLocation(),inventory.getItem(6));
                        }
                        inventory.setItem(6,itemStack);
                        player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e合成成功");
                        event.setCancelled(true);
                    } else {
                        player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e合成公式不正确");
                        event.setCancelled(true);
                    }
                }
            } else {
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e合成公式不正确");
                event.setCancelled(true);
            }

        }else if(rawSlot == 5 || rawSlot == 7 ){
            event.setCancelled(true);
        } else if(rawSlot<5){

        }
    }

    private static void menuClickedAddMode(Player player, int rawSlot, Inventory inventory, InventoryClickEvent event, ForgeItems plugin) {
        if (rawSlot == 8) {
            HashMap<String, Boolean> map = getSourceMap(inventory, event);
            if (map.size()<3) {
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e请在左侧放入足够的锻造材料");
                event.setCancelled(true);
            }
            if (inventory.getItem(6) == null){
                player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e请在左侧放入产出物品");
                event.setCancelled(true);
            } else {
                ItemStack item = inventory.getItem(6);
                RPGItem rpgItem = RPGItems.toRPGItem(item);
                Integer over = null;
                boolean rpg;
                if (rpgItem != null){
                    over = rpgItem.getID();
                    rpg = true;
                } else {
                    over = item.getType().getId();
                    rpg = false;
                }
                int total = 0;
                for (String s : map.keySet()) {
                    total = total + Integer.parseInt(s.split(",")[0]);
                }
                String[] split = inventory.getItem(8).getItemMeta().getDisplayName().split("：");
                String name;
                if (split.length>1){
                    StringBuilder temp= new StringBuilder();
                    for (int i = 1; i < split.length; i++) {
                        temp.append(split[i]);
                    }
                    name = total + "," + temp;

                    FileConfiguration config = plugin.getConfig();
                    //获取到对应的物品参数列表
                    MemorySection items = (MemorySection) config.get("items");
                    if (items == null){
                        MemoryConfiguration memoryConfiguration = new MemoryConfiguration();
                        memoryConfiguration.set("items",null);
                        items = memoryConfiguration;
                        config.set("items",items);
                    }
                    Set<String> keys = items.getKeys(false);
                    boolean check = true;
                    for (String key : keys) {
                        String[] sp = key.split(",");
                        if (sp.length<2){
                            continue;
                        }
                        if (sp[1].equals(temp.toString())){
                            check = false;
                            break;
                        }
                        if (Integer.parseInt(sp[0]) == total){
                            ConfigurationSection itemsListConf = items.getConfigurationSection(key);
                            ArrayList<String> configCache = checkDouble(itemsListConf,map);
                            if (configCache.size() == 0){
                                check = false;
                                break;
                            }
                        }
                    }
                    if (check){
                        //构建物品map
                        Map<Integer,Boolean> overMap = new HashMap<>();
                        overMap.put(over,rpg);
                        MemoryConfiguration memoryConfiguration = new MemoryConfiguration();
                        memoryConfiguration.set("key",map);
                        memoryConfiguration.set("value",overMap);
                        items.set(name,memoryConfiguration);
                        try {
                            plugin.saveConfig();
                            plugin.reloadConfig();
                        } catch (Exception e){
                            player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e保存插件配置失败，请检查配置文件");
                            event.setCancelled(true);
                            return;
                        }
                        player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e创建合成公式成功");
                        event.setCancelled(true);
                    } else {
                        player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e合成公式已存在");
                        event.setCancelled(true);
                    }
                } else {
                    player.sendMessage(ForgeItemTextLoader.PLUGIN_NAME +" §e请输入一个公式名");
                    event.setCancelled(true);
                }
            }
        }else if(rawSlot ==5 || rawSlot==7){
            event.setCancelled(true);
        }
    }


    private static ArrayList<String> checkDouble(ConfigurationSection itemsListConf,HashMap<String,Boolean> map){
        ConfigurationSection cs = (ConfigurationSection) itemsListConf.get("key");
        Set<String> keys = cs.getKeys(false);
        ArrayList<String> playerCache = new ArrayList<>(map.keySet());
        ListIterator<String> playerIds = playerCache.listIterator();
        ArrayList<String> configCache = new ArrayList<>(keys);
        ListIterator<String> configIds = configCache.listIterator();
        while (playerIds.hasNext()){
            String playerId = playerIds.next();
            configIds = configCache.listIterator();
            System.out.println(playerId);
            while (configIds.hasNext()){
                String configId = configIds.next();
                System.out.println(configId.split(",")[0]);
                System.out.println(playerId.split(",")[0]);
                if (configId.split(",")[0].equals(playerId.split(",")[0])){
                    if (cs.getBoolean(configId) == map.get(playerId)){
                        configIds.remove();
                        break;
                    }
                }
                System.out.println("p:==="+playerCache.size());
                System.out.println("c:==="+configCache.size());
            }
        }
        return configCache;
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
