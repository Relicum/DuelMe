package com.relicum.dual.Commands.Admin;

import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Annotations.Console;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.relicum.ipsum.Utils.Msg;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Status
 *
 * @author Relicum
 * @version 0.0.1
 */
@Console
@Command(aliases = {"status"}, perm = "dual.admin.status", desc = "View the current status of the plugin setup", usage = "/<command>", isSub = false)
public class Status extends AbstractCommand {

    Msg msg;

    public Status(Plugin plugin) {
        super(plugin);
        msg = DuelMe.getInstance().getMsg();
        setHelp(formatHelp("", "status", "View the current status of the plugin setup"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {
/*        msg.sendHeader(sender);
        sender.sendMessage(" ");
        if (DuelMe.getInstance().getSettings().getSetupDone()) {
            //sender.sendMessage(ChatColor.GREEN + "Everything has been setup your ready to dual");
            msg.sendAdminMessage(sender, "Everything has been setup your ready to dual");

        }
        if (!DuelMe.getInstance().getSettings().getLobbyReady()) {
            //sender.sendMessage(ChatColor.DARK_PURPLE + "The lobby has not been setup. You need to do that now");
            msg.sendAdminMessage(sender, "The lobby has not been setup. You need to do that now");
        }
        if (!DuelMe.getInstance().getSettings().getArenaSetup()) {
            //sender.sendMessage(ChatColor.DARK_PURPLE + "No arenas are set up you need to set up an arena before you can dual");
            msg.sendAdminMessage(sender, "No arenas are set up you need to set up an arena before you can dual");
        }
        sender.sendMessage(" ");
        msg.sendFooter(sender);*/

        Player player = (Player) sender;
        World world = player.getWorld();

        LivingEntity entity = (Cow) world.spawnEntity(player.getLocation(), EntityType.COW);

        entity.setCustomName(ChatColor.GOLD + "Relicums Cow");
        entity.setCustomNameVisible(true);

        player.sendMessage(ChatColor.GREEN + "You cow has been spawned");


        return true;
    }

}
