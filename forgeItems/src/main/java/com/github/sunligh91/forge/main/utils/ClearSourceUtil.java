package com.github.sunligh91.forge.main.utils;

import org.bukkit.inventory.Inventory;

/**
 * @author ：chr
 * @description：清除材料框
 * @date ：Created in 2019/7/30 22:53
 * @version: $
 */
public class ClearSourceUtil {
    public static void clear(Inventory inventory){
        inventory.setItem(0, null);
        inventory.setItem(1, null);
        inventory.setItem(2, null);
        inventory.setItem(3, null);
        inventory.setItem(4, null);
    }
}
