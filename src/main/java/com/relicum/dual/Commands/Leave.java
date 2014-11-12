package com.relicum.dual.Commands;

import com.relicum.ipsum.Annotations.Command;
import com.relicum.ipsum.Annotations.Console;
import com.relicum.ipsum.Commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Name: Leave.java Created: 12 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Console
@Command(aliases = {"leave"}, perm = "dual.player.leave", desc = "Used to leave Dual", min = 0, max = 0, isSub = true, parent = "1v1", usage = "/1v1 leave")
public class Leave extends AbstractCommand {


    public Leave(Plugin plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, String s, String[] strings) {
        sender.sendMessage(ChatColor.AQUA + "Hello");

        return true;
    }

}
