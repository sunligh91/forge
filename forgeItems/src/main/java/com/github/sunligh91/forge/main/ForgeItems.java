package com.github.sunligh91.forge.main;

import com.github.sunligh91.forge.listener.CheckListener;
import com.github.sunligh91.forge.main.command.ForgeItemCommandExecutor;
import com.github.sunligh91.forge.main.command.ForgeItemReloadCommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.hamcrest.internal.ArrayIterator;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * @author ：chr
 * @description：
 * @date ：Created in 2019/7/24 10:28
 * @version: $
 */
public class ForgeItems extends JavaPlugin {
    private static Logger log = Logger.getLogger("Minecraft");


    @Override
    public void onEnable() {
        System.out.println("ForgeItems插件版本：1.0");
        ForgeItemTextLoader forgeItemTextLoader = new ForgeItemTextLoader();
        forgeItemTextLoader.init(this);
        String currentPath = getConfig().getCurrentPath();
        if (ClassLoader.getSystemClassLoader().getResourceAsStream(currentPath)== null){
            saveDefaultConfig();
        }
        Server server = getServer();
        CheckListener checkListener=new CheckListener(this);
        server.getPluginManager().registerEvents(checkListener,this);
        this.getCommand("dj").setExecutor(new ForgeItemCommandExecutor(this));
        this.getCommand("djr").setExecutor(new ForgeItemReloadCommandExecutor(this));
        System.out.println("ForgeItems加载成功");
    }

    @Override
    public void onDisable() {

    }

}
