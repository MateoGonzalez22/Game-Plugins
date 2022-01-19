package com.plugin.teleportmanager.commands;

import com.plugin.teleportmanager.files.TeleportConfiFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;

public class Command implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {


        if (sender instanceof Player) {

            Player player = (Player) sender;

            // args.length == 0

            if (args.length == 0) {
                modoDeUso(player);
            }

            // args.length == 1

            else if (args.length == 1) {
                String place = args[0];


                if (Objects.equals(args[0], "help")) {
                    modoDeUso(player);
                }

                else if (isPlayer(player, args[0])){
                    Player objetivo = Bukkit.getPlayer(args[0]);
                    player.teleport(objetivo.getLocation());
                    player.sendMessage("");
                    objetivo.sendMessage("");
                    objetivo.sendMessage(ChatColor.GREEN + player.getName() + " hizo tp hacia vos");
                    player.sendMessage(ChatColor.GREEN + "Teletransportado a " + objetivo.getName());
                }

                else if(Objects.equals(args[0], "list")){
                    listPlaces(player);

                }


                else if(player.isOp() && Objects.equals(args[0], "reload")){
                    TeleportConfiFile.reload();
                    player.sendMessage(ChatColor.BLUE + "Plugin reloaded");
                }


                else if (placeExists(player, place)){

                    Location location = returnLocationOfPlace(player, place);
                    player.teleport(location);
                    player.sendMessage(" ");
                    player.sendMessage(ChatColor.GREEN + "Teletransportado a " + place);

                }

                else if(args[0].toLowerCase(Locale.ROOT).equals("deleteall")){
                    deleteAllPlaces(player);
                }

                else{
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "Formato Incorrecto!");
                    player.sendMessage(ChatColor.WHITE + args[0] + " " + ChatColor.RED + "no es un jugador ni un lugar guardado!");
                    player.sendMessage(ChatColor.GREEN + "/t o /t help (ver modo de uso)");
                }
            }

            // args.length == 2

            else if (args.length == 2) {

                if (Objects.equals(args[0], "set")) {

                    String name = player.getName();
                    String place = args[1];

                    TeleportConfiFile.get().set(name, place);

                    TeleportConfiFile.save();

                }
                else if(Objects.equals(args[0], "delete")){
                    if(placeExists(player, args[1])) {
                        deletePlace(player, args[1]);
                        player.sendMessage("");
                        player.sendMessage(ChatColor.GREEN + "Lugar borrado!");
                    }
                    else{
                        player.sendMessage("");
                        player.sendMessage(ChatColor.RED + "El lugar que ingreso no existe, crealo con /t set");
                    }
                }
            }


            // args.length == 3

            else if (args.length == 3) {
                World mundo = player.getWorld();
                Location coordenadas = null;
                try {
                    double x = Double.parseDouble(args[0]);
                    double y = Double.parseDouble(args[1]);
                    double z = Double.parseDouble(args[2]);
                    coordenadas = new Location(mundo, x, y, z);
                    if (!isBlockAt(player, coordenadas)) {
                        player.teleport(coordenadas);
                        sender.sendMessage("Teletransportado a " + x + " " + y + " " + z);
                    }

                } catch (NumberFormatException e) {

                    player.sendMessage(ChatColor.RED + "Formato incorrecto! Debe ingresar numeros sin coma");
                }
            }

            // args.length == 4

            else if (args.length == 4) {
                World world = null;

                if (Objects.equals(args[0], "world")) {
                    world = Bukkit.getWorlds().get(0);
                } else if (Objects.equals(args[0], "nether")) {
                    world = Bukkit.getWorlds().get(1);
                } else if (Objects.equals(args[0], "end")) {
                    world = Bukkit.getWorlds().get(2);
                }

                if (world == null) {
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "ERROR: Mundo ingresado incorrectamente");
                    player.sendMessage(ChatColor.GREEN + "Mundos: world, nether, end");
                    player.sendMessage(ChatColor.YELLOW + "/t help (para ver el modo de uso)");

                } else {
                    try {
                        double x = Double.parseDouble(args[1]);
                        double y = Double.parseDouble(args[2]);
                        double z = Double.parseDouble(args[3]);
                        Location coordenadas = new Location(world, x, y, z);
                        if (!isBlockAt(player, coordenadas)) {
                            player.teleport(coordenadas);
                            player.sendMessage("Teletransportado a " + world.getName() + " " + x + " " + y + " " + z);
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Formato incorrecto! Debe ingresar numeros sin coma");
                        player.sendMessage(ChatColor.GREEN + "/t help (para ver el modo de uso)");
                    }
                }
            }


            // args.length == 6

            else if (args.length == 6) {
                if (Objects.equals(args[0], "set")) {

                    setPlace(player, args[1], args[2], args[3], args[4], args[5]);
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + "Set guardado!");
                    player.sendMessage(ChatColor.YELLOW + "Usalo con /t " + args[1]);

                }
                else if(Objects.equals(args[0], "change")){
                    modifyPlace(player, args[1], args[2], args[3], args[4], args[5]);
                    player.sendMessage("");
                    player.sendMessage(ChatColor.GREEN + args[1] +" guardado!");
                    player.sendMessage(ChatColor.YELLOW + "Usalo con /t " + args[1]);

                }
            }

            else{
                modoDeUso(player);
            }
        }
        return false;
    }


    public void modoDeUso (Player player){
        player.sendMessage("");
        player.sendMessage(ChatColor.BLUE +"Plugin by tute");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Modo de uso:\n");
        player.sendMessage("");
        player.sendMessage(ChatColor.GREEN + "NO usar: '<>' ',' '()'");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "<x y z> " + ChatColor.WHITE + "Para tepearte en el mundo en el que estas");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "<world> <x y z> " + ChatColor.WHITE + "world:[world, nether ó end] Para tepearte en otro mundo");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "<Nombre Otro Jugador> " + ChatColor.WHITE + "Para tepearte a un jugador");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "set <lugar> <world> <x y z> " + ChatColor.WHITE + "Para guardar las coordenadas con un nombre");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "change <lugar> <world> <x y z> " + ChatColor.WHITE + "Para cambiar las coordenadas de un nombre");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "delete <lugar> " + ChatColor.WHITE + "Para borrar un lugar");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "deleteAll " + ChatColor.WHITE + "Para borrar todos los lugares guardados");
        player.sendMessage(ChatColor.GREEN + "/t " + ChatColor.YELLOW + "<lugar> " + ChatColor.WHITE + "Para tepearte a ese lugar");
    }

    public boolean isBlockAt (Player player, Location location){
        String block = location.getBlock().getType().toString();
        if (!Objects.equals(block, "AIR") && !Objects.equals(block, "WATER")) {

            player.sendMessage(ChatColor.RED + "Intentaste tepearte dentro de un bloque!");
            player.sendMessage(ChatColor.YELLOW + "Cambia las coordenadas");
            return true;
        }
        return false;
    }

    public boolean placeExists (Player player, String place){

        if (TeleportConfiFile.get().contains(player.getName() + "." + place)) {
            return true;
        }
        return false;
    }

    public void setPlace(Player player, String place, String world, String x, String y, String z){

        if(coordsIsNumber(player, x, y, z)){

            if (!Objects.equals(world, "world") && !Objects.equals(world, "nether") && !Objects.equals(world, "end")) {
                player.sendMessage("");
                player.sendMessage(ChatColor.RED + "ERROR: Mundo ingresado incorrectamente");
                player.sendMessage(ChatColor.GREEN + "Mundos: world, nether, end");
                player.sendMessage(ChatColor.YELLOW + "/t help (para ver el modo de uso)");
            }

            else{
                if (!placeExists(player, place)){

                    ArrayList<String> coords = new ArrayList<>();
                    coords.add(world);
                    coords.add(x);
                    coords.add(y);
                    coords.add(z);

                    TeleportConfiFile.get().set(player.getName() + "." + place, coords);
                    TeleportConfiFile.save();
                }
                else{
                    player.sendMessage("");
                    player.sendMessage(ChatColor.RED + "Ese nombre ya existe, ingresa uno diferente o modificalo");
                }
            }
        }
    }

    public void listPlaces(Player player){
        ArrayList<String> arrayList = new ArrayList<>();
        String name = player.getName() + ".";


        HashSet<String> hashSet = new HashSet<>();

        try{
            hashSet = (HashSet<String>) TeleportConfiFile.get().getConfigurationSection(name).getKeys(true);
        }catch(NullPointerException e){
            //
        }
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "Lista de lugares: ");
        player.sendMessage("");

        if (hashSet.size() == 0){
            player.sendMessage(ChatColor.GREEN + "Todavia no tienes ningun lugar guardado");
        }

        for (String place : hashSet) {
            Location location = returnLocationOfPlace(player, place);
            String world = location.getWorld().getName();

            if (Objects.equals(world, "world")) {
                world = "world";
            } else if (Objects.equals(world, "nether")) {
                world = "nether";
            } else if (Objects.equals(world, "end")) {
                world = "end";
            }

            String x = String.valueOf(location.getX());
            String y = String.valueOf(location.getY());
            String z = String.valueOf(location.getZ());


            player.sendMessage(ChatColor.GREEN + place + ": " + ChatColor.YELLOW + world + " " + x + ", " + y + ", " + z);


        }


    }

    public boolean modifyPlace(Player player, String place, String wrld, String x, String y, String z){

        if (placeExists(player, place)){
            deletePlace(player, place);
            setPlace(player, place, wrld, x, y, z);
        }
        else{
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "El lugar que ingreso no existe");
            player.sendMessage(ChatColor.GREEN + "Agregalo con /t set <nombre> world x y z");
            return false;
        }
        return true;
    }

    public void deletePlace(Player player, String place){
        String name = player.getName() + ".";
        TeleportConfiFile.get().set(name + place, null);
        TeleportConfiFile.save();
    }

    public void deleteAllPlaces(Player player){
        HashSet<String>hashSet = new HashSet<>();
        String name = player.getName() + ".";

        try{
            hashSet = (HashSet<String>) TeleportConfiFile.get().getConfigurationSection(name).getKeys(true);
        }catch(NullPointerException e){
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "Tu nombre todavia no esta registrado");
            player.sendMessage(ChatColor.GREEN + "/t set " + ChatColor.WHITE + "Para guardar un nuevo set");
        }

        if (hashSet.size() == 0){
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "Todavia no tienes ningun lugar guardado");
        }
        else{
            player.sendMessage(ChatColor.GREEN + "Lugares borrados!");
            for (String place : hashSet) {
                TeleportConfiFile.get().set(name + place, null);
                TeleportConfiFile.save();
            }
        }
    }


    public boolean coordsIsNumber(Player player, String args1, String args2, String args3) {
        try {
            double x = Double.parseDouble(args1);
            double y = Double.parseDouble(args2);
            double z = Double.parseDouble(args3);
        } catch (NumberFormatException e) {
            player.sendMessage("");
            player.sendMessage(ChatColor.RED + "Formato incorrecto! Debe ingresar numeros sin coma");
            player.sendMessage(ChatColor.GREEN + "/t help (para ver el modo de uso)");
            return false;
        }
        return true;
    }

    public boolean isPlayer(Player player, String name){
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    public Location returnLocationOfPlace(Player player, String place){
        ArrayList<String>arrayList = new ArrayList<>();
        Object obj = new Object();
        obj = TeleportConfiFile.get().get(player.getName() + "." + place);
        arrayList = (ArrayList<String>) obj;

        String worldName = arrayList.get(0);
        Double x = Double.parseDouble(arrayList.get(1));
        Double y = Double.parseDouble(arrayList.get(2));
        Double z = Double.parseDouble(arrayList.get(3));

        World world = returnWorld(worldName);
        Location location = new Location(world, x, y, z);

        return location;
    }

    public World returnWorld(String name){
        World world = null;
        if (Objects.equals(name, "world")) {
            world = Bukkit.getWorlds().get(0);
        } else if (Objects.equals(name, "nether")) {
            world = Bukkit.getWorlds().get(1);
        } else if (Objects.equals(name, "end")) {
            world = Bukkit.getWorlds().get(2);
        }
        else{
            Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.RED + "ERROR: world is null");
        }
        return world;
    }
}







