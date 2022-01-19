package com.plugin.deathchest;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class Command implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {

        if(sender instanceof Player){
            Player player = (Player) sender;

            if(args.length == 1){
                if (Objects.equals(args[0], "info")){

                    player.sendMessage(ChatColor.BLACK + "This is the Death Chest Plugin");

                }
            }
        }

        return false;
    }
}
