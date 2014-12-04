package com.relicum.dual.Runnables;

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

/**
 * The type TP delay.
 */
public class TPDelay implements Runnable {

    private final Player player;
    private final Location past;
    private final Location destination;
    private final String message;
    private boolean joining;
    private int count = 1;

    public TPDelay(Player player, Location playerLocation, Location destination, String message, boolean joining) {
        this.player = player;
        this.past = playerLocation;
        this.destination = destination;
        this.message = message;
        this.joining = joining;
    }


    @Override
    public void run() {

        if (count > 3) {
            return;
        }

        if (count == 1) {
            Location current = player.getLocation();
            if (current.getBlockX() != past.getBlockX() || current.getBlockY() != past.getBlockY() || current.getBlockZ() != past.getBlockZ()) {

                player.sendMessage(ChatColor.RED + "Teleport was canceled, movement detected");
                return;
            }
            player.getInventory().clear();
            player.getInventory().setArmorContents(null);
            if (joining) {
                player.setMetadata("1v1-known", new FixedMetadataValue(DuelMe.getInstance(), true));

            } else
                player.removeMetadata("1v1-known", DuelMe.getInstance());


        }
        if (count == 2) {
            player.teleport(destination);
            player.sendMessage(message);

            player.updateInventory();
        }

        count++;
    }
}
