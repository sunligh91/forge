package com.github.sunligh91.forge.main.command;

import com.github.sunligh91.forge.main.ForgeItems;
import com.github.sunligh91.forge.main.ForgeItemTextLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ForgeItemReloadCommandExecutor implements CommandExecutor {
    private ForgeItems plugin;

    public ForgeItemReloadCommandExecutor(ForgeItems plugin) {
        this.plugin=plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        plugin.reloadConfig();
        ForgeItemTextLoader sun91CheckTextLoader = new ForgeItemTextLoader();
        sun91CheckTextLoader.init(plugin);
        sender.sendMessage("插件配置重载成功");
        return true;
    }
}
