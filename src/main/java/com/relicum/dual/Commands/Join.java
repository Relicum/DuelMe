package com.relicum.dual.Commands;

import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Join
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"join"}, perm = "dual.player.join", desc = "Used to join the dual lobby", isSub = true, parent = "1v1", usage = "/1v1 join")
public class Join extends AbstractCommand {


    public Join(Plugin plugin) {
        super(plugin);
        setHelp(formatHelp("1v1", "join", "Used to join the dual lobby"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {

        Player player = (Player) sender;

        if (!DuelMe.getInstance().getSettings().getLobbyReady()) {
            player.sendMessage(ChatColor.DARK_PURPLE + "The lobby has not been setup. You need to do that now");
            return true;
        }
        Location location = DuelMe.getInstance().getLobby().getSpawn().toLocation();

        if (!location.getChunk().isLoaded()) {

            location.getChunk().load();

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(location);
                    player.sendMessage(ChatColor.GREEN + "You are now at the lobby spawn");
                }
            }.runTaskLater(DuelMe.getInstance(), 5l);
        } else {
            player.teleport(location);
            player.sendMessage(ChatColor.GREEN + "You are now at the lobby spawn");
        }

        return false;
    }
}
