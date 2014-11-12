package com.relicum.dual.Commands.Admin;

import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Annotations.Console;
import com.relicum.ipsum.Commands.AbstractCommand;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
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


    public Status(Plugin plugin) {
        super(plugin);
        setHelp(formatHelp("", "status", "View the current status of the plugin setup"));
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] args) {

        if (DuelMe.getInstance().getSettings().getSetupDone()) {
            sender.sendMessage(ChatColor.GREEN + "Everything has been setup your ready to dual");
            return true;
        }
        if (!DuelMe.getInstance().getSettings().getLobbyReady()) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "The lobby has not been setup. You need to do that now");
            return true;
        }
        if (!DuelMe.getInstance().getSettings().getArenaSetup()) {
            sender.sendMessage(ChatColor.DARK_PURPLE + "No arenas are set up you need to set up an arena before you can dual");
            return true;
        }

        return false;
    }

}
