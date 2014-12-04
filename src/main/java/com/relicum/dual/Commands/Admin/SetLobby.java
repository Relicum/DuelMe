package com.relicum.dual.Commands.Admin;

import com.relicum.dual.lobby.Lobby;
import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Location.SpawnPoint;
import com.relicum.ipsum.Utils.MathUtils;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.ipsum.io.GsonIO;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockVector;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Paths;

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

    Msg msg;

    public SetLobby(Plugin plugin) {
        super(plugin);
        msg = DuelMe.getInstance().getMsg();
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
            msg.sendErrorMessage(player, "Error: Lobby Region has not been set");

            return true;
        }

        BlockVector minPoint = new BlockVector(region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());


        BlockVector maxPoint = new BlockVector(region.getMaximumPoint().getX(), region.getMaximumPoint().getY(), region.getMaximumPoint().getZ());

        if (!player.getLocation().toVector().isInAABB(minPoint, maxPoint)) {
            msg.sendErrorMessage(player, "Error the Lobby Spawn Point is not inside the lobby region");

            return true;
        }

        //player.sendMessage("Min point is " + min.toString() + " and max point is " + max.toString());

        SpawnPoint lobbySpawn = new SpawnPoint(player.getWorld().getName(), MathUtils.floor(player.getLocation().getX())
                + 0.5d, MathUtils.floor(player.getLocation().getY()) + 0.5d, MathUtils.floor(player.getLocation().getZ()) + 0.5d,
                MathUtils.getDirection(player.getLocation().getYaw()), player.getLocation().getPitch());
        Region region1 = new Region(minPoint, maxPoint, player.getWorld().getName());

        Type lType = new TypeToken<Lobby>() {
        }.getType();
        try {
            GsonIO.writeToFile(Paths.get(plugin.getDataFolder().toString() + File.separator, "lobby.json"), new Lobby(region1, lobbySpawn), lType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DuelMe.getInstance().getConfig().set("lobby.settings", new Lobby(region1, lobbySpawn));
        DuelMe.getInstance().saveConfig();
        DuelMe.getInstance().getSettings().setLobbyReady(true);
        DuelMe.getInstance().getSettings().saveConfigs();
        DuelMe.getInstance().loadLobby();
        worldEditPlugin.getWorldEdit().clearSessions();
        log("Lobby Region and Spawn successfully created");
        msg.sendMessage(player, "Lobby Region and Spawn successfully created");
        worldEditPlugin = null;
        region = null;
        region1 = null;
        minPoint = null;
        maxPoint = null;
        lobbySpawn = null;

        return true;
    }
}
