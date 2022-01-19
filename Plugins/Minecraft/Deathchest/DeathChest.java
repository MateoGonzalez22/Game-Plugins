
package com.plugin.deathchest;


import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;


import java.util.List;
import java.util.stream.Collectors;

public final class DeathChest extends JavaPlugin implements Listener {
    DeathChest plugin;
    TextComponent textComponent;


    @Override
    public void onEnable() {
        getCommand("dt").setExecutor(new Command());
        getServer().getPluginManager().registerEvents(this, this);


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) throws InterruptedException {

        Player player = event.getEntity().getPlayer();

        Location deathLoc = player.getLocation();

        int x = (int) Math.round(deathLoc.getX());
        int y = (int) Math.round(deathLoc.getY()) + 1;
        int z = (int) Math.round(deathLoc.getZ());

        player.sendMessage("Prueba 3");
        player.sendMessage(ChatColor.BLUE + "You died in " + x + ", " + y + ", " + z);
        player.sendMessage(ChatColor.WHITE + "You can tp with /t " + x + " " + y + " " + z);

        if (player.getInventory().isEmpty()) {
            return;
        }

        ItemStack[] playerItems = player.getInventory().getContents();

        int nulls = 0;
        int countItem = 0;

        for (ItemStack item : playerItems) {

            if (item != null) {
                countItem += 1;
            } else {
                nulls += 1;
            }
        }

        Block block1 = deathLoc.getBlock();
        Block block2 = deathLoc.clone().add(1, 0, 0).getBlock();

        Material block2Material = block2.getType();


        block1.setType(Material.CHEST);
        block2.setType(Material.CHEST);


        Chest chest1 = (Chest) block1.getState();
        Chest chest2 = (Chest) block2.getState();

        NamespacedKey key = new NamespacedKey(this, "message");

        PersistentDataContainer container1 = chest1.getPersistentDataContainer();
        container1.set(key, PersistentDataType.STRING, "DeathChest");

        PersistentDataContainer container2 = chest2.getPersistentDataContainer();
        container2.set(key, PersistentDataType.STRING, "DeathChest");

        chest1.update();
        chest2.update();

        Inventory chest1Inv = chest1.getInventory();
        Inventory chest2Inv = chest2.getInventory();

        int i = 0;
        if (countItem > 27) {

            for (ItemStack item : playerItems) {

                if (item != null) {
                    i += 1;

                    if (i <= 27) {
                        chest1Inv.addItem(item);
                    } else {
                        chest2Inv.addItem(item);
                    }
                }
            }
        } else {

            for (ItemStack item : playerItems) {

                if (item != null) {

                    chest1Inv.addItem(item);
                    block2.setType(block2Material);

                }
            }
        }

        event.getDrops().clear();


        int cant = countItem;
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                block1.setType(Material.AIR);
                block2.setType(Material.AIR);
            }
        }, 50 * 20);
    }


    NamespacedKey key = new NamespacedKey(this, "message");

    @EventHandler

    public void onChestBreak(BlockBreakEvent event) {

        Material material = event.getBlock().getType();

        if (event.getBlock().getType() != Material.CHEST) {
            return;
        }
        if (!(event.getBlock().getState() instanceof TileState)) {
            return;
        }

        TileState state = (TileState) event.getBlock().getState();
        PersistentDataContainer container = state.getPersistentDataContainer();

        if (!container.has(key, PersistentDataType.STRING)) {
            return;
        }
        if (!container.get(key, PersistentDataType.STRING).equals("DeathChest")) {
            return;
        }

        event.setCancelled(true);

        String name = container.get(key, PersistentDataType.STRING);
        Player player = event.getPlayer();
        player.sendMessage("Message: " + name);

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        for (Block block : event.blockList()) {
            if ((block.getState() instanceof Chest)){
                Chest chest = (Chest) block.getState();
                PersistentDataContainer container = chest.getPersistentDataContainer();
                if (container.has(key, PersistentDataType.STRING)){
                    if (container.get(key, PersistentDataType.STRING).equals("DeathChest")){
                        event.blockList().remove(block);
                    }
                }
            }
        }
    }
}



/*


    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        for (Block block : event.blockList()) {
            if (!(block.getState() instanceof Chest)) continue;
            Chest chest = (Chest) block.getState();
            PersistentDataContainer container = chest.getPersistentDataContainer();
            if (!container.has(key, PersistentDataType.STRING)) continue;
            if (!container.get(key, PersistentDataType.STRING).equals("DeathChest")) continue;
            event.blockList().remove(block);
        }
    }
}



    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {


        List<Block> list = event.blockList();

        for (Block block : list) {

            if (block.getType() == Material.CHEST){

                Chest chest = (Chest) block.getState();
                PersistentDataContainer container = chest.getPersistentDataContainer();

                NamespacedKey key = new NamespacedKey(this, "message");

                if(!container.has(key, PersistentDataType.STRING)){
                    return;
                }
                if(container.get(key,PersistentDataType.STRING).equals("DeathChest")){
                    event.blockList().removeAll(event.blockList().stream().filter(b -> b.getType() == Material.CHEST).collect(Collectors.toList()));
                }
            }
        }
    }
}

 */

