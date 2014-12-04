package com.relicum.dual.Commands.Admin;

import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.titleapi.WrappedPlayer;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Name: LobbyTp.java Created: 12 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"lobbytp"}, perm = "dual.admin.lobbytp", desc = "Used to test the lobby spawn", isSub = true
        , parent = "da", min = 0, max = 0, usage = "/da lobbytp")
public class LobbyTp extends AbstractCommand {
    Msg msg;

    public LobbyTp(Plugin plugin) {
        super(plugin);
        msg = DuelMe.getInstance().getMsg();
        setHelp(formatHelp("da", "lobbytp", "TP an admin to the lobby spawn, this is only to test the spawn point itself"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {

        Player player = (Player) sender;

        if (!DuelMe.getInstance().getSettings().getLobbyReady()) {

            msg.sendAdminMessage(player, "The lobby has not been setup. You need to do that now");
            return true;
        }
        if (DuelMe.getInstance().getLobby().getRegion().isRegion(player.getLocation().toVector())) {

            msg.sendErrorMessage(player, "You can not tp to the lobby as you are already in it");
            return true;
        }
        Location location = DuelMe.getInstance().getLobby().getSpawn().toLocation();

        if (!location.getChunk().isLoaded()) {

            location.getChunk().load();

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(location);

                    msg.sendMessage(player, "You are now at the lobby spawn");
                }
            }.runTaskLater(DuelMe.getInstance(), 5l);
        } else {
            player.teleport(location);
            // player.sendMessage(ChatColor.GREEN + "You are now at the lobby spawn");
            msg.sendMessage(player, "You are now at the lobby spawn");
            WrappedPlayer wrappedPlayer = new WrappedPlayer(player);
           /* if (TitleApi.get().checkVersion(player.getUniqueId())) {
                wrappedPlayer.getPlayer().get().getHandle().playerConnection.sendPacket(DuelMe.getInstance().getTitleApi().getResetPacket());
                wrappedPlayer.getPlayer().get().getHandle().playerConnection.sendPacket(DuelMe.getInstance().getTitleApi().getTimesPacket(-1, 80, -1));
                wrappedPlayer.getPlayer().get().getHandle().playerConnection.sendPacket(DuelMe.getInstance().getTitleApi().getTitlePacket("&6Welcome to 1-V-1 by PVPING NINJA"));
            }*/
            msg.sendMessage(player, "&6Welcome to 1-V-1 by PVPING NINJA");

            wrappedPlayer.clear();

        }

        return true;
    }
}
