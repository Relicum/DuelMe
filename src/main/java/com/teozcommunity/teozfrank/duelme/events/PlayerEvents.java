package com.teozcommunity.teozfrank.duelme.events;

/**
 The MIT License (MIT)

 Copyright (c) 2014 teozfrank

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import com.google.common.reflect.TypeToken;
import com.relicum.dual.Configuration.PlayerProperties;
import com.relicum.dual.GameInventory;
import com.relicum.ipsum.Items.Inventory.BaseInventory;
import com.relicum.ipsum.io.GsonIO;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.mysql.FieldName;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerEvents implements Listener {

    private DuelMe plugin;
    private static HashMap<Player, Vector> locations = new HashMap<Player, Vector>();
    private List<String> allowedCommands;
    // private Type inT = new TypeToken<GameInventory>() {
    // }.getType();
    public Type bit = new TypeToken<BaseInventory>() {
    }.getType();
    public Path path;
    private GameInventory pl;
    private String storagePath;
    private Type plPropType = new TypeToken<PlayerProperties>() {
    }.getType();
    private Type top = new TypeToken<Inventory>() {
    }.getType();

    public PlayerEvents(DuelMe plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.allowedCommands = new ArrayList<String>();
        this.setupAllowedCommands();
        this.path = plugin.getPath("BaseInventory.json");
        this.storagePath = plugin.getDataFolder().toString() + File.separator + "players" + File.separator;

    }

    public PlayerEvents() {
    }

    private void setupAllowedCommands() {
        this.allowedCommands.add("/duel leave");
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        if (player.hasPermission("duelme.admin.update.notify")) {
            if (UpdateChecker.isUpdateAvailable() && plugin.getConfig().getBoolean("duelme.checkforupdates")) {
                Util.sendMsg(player, ChatColor.GREEN + "There is an update available for" +
                        " for this plugin get it on bukkit dev page: " +
                        ChatColor.AQUA + "http://dev.bukkit.org/bukkit-plugins/duelme/");
            }
        }

        player.setAllowFlight(true);
        PlayerProperties playerProperties = new PlayerProperties();
        //playerProperties.saveSettings(player);
        //playerProperties.saveInventory(player.getInventory());

        try {
            //GsonIO.writeToFile(Paths.get(storagePath,player.getUniqueId().toString() + ".json"),playerProperties,plPropType);
            playerProperties = GsonIO.readFromFile(Paths.get(storagePath, player.getUniqueId().toString() + ".json"), plPropType, playerProperties);
            playerProperties.applySettings(player);
            playerProperties.applyInventory(player);
            plugin.getLogger().info("Successfully loaded player settings and inventory");

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //BarAPI.setMessage(player,ChatColor.GREEN + "Hello Relicum" + ChatColor.GOLD + " 1-V-1 coming soon",10);
      /*  new BukkitRunnable() {

            int count = 1;

            @Override
            public void run() {



                   if(BarAPI.hasWitherBar(player)){
                       player.sendMessage("Got Bar");

                   } else
                       player.sendMessage(ChatColor.RED+"No Bar");
               if(count==7){
                   BarAPI.setMessage(player,ChatColor.GREEN + "Hello Relicum" + ChatColor.GOLD + " 1-V-1 coming soon",2);
               }

               if (count==1){
                   BarAPI.setMessage(player,ChatColor.GREEN + "Hello Relicum" + ChatColor.GOLD + " 1-V-1 coming soon",5);
               }

               if(count==12)
                   cancel();


               count++;
            }
        }.runTaskTimer(plugin,80l,20l);*/


    }

    @EventHandler
    public void onLog(PlayerSpawnLocationEvent e) {
        e.getPlayer().sendMessage(ChatColor.GREEN + "Being moved to spawn by event");
        e.setSpawnLocation(e.getPlayer().getWorld().getSpawnLocation());
    }


    public void onPlayerRightClickToDuel(PlayerInteractEntityEvent e) {

        e.getPlayer().sendMessage(ChatColor.AQUA + "You have clicked a " + e.getRightClicked().getType().toString());

        Player player = e.getPlayer();
        Entity entity = e.getRightClicked();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if (!fm.isRightClickToDuelEnabled()) {
            return;
        }

        if (entity instanceof Player) {
            Player target = (Player) entity;
            if (player.isSneaking() && player.getItemInHand().equals(new ItemStack(Material.DIAMOND_SWORD))) {//if the player is sneaking and has a diamond sword
                dm.sendNormalDuelRequest(player, target.getName());//send a duel request
                return;
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerBreakBlock(BlockBreakEvent e) {
        Player dueler = e.getPlayer();
        DuelManager dm = plugin.getDuelManager();

        if (dm.isInDuel(dueler.getUniqueId())) {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDamageByFall(EntityDamageEvent e) {
        Entity entity = e.getEntity();

        if (!(entity instanceof Player)) {
            return;
        }

        if (e.getCause() != EntityDamageEvent.DamageCause.FALL) {
            return;
        }

        DuelManager dm = plugin.getDuelManager();
        Player player = (Player) entity;
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        if (dm.isInDuel(playerUUID)) {//if the player is in a duel
            DuelArena playersArena = dm.getPlayersArenaByUUID(playerUUID);
            if (playersArena.getDuelState() == DuelState.STARTING) {//if the duel state is starting
                e.setCancelled(true); //cancel the event
            }
        }


    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();
        MySql mySql = plugin.getMySql();

        if (dm.isInDuel(playerUUID)) {
            dm.addDeadPlayer(playerUUID);

            if (fm.isMySqlEnabled()) {
                mySql.addPlayerKillDeath(playerUUID, playerName, FieldName.DEATH);
            }

            if (e.getEntity().getKiller() instanceof Player) {
                Player killer = e.getEntity().getKiller();
                String killerName = killer.getName();
                if (fm.isMySqlEnabled()) {
                    mySql.addPlayerKillDeath(playerUUID, killerName, FieldName.KILL);
                }

                if (!fm.isDropItemsOnDeathEnabled()) {
                    if (plugin.isDebugEnabled()) {
                        SendConsoleMessage.debug("Item drops disabled, clearing.");
                    }
                    e.getDrops().clear();
                }

                if (!fm.isDeathMessagesEnabled()) {
                    e.setDeathMessage("");
                    return;
                }
                e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel by "
                        + ChatColor.AQUA + killer.getName());
            } else {
                if (!fm.isDeathMessagesEnabled()) {
                    e.setDeathMessage("");
                    return;
                }
                e.setDeathMessage(ChatColor.GOLD + "[DuelMe] " + ChatColor.AQUA + player.getName() + ChatColor.RED + " was killed in a duel!");
            }
            dm.endDuel(player);

        }

    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e.getView().getTitle().length() != 0) {
            Player player = ((Player) e.getWhoClicked());

            if (e.getCurrentItem().getType() != null) {
                player.sendMessage(ChatColor.AQUA + "Container type is " + e.getInventory().getType().name() + " with a Title of " + e.getInventory().getTitle());
            }

            if (e.getRawSlot() < 9) {
                player.playSound(player.getLocation(), Sound.CAT_HISS, 20.0f, 20.0f);
                player.sendMessage(ChatColor.AQUA + "Thats one of our slots");
                e.setCancelled(true);
            }
            player.sendMessage(ChatColor.AQUA + "Slot clicked is " + e.getSlot() + " raw slot is " + e.getRawSlot() + " type is " + e.getSlotType().name());
        }

    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {

        if (e.getView().getTitle().length() != 0) {

            ((Player) e.getPlayer()).sendMessage(ChatColor.AQUA + "Title of closing inv is " + e.getView().getTitle());
        }
    }


    @EventHandler
    public void onEE(PlayerInteractEntityEvent e) {

        if (e.getRightClicked().getType().toString().equalsIgnoreCase("VILLAGER")) {
            e.getPlayer().sendMessage(ChatColor.AQUA + "You have clicked a " + e.getRightClicked().getType().toString());
            Entity entity = e.getRightClicked();
            if (entity instanceof Villager) {
                Villager cev = (Villager) entity;

                //cev.getBukkitEntity().getUniqueId();
                String name = ChatColor.stripColor(cev.getCustomName());

                if (name.equalsIgnoreCase("Custom Trader")) {
                    Inventory inv = null;
                    inv = getInventory();
                    e.getPlayer().openInventory(inv);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.LEVEL_UP, 5.0f, 1.0f);

                    final Inventory finalInv = inv;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            finalInv.getViewers().stream().forEach(p -> ((Player) p).sendMessage(p.getName() + " is still viewing"));
                            e.getPlayer().getOpenInventory().close();
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.CHEST_CLOSE, 5.0f, 1.0f);
                        }
                    }.runTaskLater(DuelMe.getInstance(), 200l);

                }
            }
        }
    }

    public Inventory getInventory() {
/*       Inventory topbar= Bukkit.createInventory(null, InventoryType.CHEST, ChatColor.DARK_AQUA + "<- " + ChatColor.YELLOW + ChatColor.ITALIC + "Lobby Shop" + ChatColor.DARK_AQUA + " ->");

        ItemStack slot0 = Ipsum.getInstance().getSimpleItemFactory().getItemBuilder(Material.COMPASS,1, MetaType.ITEM_META)
                .setDisplayName(ChatColor.GREEN + "PVP Taxi")
                .setItemLores(Arrays.asList(ChatColor.AQUA+"Simple Way To",ChatColor.AQUA+"Travel between Servers"," ",ChatColor.DARK_PURPLE+"Right Click to Open"))
                .build();
        ItemStack slot3 = Ipsum.getInstance().getSimpleItemFactory().getItemBuilder(Material.CHEST,1,MetaType.ITEM_META)
                .setDisplayName(ChatColor.GREEN+"Hub Gadgets")
                .build();
        ItemStack slot8=Ipsum.getInstance().getSimpleItemFactory().getItemBuilder(Material.EMERALD,1,MetaType.ITEM_META)
                .setDisplayName(ChatColor.GREEN+"Token Shop")
                .setLore(ChatColor.AQUA+"Right click to open shop")
                .build();

        topbar.setItem(0,slot0);
        topbar.setItem(3,slot3);
        topbar.setItem(8,slot8);*/
        Inventory topbar = Bukkit.createInventory(null, 9, ChatColor.DARK_AQUA + "<- " + ChatColor.YELLOW + ChatColor.ITALIC + "Lobby Shop" + ChatColor.DARK_AQUA + " ->");
        try {
            topbar = GsonIO.readFromFile(Paths.get(plugin.getDataFolder().toString() + File.separator, "topBar.json"), top, topbar);
            // GsonIO.writeToFile(Paths.get(plugin.getDataFolder().toString() + File.separator, "topBar.json"), topbar, top);
            DuelMe.getInstance().getMsg().logFormatted(Level.INFO, "Topbar Inventory Successfully loaded");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // System.out.println(Arrays.toString(topbar.getContents()));

        return topbar;

    }


    public void onS(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {
            Type bity = new TypeToken<BaseInventory>() {
            }.getType();
            Player player = e.getPlayer();
            BaseInventory bi = new BaseInventory();





/*            bi.setArmor(armor);
            // bi.setContents(e.getPlayer().getInventory().getContents());

            Inventory inventory = e.getPlayer().getInventory();

            inv = Bukkit.createInventory(null, 36, "playerName");


            inv.setContents(inventory.getContents());
            inv.setItem(2, new ItemStack(Material.DIAMOND, 1));
            int count = 0;
            ItemStack[] stacks = new ItemStack[36];
            for (ItemStack stack : inv) {
                String m = "Slot: " + count + " ";
                if (stack != null) {
                    stacks[count]=new ItemStack(stack);
                    m += " has matrial value of " + stack.getType().name();
                } else {
                    m += "is null";
                }
                System.out.println(m);
                count++;
            }
            bi.setContents(stacks);*/


            try {
                bi = GsonIO.readFromFile(Paths.get(plugin.getDataFolder().toString() + File.separator, "inv2.json"), bity, bi);
                //GsonIO.writeToFile(Paths.get(plugin.getDataFolder().toString() + File.separator, "inv2.json"), bi, bity);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println(bi.toString());
            player.getInventory().setArmorContents(bi.getArmor());
            player.getInventory().setContents(bi.getContents());

        }
        //  GsonIO.readFromFile(Paths.get(getDataFolder().toString() + File.separator, "lobby.json"),lType,lobby2);

    }


/*    public void OnSneak(PlayerToggleSneakEvent e){
         if(e.isSneaking()) {
             Player player = e.getPlayer();
              pl.setContents(player.getInventory(), GameInventory.INV_TYPE.LOBBY);
             pl.setArmor(player.getInventory().getArmorContents(), GameInventory.INV_TYPE.LOBBY);
  *//*           try {
                 GsonIO.writeToFile(path, pl, inT);
             } catch (IOException e1) {
                 e1.printStackTrace();
             }*//*


             player.getInventory().clear();
             player.getInventory().setArmorContents(new ItemStack[4]);
             player.getInventory().setArmorContents(pl.getArmor(GameInventory.INV_TYPE.PLAYER));
             player.getInventory().setContents(pl.getInvContents(GameInventory.INV_TYPE.PLAYER).getContents());

             plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
                 @Override
                 public void run() {
                     player.updateInventory();
                 }
             },1l);
         }
    }

    public void OnFly(PlayerToggleFlightEvent e){
        if(e.isFlying()) {
            e.setCancelled(true);

            Player player = e.getPlayer();
            pl.setInventory(player.getInventory());

*//*            try {
                GsonIO.writeToFile(path, pl, inT);
            } catch (IOException e1) {
                e1.printStackTrace();
            }*//*


            player.getInventory().clear();
            player.getInventory().setArmorContents(new ItemStack[4]);
            player.getInventory().setArmorContents(pl.getArmor(GameInventory.INV_TYPE.LOBBY));
            player.getInventory().setContents(pl.getInvContents(GameInventory.INV_TYPE.LOBBY).getContents());

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,new Runnable() {
                @Override
                public void run() {
                    player.updateInventory();
                }
            },1l);
        }
    }*/


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        FileManager fm = plugin.getFileManager();

        if (dm.isDeadPlayer(playerUUID)) {
            PlayerData playerData = dm.getPlayerDataByUUID(playerUUID);
            e.setRespawnLocation(playerData.getLocation());
            dm.restorePlayerData(player);
            dm.removedDeadPlayer(playerUUID);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();

        DuelManager dm = plugin.getDuelManager();

        if (dm.isInDuel(playerUUID)) {
            dm.endDuel(player);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerUseCommand(PlayerCommandPreprocessEvent e) {

        Player player = e.getPlayer();
        String playerName = player.getName();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();

        if (dm.isInDuel(playerUUID)) {
            for (String allowedCommands : this.allowedCommands) {
                if (!(e.getMessage().equalsIgnoreCase(allowedCommands))) {
                    e.setCancelled(true);
                    Util.sendMsg(player, ChatColor.RED + "You may not use this command during a duel, use " +
                            ChatColor.AQUA + "/duel leave" + ChatColor.RED + " to leave.");
                    return;
                }
            }
        }

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();
        DuelManager dm = plugin.getDuelManager();
        if (dm.isFrozen(playerUUID)) {


            Location loc = player.getLocation();
            if (locations.get(player) == null) {
                locations.put(player, loc.toVector());
            }

            if (loc.getBlockX() != locations.get(player).getBlockX() || loc.getBlockZ() != locations.get(player).getBlockZ()) {
                if (plugin.isDebugEnabled()) {
                    SendConsoleMessage.debug("Frozen player in duel moved, teleporting back!");
                }

                loc.setX(locations.get(player).getBlockX());
                loc.setZ(locations.get(player).getBlockZ());
                loc.setPitch(loc.getPitch());

                loc.setYaw(loc.getYaw());

                player.teleport(loc);
            }
        }
    }
}




