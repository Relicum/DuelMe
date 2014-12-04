package com.relicum.dual.Player;

import com.relicum.dual.Configuration.PlayerProperties;
import com.relicum.dual.Configuration.PlayersInventory;
import com.relicum.dual.GameInventory;
import com.relicum.dual.InvType;
import com.relicum.ipsum.Location.SpawnPoint;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Name: GamePlayer.java Created: 15 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
@Setter
public class GamePlayer {

    private SpawnPoint lastLocation;
    private SpawnPoint lobbySpawn;

    private String name;
    private UUID uniqueId;
    private Player player;
    private DuelMe plugin;

    private boolean inLobby;
    private boolean saveInv = false;

    private PlayersInventory playerInventory;
    private boolean InvClear;
    private GameInventory lobbyInventory;

    public GamePlayer(Player player, DuelMe plugin) {

        this.player = player;
        this.plugin = plugin;
        this.lastLocation = getLocationAsSpawn();
        this.name = player.getName();
        this.uniqueId = player.getUniqueId();
        this.lobbySpawn = plugin.getLobby().getSpawn();
        this.saveInv = plugin.getSettings().getSaveInv();
        this.InvClear = false;
        setUp();

    }

    public void setLobbyInventory(GameInventory gameInv) {

        this.lobbyInventory = new GameInventory();
        lobbyInventory.setArmor(gameInv.getArmor(InvType.LOBBY), InvType.LOBBY);
        lobbyInventory.setContents(gameInv.getInvContents(InvType.LOBBY), InvType.LOBBY);


    }

    public void setGameInventory(Inventory contents, ItemStack[] armor) {

        this.lobbyInventory = new GameInventory();
        this.lobbyInventory.setNewInvType(InvType.LOBBY);
        this.lobbyInventory.setArmor(armor, InvType.LOBBY);
        this.lobbyInventory.setContents(contents.getContents(), InvType.LOBBY);

    }

    public void applyInventory(GameInventory inventory, InvType type) {

        player.getInventory().setArmorContents(inventory.getArmor(type));
        player.getInventory().setContents(inventory.getInvContents(type));
    }

    public void applyPlayerSettings(PlayerProperties settings) {

        settings.applySettings(getPlayer());

    }

    public PlayerProperties saveSettings(PlayerProperties settings) {

        settings.saveSettings(getPlayer());
        return settings;
    }

    private void setUp() {

        if (isSaveInv()) {
            playerInventory = new PlayersInventory();
            saveDefaultInventory(false);
        }
    }

    public boolean joinLobby() {

        if (isSaveInv()) {
            playerInventory = new PlayersInventory();
            teleportToLobby(false);
            getPlayer().updateInventory();


        }

        return true;


    }

    /**
     * Send message standard message to player.
     *
     * @param text the message text
     */
    public void sendMessage(String text) {

        getPlugin().getMsg().sendMessage(getPlayer(), text);
    }

    /**
     * Send error message to player.
     *
     * @param text the error message text
     */
    public void sendErrorMessage(String text) {

        getPlugin().getMsg().sendErrorMessage(getPlayer(), text);
    }

    /**
     * Get current location.
     *
     * @return the location
     */
    public Location getCurrentLocation() {

        return getPlayer().getLocation().clone();
    }

    public SpawnPoint getLocationAsSpawn() {

        Location l = getCurrentLocation();

        return new SpawnPoint(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }

    public void teleportToLobby() {

        getPlayer().teleport(getLobbySpawn().toLocation());
    }

    public void teleport(SpawnPoint loc) {


    }

    public void destroy() {
        plugin.getLogger().info("GamerPlayer object for " + name + " is being destroyed");
        player = null;
        plugin = null;
        name = null;
        uniqueId = null;
        lastLocation = null;
        lobbySpawn = null;
        playerInventory = null;
    }

    /**
     * Save default player inventory and store locally or also save it to the hard disk as well.
     *
     * @param toDisk set true to save the inventory o hard disk, false to just save it locally.
     */
    public void saveDefaultInventory(boolean toDisk) {

        this.getPlayerInventory().setInventory(getPlayer().getInventory());
    }

    /**
     * Clear current inventory.
     */
    public void clearCurrentInventory() {

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        setInvClear(true);

    }

    /**
     * Restore default inventory.
     *
     * @param fromDisk set to true to load the stored inventory from disk, false and it will use the local one.
     */
    public void restoreDefaultInventory(boolean fromDisk) {

        if (!fromDisk) {

            clearCurrentInventory();

            player.getInventory().setArmorContents(getPlayerInventory().getArmor());
            player.getInventory().setContents(getPlayerInventory().getInvContents());

            if (isInvClear())
                setInvClear(false);
        }
    }

    public void teleportToLobby(boolean tpFirst) {

        new BukkitRunnable() {

            int count = 1;

            @Override
            public void run() {

                if (count == 1) {

                    //if (current.getBlockX() != past.getBlockX() || current.getBlockY() != past.getBlockY() || current.getBlockZ() != past.getBlockZ()) {
                    if (!getCurrentLocation().getBlock().equals(getLastLocation().toLocation().getBlock())) {
                        player.sendMessage(ChatColor.RED + "Teleport was canceled, movement detected");
                        return;
                    }
                }
                if (count == 2) {

                    if (tpFirst) {
                        player.teleport(getLobbySpawn().toLocation());
                        getPlugin().getMsg().sendMessage(player, "You are being teleported now");
                    } else {

                        if (isSaveInv()) {
                            saveDefaultInventory(false);
                            clearCurrentInventory();
                        }
                    }
                }

                if (count == 3) {
                    if (!tpFirst) {

                        if (isSaveInv()) {
                            saveDefaultInventory(false);
                            clearCurrentInventory();
                        }

                    } else {
                        player.teleport(getLobbySpawn().toLocation());
                        getPlugin().getMsg().sendMessage(player, "You are being teleported now");
                    }
                }
                if (count == 4) {

                    getPlayer().updateInventory();
                }

            }


        }.runTaskTimer(plugin, 90l, 2l);
    }

    public void delayedTP(SpawnPoint spawnPoint, long delay, long period) {


    }
}
