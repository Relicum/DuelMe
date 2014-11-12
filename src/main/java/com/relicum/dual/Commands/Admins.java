package com.relicum.dual.Commands;

import com.relicum.ipsum.sk89q.Commands.minecraft.util.commands.Command;
import com.relicum.ipsum.sk89q.Commands.minecraft.util.commands.NestedCommand;
import org.bukkit.ChatColor;

/**
 * Admin deals with admin commands.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Admins {

    public static class ParentCommand {
        @Command(aliases = {"da", "dadmin"},
                desc = "Access to admin commands",
                min = 0,
                max = -1)
        @NestedCommand({Admins.class})
        public static void duelAdmin() {
        }

    }


    public static String formatHelp(String cm, String mess) {

        return ChatColor.GOLD + "/da " + cm + ChatColor.GREEN + " : " + mess;
    }

}
