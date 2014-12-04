package com.relicum.dual.Commands;

import com.relicum.dual.Events.GPJoinEvent;
import com.relicum.dual.Player.PlayerTmpData;
import com.relicum.dual.Runnables.TPDelay;
import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Items.Inventory.InventoryFactory;
import com.relicum.ipsum.Location.SpawnPoint;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.ipsum.io.GsonIO;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;

/**
 * Join
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"join"}, perm = "dual.player.join", desc = "Used to join the dual lobby", isSub = true, parent = "1v1", usage = "/1v1 join")
public class Join extends AbstractCommand {

    private Msg msg;

    public Join(Plugin plugin) {
        super(plugin);
        msg = DuelMe.getInstance().getMsg();
        setHelp(formatHelp("1v1", "join", "Used to join the dual lobby"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
        Type type = new TypeToken<PlayerTmpData>() {
        }.getType();
        Player player = (Player) sender;

        if (!DuelMe.getInstance().getSettings().getCanJoin()) {
            msg.sendErrorMessage(player, "Sorry players can not currently join 1-V-1");
            return true;
        }

        if (DuelMe.getInstance().getLobby().getRegion().isRegion(player.getLocation().toVector())) {

            msg.sendErrorMessage(player, "You can not tp to the lobby as you are already in it");
            return true;
        }

        PlayerTmpData playerTmpData = new PlayerTmpData();
        playerTmpData.setUuid(player.getUniqueId().toString())
                .setGameMode(player.getGameMode().name())
                .setHealth(player.getHealth())
                .setFood(player.getFoodLevel())
                .setName(player.getName())
                .setLastLocation(getPlayerSpawn(player.getLocation()))
        ;
        String[] inv = InventoryFactory.playerInventoryToBase64(player.getInventory());
        playerTmpData.setInventory(inv[0])
                .setArmor(inv[1]);
        DuelMe.getInstance().getConfig().set("player.content", inv[0]);
        DuelMe.getInstance().getConfig().set("player.armor", inv[1]);
        DuelMe.getInstance().saveConfig();

        GPJoinEvent event = new GPJoinEvent(player, getPlayerSpawn(player.getLocation()));
        Bukkit.getServer().getPluginManager().callEvent(event);

        try {
            GsonIO.writeToFile(Paths.get(plugin.getDataFolder().toString() + File.separator, player.getUniqueId().toString() + ".json"), playerTmpData, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        msg.sendMessage(player, "You will be teleport to the 1-V-1 lobby in 5 seconds. Do not move or the teleport will be canceled");
        Location location = DuelMe.getInstance().getLobby().getSpawn().toLocation();
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new TPDelay(player, player.getLocation().clone(), location, msg.getMessage("&6Welcome to the 1-V-1 Lobby"), true), 100l, 20l);

        if (!location.getChunk().isLoaded()) {

            location.getChunk().load();

        }

        return true;
    }

    private SpawnPoint getPlayerSpawn(Location l) {

        return new SpawnPoint(l.getWorld().getName(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch());
    }
}
