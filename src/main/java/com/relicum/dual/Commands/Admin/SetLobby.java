package com.relicum.dual.Commands.Admin;

import com.relicum.dual.lobby.Lobby;
import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Location.SpawnPoint;
import com.relicum.ipsum.Utils.MathUtils;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

/**
 * SetLobby
 *
 * @author Relicum
 * @version 0.0.1
 */
@Command(aliases = {"setlobby"}, perm = "dual.admin.setlobby",
        desc = "Set the lobby points using WorldEdit and stand where you want the spawn point to be",
        min = 0, max = 0, isSub = true, parent = "da", usage = "/da setlobby")
public class SetLobby extends AbstractCommand {


    public SetLobby(Plugin plugin) {
        super(plugin);
        setHelp(formatHelp("da", "setlobby", "Set the lobby points using WorldEdit and stand where you want the spawn point to be"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {

        Player player = (Player) sender;

        WorldEditPlugin worldEditPlugin;
        worldEditPlugin = (WorldEditPlugin) DuelMe.getInstance().getServer().getPluginManager().getPlugin("WorldEdit");

        CuboidRegion region;
        try {
            region = CuboidRegion.makeCuboid(worldEditPlugin.getSelection(player).getRegionSelector().getRegion());

        } catch (Exception ignored) {
            player.sendMessage(ChatColor.RED + "Error: Lobby Region has not been set");
            return true;
        }

        BlockVector minPoint = new BlockVector(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());


        BlockVector maxPoint = new BlockVector(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());

        if (!player.getLocation().toVector().isInAABB(minPoint, maxPoint)) {
            player.sendMessage(ChatColor.RED + "Error the Lobby Spawn Point is not inside the lobby region");
            return true;
        }
        player.sendMessage(ChatColor.GREEN + "Lobby Region and Spawn successfully created");
        //player.sendMessage("Min point is " + min.toString() + " and max point is " + max.toString());

        SpawnPoint lobbySpawn = new SpawnPoint(player.getWorld().getName(), MathUtils.floor(player.getLocation().getX())
                + 0.5d, MathUtils.floor(player.getLocation().getY()) + 0.5d, MathUtils.floor(player.getLocation().getZ()) + 0.5d,
                MathUtils.getDirection(player.getLocation().getYaw()), player.getLocation().getPitch());
        Region region1 = new Region(minPoint, maxPoint, player.getWorld().getName());
        DuelMe.getInstance().getConfig().set("lobby.settings", new Lobby(region1, lobbySpawn));
        DuelMe.getInstance().saveConfig();
        DuelMe.getInstance().getSettings().setLobbyReady(true);
        DuelMe.getInstance().getSettings().saveConfigs();
        worldEditPlugin.getWorldEdit().clearSessions();

        worldEditPlugin = null;
        region = null;
        region1 = null;
        minPoint = null;
        maxPoint = null;
        lobbySpawn = null;

        return true;
    }
}
