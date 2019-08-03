package com.github.sunligh91.forge.main.command;

import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import com.github.sunligh91.forge.main.utils.ClearSourceUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import think.rpgitems.api.RPGItems;
import think.rpgitems.item.RPGItem;
import yo.bz;

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
                Inventory inventory;
                ConfigurationSection itemSection = plugin.getConfig().getConfigurationSection("items");
                switch (args[0]){
                    case "open":
                        inventory = Bukkit.createInventory(p1, 9, ForgeItemTextLoader.PLUGIN_GUI_TITLE);
                        makeGlass(inventory);
                        inventory.setItem(8, getItemStack(Material.SIGN, "§a锻造", "§c请将要锻造的材料放入左侧空白区域"));
                        p1.closeInventory();
                        p1.openInventory(inventory);
                        break;
                    case "add":
                        if (args.length>1 && !args[1].equals("")) {
                            if(sender.isOp()) {
                                inventory = Bukkit.createInventory(p1, 9, ForgeItemTextLoader.PLUGIN_GUI_TITLE + "（添加模式）");
                                makeGlass(inventory);
                                inventory.setItem(8, getItemStack(Material.SIGN, "§a添加锻造记录：" + args[1], "§c请依次放入锻造材料，锻造产出物品，最后点击我"));
                                p1.closeInventory();
                                p1.openInventory(inventory);
                            } else {
                                sender.sendMessage("你没有权限使用此命令！");
                            }
                        }
                        break;
                    case "list":
                        if (args.length==2){
                            try {
                                int i = Integer.parseInt(args[1]);
                                int pageSize = 10;
                                if (i>0){
                                    Set<String> strings = itemSection.getKeys(false);
                                    ArrayList<String> items = new ArrayList<>(strings);
                                    items.sort(String::compareTo);
                                    int max = items.size();
                                    int min = 0;
                                    if (i*pageSize<items.size()){
                                        max = i*pageSize;
                                    }
                                    if (i*pageSize>items.size()){
                                        if ((i-1)*pageSize>items.size()){
                                            sender.sendMessage("当前最大页数：" + items.size()/pageSize);
                                            break;
                                        } else {
                                            min = (i-1)*pageSize;
                                        }
                                    } else {
                                        if ((i-1)*pageSize > 0) {
                                            min = (i - 1) * pageSize;
                                        }
                                    }
                                    StringBuilder sb = new StringBuilder();
                                    for (; min < max; min++) {
                                        sb.append(items.get(min).split(",")[1]).append("\n");
                                    }
                                    sender.sendMessage(sb.toString());
                                } else {
                                    throw new RuntimeException();
                                }
                            } catch (Exception e){
                                sender.sendMessage("请输入合法的页数，例如dj list 1");
                            }
                        } else {
                            sender.sendMessage("请输入合法的页数，例如dj list 1");
                        }
                        break;
                    case "show":
                        Set<String> items = itemSection.getKeys(false);
                        if (args.length==2) {
                            StringBuilder sb = new StringBuilder(args[1]).append("：");
                            for (String item : items) {
                                if(item.split(",")[1].equals(args[1])) {
                                    ConfigurationSection keySection = itemSection.getConfigurationSection(item).getConfigurationSection("key");
                                    Map<String, Object> values = keySection.getValues(false);
                                    for (Map.Entry<String, Object> value : values.entrySet()) {
                                        Integer s = Integer.parseInt(value.getKey().split(",")[0]);
                                        if ((boolean)value.getValue()){
                                            RPGItem rpgItemById = RPGItems.getRPGItemById(s);
                                            sb.append(rpgItemById.getName());
                                        } else {
                                            sb.append(Material.getMaterial(s));
                                        }
                                        sb.append(" ");
                                    }
                                }
                            }
                            sender.sendMessage(sb.toString());
                        } else {
                            sender.sendMessage("请输入正确的格式，例如dj show 老司机");
                        }
                         break;
                    default:
                }
            } else {
                sender.sendMessage("dj: //\n" +
                        "  open //打开锻造窗口\n" +
                        "  add 图纸名 //添加一个锻造图纸（仅op可用）\n" +
                        "  list 页数//查看全部图纸\n" +
                        "  show 图纸名//查看图纸具体信息");

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
