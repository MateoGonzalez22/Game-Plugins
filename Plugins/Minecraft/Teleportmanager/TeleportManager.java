package com.plugin.teleportmanager;

import com.plugin.teleportmanager.commands.Command;
import com.plugin.teleportmanager.files.TeleportConfiFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class TeleportManager extends JavaPlugin {
    private static TeleportManager plugin;

    @Override
    public void onEnable() {
        hi();

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        TeleportConfiFile.setup();

        getCommand("t").setExecutor(new Command());

    }

    public static void hi(){

        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CoordinatesManager]" + ChatColor.YELLOW + " Loaded Succesfuly");
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CoordinatesManager]" + ChatColor.YELLOW + " Thanks for using my plugin!" + ChatColor.WHITE + " by tute");

    }
}