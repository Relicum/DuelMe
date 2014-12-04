package com.relicum.dual.Commands;

import com.relicum.dual.Events.GPLeaveLobbyEvent;
import com.relicum.dual.Player.PlayerTmpData;
import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Items.Inventory.InventoryFactory;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.ipsum.io.GsonIO;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Name: Leave.java Created: 12 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"leave"}, perm = "dual.player.leave", desc = "Used to leave Dual", min = 0, max = 0, isSub = true, parent = "1v1", usage = "/1v1 leave")
public class Leave extends AbstractCommand {

    Msg msg;

    public Leave(Plugin plugin) {
        super(plugin);
        msg = DuelMe.getInstance().getMsg();
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] strings) {

        Player player = (Player) sender;

        if (!player.hasMetadata("1v1-known")) {
            msg.sendErrorMessage(player, "You can't run the command you are not part of 1v1");
            return true;
        }

        Inventory inventory = InventoryFactory.fromString(DuelMe.getInstance().getConfig().getString("player.content"));
        ItemStack[] armor = InventoryFactory.itemStackArrayFromBase64(DuelMe.getInstance().getConfig().getString("player.armor"));

        player.getInventory().setContents(inventory.getContents());
        player.getInventory().setArmorContents(armor);


        if (DuelMe.getInstance().getSettings().getLastDestination() && Files.exists(Paths.get(plugin.getDataFolder().toString() + File.separator + player.getUniqueId().toString() + ".json"))) {
            Type type = new TypeToken<PlayerTmpData>() {
            }.getType();
            PlayerTmpData playerTmpData = new PlayerTmpData();

            try {
                playerTmpData = GsonIO.readFromFile(Paths.get(plugin.getDataFolder().toString() + File.separator + player.getUniqueId().toString() + ".json"), type, playerTmpData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            GPLeaveLobbyEvent event = new GPLeaveLobbyEvent(player);
            plugin.getServer().getPluginManager().callEvent(event);

            player.teleport(playerTmpData.getLastLocation().toLocation());
            msg.sendMessage(player, "Leaving 1-v-1 and returning to last known location");

        } else {
            msg.sendMessage(player, "Leaving 1-v-1 and returning to world spawn");
            player.teleport(player.getWorld().getSpawnLocation());
        }
        DuelMe.getInstance().getConfig().set("player.content", null);
        DuelMe.getInstance().getConfig().set("player.armor", null);
        DuelMe.getInstance().saveConfig();
        player.updateInventory();
        player.removeMetadata("1v1-known", plugin);

        return true;
    }

}
