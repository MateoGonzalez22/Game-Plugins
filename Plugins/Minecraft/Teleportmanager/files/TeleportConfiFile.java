package com.plugin.teleportmanager.files;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class TeleportConfiFile {

    private static File file;
    private static FileConfiguration customFile;


    public static void setup(){

        file = new File(Bukkit.getServer().getPluginManager().getPlugin("TeleportManager").getDataFolder(), "config.yml");

        if(!file.exists()){
            try{
                file.createNewFile();
            }catch(IOException e){
                System.out.println("ERROR: No se pudo crear el archivo yml");
            }
        }
        customFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return customFile;
    }

    public static void set(String name, String place){
        customFile.set(name, place);
    }

    public static void save(){
        try{
            TeleportConfiFile.get().save(file);
        }catch(IOException e){
            System.out.println("ERROR: No se pudo guardar el archivo");
        }
    }

    public static void reload(){
        customFile = YamlConfiguration.loadConfiguration(file);
    }

}

