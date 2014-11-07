package com.teozcommunity.teozfrank.duelme.util;

/**
        The MIT License (MIT)

        Copyright (c) 2014 teozfrank

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;

//todo.me Create a separate message managing class that stems from an Interface
public class Util {

    /**
     * duelme plugin
     */
    private DuelMe plugin;

    /**
     * random class
     */
    Random rand;

    public static String UNKNOWN_CMD = ChatColor.RED+"Unknown Command!";
    public static String NO_PERMS = ChatColor.RED+"You do not have permission!";
    public static String NO_ARENAS = ChatColor.RED + "There are no arenas to start the duel! Please notify a member of staff!";
    public static final String LINE_BREAK = ChatColor.LIGHT_PURPLE + "-----------------------------------------------------";


    public Util(DuelMe plugin) {
        this.plugin = plugin;
        this.rand = new Random();
    }

    //todo.me move the actual prefix into this class and remove static reference
    /**
    * Method to broadcast a plugin message to all online players
    * @param message message to send to all players
    */
    public static void broadcastMessage(String message) {

        Bukkit.broadcastMessage(DuelMe.getPrefix() + " " + message);

    }


    /**
     * sends a message to the recipient with the plugin prefix
     * @param sender the recipient to send the message to
     * @param message the message to send the sender
     */
    public static void sendMsg(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }

        Player p = null;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (p == null) {
            Bukkit.getLogger().info(DuelMe.getPrefix() + " " + ChatColor.stripColor(message));
        } else {
            p.sendMessage(DuelMe.getPrefix() + " " + message);
        }
    }

    /**
     * sends plugin credits to sender of command
     * @param sender the command sender
     */
    public static void sendCredits(CommandSender sender){
        sendEmptyMsg(sender,ChatColor.GOLD + "                               V"+DuelMe.getVersion());
    }

    /**
     * send a plugin message to a player
     * @param player the player to send the message to
     * @param message the message to send the player
     */
    public static void sendMsg(Player player, String message){
        player.sendMessage(DuelMe.getPrefix() + " " + message);
    }


    /**
     * send the same message to two players
     * @param sender the duel sender
     * @param target the duel acceptor
     * @param message the message to send to the two players
     */
    public static void sendMsg(Player sender, Player target, String message){
        sender.sendMessage(DuelMe.getPrefix() + " " + message);
        target.sendMessage(DuelMe.getPrefix() + " " + message);
    }

    /**
     * sends a empty message to the recipient with the plugin prefix
     * @param sender the recipient to send the message to
     * @param message the message to send the sender
     */
    public static void sendEmptyMsg(CommandSender sender, String message) {
        if (sender == null) {
            return;
        }

        Player p = null;

        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (p == null) {
            Bukkit.getLogger().info(ChatColor.stripColor(message));
        } else {
            p.sendMessage(message);
        }
    }


    //todo.me This really should be moved into the Dual Arena class so each arena has its only checker.
    /**
     * check to see if a given location with within a given region
     * <p>This presumes loc1 is the min point and loc2 is the max point.
     *
     * @param playerLoc player or entity location
     * @param loc1 position 1 of the region
     * @param loc2 position 2 of the region
     * @return return true if the location is within the region, false if not
     */
    public static boolean isInRegion(Location playerLoc, Location loc1, Location loc2) {

        return playerLoc.toVector().isInAABB(loc1.toVector(), loc2.toVector());

    }

    /**
     * set the time for countdown by setting the players level
     * @param player the player to set the level (time) to
     * @param time the time in seconds
     */
    public static void setTime(Player player, int time) {
        player.setLevel(time);
    }

    /**
     * set the time for countdown for both players by setting the players level
     * @param sender the duel sender
     * @param target the duel acceptor
     * @param time the time in seconds
     */
    public static void setTime(Player sender, Player target, int time) {
        sender.setLevel(time);
        target.setLevel(time);
    }


}
