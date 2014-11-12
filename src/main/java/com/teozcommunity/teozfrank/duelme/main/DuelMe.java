package com.teozcommunity.teozfrank.duelme.main;

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

import com.google.common.collect.ImmutableList;
import com.relicum.dual.Commands.Admin.LobbyTp;
import com.relicum.dual.Commands.Admin.SetLobby;
import com.relicum.dual.Commands.Admin.Status;
import com.relicum.dual.Commands.Join;
import com.relicum.dual.Commands.Leave;
import com.relicum.dual.Configuration.Settings;
import com.relicum.dual.lobby.Lobby;
import com.relicum.ipsum.Commands.CommandRegister;
import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Effect.Game.SimpleArena;
import com.relicum.ipsum.Location.SpawnPoint;
import com.teozcommunity.teozfrank.duelme.commands.DuelAdminExecutor;
import com.teozcommunity.teozfrank.duelme.commands.DuelExecutor;
import com.teozcommunity.teozfrank.duelme.events.PlayerEvents;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DuelMe extends JavaPlugin {

    private static DuelMe instance;

    /**
     * duelmanager class
     */
    private DuelManager duelManager;

    /**
     * filemanager class
     */
    private FileManager fileManager;

    /**
     * item manager class
     */
    private ItemManager itemManager;

    /**
     * mysql class
     */
    private MySql mySql;

    private Economy economy = null;

    /**
     * string to hold the plugin version
     */
    private static String version;

    private int errorCount;

    private Settings settings = null;
    CommandRegister register;

    private List<String> ALEVEL1;
    private List<String> PLEVEL1;

    public static String prefix;

    private Lobby lobby = null;

    public DuelMe() {

        this.errorCount = 0;
    }


    @Override
    public void onEnable() {

        ConfigurationSerialization.registerClass(SerializedLocation.class);  //Register deserializers
        ConfigurationSerialization.registerClass(PlayerData.class);
        ConfigurationSerialization.registerClass(SpawnPoint.class);
        ConfigurationSerialization.registerClass(SimpleArena.class);
        ConfigurationSerialization.registerClass(Region.class);
        ConfigurationSerialization.registerClass(Lobby.class);
        instance = this;


        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        SendConsoleMessage.info("Enabling.");
        SendConsoleMessage.info("Registering Commands");
        register = new CommandRegister(this);
        getCommand("1v1").setExecutor(register);
        getCommand("1v1").setTabCompleter(this);
        getCommand("da").setExecutor(register);
        getCommand("da").setTabCompleter(this);
        register.register(new Leave(this));
        register.register(new Status(this));
        register.register(new LobbyTp(this));
        register.register(new SetLobby(this));
        register.register(new Join(this));
        ALEVEL1 = ImmutableList.of("setlobby", "status", "lobbytp");
        PLEVEL1 = ImmutableList.of("join", "leave");

        register.endRegistration();


        settings = new Settings(this, "Settings");

        settings.doInit();


        startChecks();


        version = this.getDescription().getVersion();
        this.fileManager = new FileManager(this);
        prefix = getFileManager().getPrefix();
        this.setupYMLs();

        setupCommands();

        this.setupEconomy();
        this.duelManager = new DuelManager(this);
        new PlayerEvents(this);
        this.itemManager = new ItemManager(this);
        this.mySql = new MySql(this);
        getCommand("duel").setExecutor(new DuelExecutor(this));
        getCommand("dueladmin").setExecutor(new DuelAdminExecutor(this));
        this.getFileManager().loadDuelArenas();
        this.checkErrors();
    }

    @Override
    public void onDisable() {
        SendConsoleMessage.info("Disabling.");
        SendConsoleMessage.info("Saving Settings");
        this.settings.saveConfigs();
        register.destroy();
        if (settings.getEnable()) {
            this.endAllRunningDuels();
            this.getFileManager().saveDuelArenas();
            saveConfig();
        }
    }

    public static DuelMe getInstance() {
        return instance;
    }

    public Settings getSettings() {

        return settings;
    }

    public Lobby getLobby() {

        return lobby;
    }

    private void startChecks() {

        if (!settings.getEnable())
            getServer().getPluginManager().disablePlugin(this);

        if (settings.getLobbyReady() && getConfig().contains("lobby.settings")) {
            this.lobby = (Lobby) getConfig().get("lobby.settings");
            SendConsoleMessage.info("Lobby has been loaded");
        }
    }

    private void setupCommands() {
/*      this.commands = new CommandsManager<CommandSender>() {
          @Override
          public boolean hasPermission(CommandSender sender, String s) {
              return sender.hasPermission(s);
          }
      };

        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this,this.commands);*/


    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

        if (cmd.getName().contains("1v1") || cmd.getName().equalsIgnoreCase("da")) {

            sender.sendMessage(ChatColor.GREEN + "Hello from " + cmd.getLabel() + " name is " + cmd.getName() + " There is " + args.length);
            return true;
        }

/*        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You don't have permission.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, string received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "An error has occurred. See console.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }*/

        return true;
    }
    private void endAllRunningDuels() {
        DuelManager dm = this.getDuelManager();
        if (dm.getDuelArenas().size() == 0) {//if there are no duel arenas
            return;
        }
        for (DuelArena duelArena : dm.getDuelArenas()) {
            dm.endDuel(duelArena);
        }

    }

    /**
     * Check for startup errors for outdated plugin configs
     */
    private void checkErrors() {
        this.checkConfigVersions();
        if (this.errorCount != 0) {
            SendConsoleMessage.warning(ChatColor.RED + "There were " + ChatColor.AQUA + errorCount +
                    ChatColor.RED + " startup error(s), plugin DISABLED!");
            this.getPluginLoader().disablePlugin(this);
        } else {
            SendConsoleMessage.info("Successfully Enabled - SPIGOT TEST BUILD");
        }
    }


    //Tmp Disable Stats and update checking to avoid any confusion.

    /**
     * check for updates to the plugin
     */
    public void checkForUpdates() {
//        if(this.getConfig().getBoolean("duelme.checkforupdates")) {
//
//            new UpdateCheckerThread(this);
//        }
    }

    /**
     * attempt to submit the plugin stats
     * to mcstats.org
     */
    public void submitStats() {
//        try {
//            MetricsLite metrics = new MetricsLite(this);
//           metrics.start();
//        } catch (IOException e) {
//            SendConsoleMessage.severe("Could not submit the stats! :(");
//        }
    }

    /**
     * setup the yml files used in the plugin
     */
    public void setupYMLs() {
        if (!(new File(getDataFolder(), "config.yml")).exists()) {
            SendConsoleMessage.info("Saving default config.yml.");
            saveDefaultConfig();
        }
        if (!(new File(getDataFolder(), "duelarenas.yml")).exists()) {
            SendConsoleMessage.info("Saving default duelarenas.yml.");
            this.getFileManager().saveDefaultDuelArenas();
        }

        if (!(new File(getDataFolder(), "messages.yml")).exists()) {
            SendConsoleMessage.info("Saving default messages.yml.");
            this.getFileManager().saveDefaultMessages();
        }
    }

    /**
     * check the config file versions and see
     * do they match the latest version
     */
    public void checkConfigVersions() {
        if (new File(getDataFolder(), "config.yml").exists()) {
            if (fileManager.getConfigVersion() != 1.4) {
                SendConsoleMessage.warning("Your config.yml is out of date! please remove or back it up before using the plugin!");
                errorCount++;
            }
        }
    }

    /**
     * setup economy
     *
     * @return economy object
     */
    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }

    /**
     * get the duel manager object
     *
     * @return duel manager object
     */
    public DuelManager getDuelManager() {
        return this.duelManager;
    }

    /**
     * get the file manager object
     *
     * @return file manager object
     */
    public FileManager getFileManager() {
        return this.fileManager;
    }

    /**
     * get the item manager object
     *
     * @return item manager object
     */
    public ItemManager getItemManager() {
        return itemManager;
    }

    /**
     * get the plugin version
     *
     * @return the plugin version
     */
    public static String getVersion() {
        return version;
    }

    /**
     * get MySql object
     *
     * @return the MySql object
     */
    public MySql getMySql() {
        return this.mySql;
    }

    /**
     * is debug mode enabled
     *
     * @return true if enabled, false if not
     */
    public boolean isDebugEnabled() {
        return getFileManager().isDebugEnabled();
    }

    public boolean isUsingSeperatedInventories() {
        return this.getFileManager().isUsingSeperateInventories();
    }

    public Economy getEconomy() {
        return economy;
    }

    //todo.me.important must get this moved when I get the chance
    public static String getPrefix() {
        return prefix;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String s, String[] args) {
        if (args.length == 1) {
            if (cmd.getName().equalsIgnoreCase("da")) {
                return StringUtil.copyPartialMatches(args[0], ALEVEL1, new ArrayList<>(ALEVEL1.size()));
            }
            if (cmd.getName().equalsIgnoreCase("1v1")) {
                return StringUtil.copyPartialMatches(args[0], PLEVEL1, new ArrayList<>(PLEVEL1.size()));
            }

        }

        return Collections.emptyList();
    }



}
