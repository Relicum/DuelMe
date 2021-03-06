package com.teozcommunity.teozfrank.duelme.commands.admin;

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

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Region;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import com.teozcommunity.teozfrank.duelme.util.DuelArena;
import com.teozcommunity.teozfrank.duelme.util.DuelManager;
import com.teozcommunity.teozfrank.duelme.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by frank on 20/12/13.
 */
public class CreateCmd extends DuelAdminCmd {

    private double pos1x, pos1y, pos1z, pos2x, pos2y, pos2z;
    private String worldName;
    private World selWorld;
    private Location pos1;
    private Location pos2;

    public CreateCmd(DuelMe plugin, String mainPerm) {
        super(plugin, mainPerm);
    }

    @Override
    public void run(DuelArena duelArena, CommandSender sender, String subCmd, String[] args) {
        if (!(sender instanceof Player)) {
            Util.sendMsg(sender, NO_CONSOLE);
            return;
        }

        if (args.length < 1) {
            Util.sendMsg(sender, ChatColor.GREEN + "Usage: /dueladmin create <arenaname>");
            return;
        }

        Player p = (Player) sender;


        try {

            LocalSession session = WorldEdit.getInstance().getSession(p.getName());
            LocalWorld world = session.getSelectionWorld();

            Region region = session.getSelection(world);

            this.worldName = region.getWorld().getName();
            this.selWorld = Bukkit.getWorld(worldName);

            this.pos1x = region.getMaximumPoint().getX();
            this.pos1y = region.getMaximumPoint().getY();
            this.pos1z = region.getMaximumPoint().getZ();
            this.pos1 = new Location(selWorld, pos1x, pos1y, pos1z);

            this.pos2x = region.getMinimumPoint().getX();
            this.pos2y = region.getMinimumPoint().getY();
            this.pos2z = region.getMinimumPoint().getZ();
            this.pos2 = new Location(selWorld, pos2x, pos2y, pos2z);

        } catch (IncompleteRegionException e) {
            Util.sendMsg(sender, ChatColor.YELLOW + "You have not selected a full region, please make sure you have selected two points!");
            return;
        } catch (NullPointerException e) {
            Util.sendMsg(sender, ChatColor.RED + "You have not selected a region, please select one first!");
            return;
        }
        String arenaName = getValue(args, 0, "Arena");

        DuelManager dm = plugin.getDuelManager();

        for (DuelArena da : dm.getDuelArenas()) {
            if (da.getName().equalsIgnoreCase(arenaName)) {
                Util.sendMsg(sender, ChatColor.RED + "There is already a duel arena with the name " + arenaName + ".");
                return;
            }
        }

        DuelArena newArena = new DuelArena(arenaName, pos1, pos2);

        dm.addDuelArena(newArena);

        Util.sendMsg(sender, ChatColor.GREEN + "Created a new Duel arena called " + ChatColor.GOLD + arenaName + ".");

    }
}
