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
import com.relicum.dual.Commands.Trader;
import com.relicum.dual.Configuration.PlayerSettings;
import com.relicum.dual.Configuration.PlayerSettingsLoader;
import com.relicum.dual.Configuration.Settings;
import com.relicum.dual.Messages.FileLoader;
import com.relicum.dual.lobby.Lobby;
import com.relicum.dual.lobby.LobbyManager;
import com.relicum.ipsum.Commands.CommandRegister;
import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Effect.Game.SimpleArena;
import com.relicum.ipsum.Location.SpawnPoint;
import com.relicum.ipsum.Runnables.DirectExecutor;
import com.relicum.ipsum.Runnables.Scheduler;
import com.relicum.ipsum.Utils.Msg;
import com.relicum.ipsum.Utils.MsgBuilder;
import com.relicum.ipsum.io.GsonIO;
import com.relicum.titleapi.TitleApi;
import com.relicum.titleapi.TitleMaker;
import com.teozcommunity.teozfrank.duelme.commands.DuelAdminExecutor;
import com.teozcommunity.teozfrank.duelme.commands.DuelExecutor;
import com.teozcommunity.teozfrank.duelme.events.PlayerEvents;
import com.teozcommunity.teozfrank.duelme.mysql.MySql;
import com.teozcommunity.teozfrank.duelme.util.*;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;

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

    public static String prefix;

    //------------------------------//

    // Tab Complete For Commands
    private List<String> ALEVEL1;
    private List<String> PLEVEL1;


    // Command Register and Manager
    @Getter
    private CommandRegister register;


    // Custom Async Scheduler
    @Getter
    private Scheduler scheduler;


    // Holds all the generic settings and configurations
    @Getter
    private Settings settings = null;


    // Current message manger and formatting.
    @Getter
    private MsgBuilder msgBuilder;
    @Getter
    private Msg msg;
    // Gson Type Tokens
    @Getter
    private Type msgType = new TypeToken<MsgBuilder>() {
    }.getType();
    @Getter
    private Type lobbyType = new TypeToken<Lobby>() {
    }.getType();

    private Type playSetType = new TypeToken<PlayerSettings>() {
    }.getType();

    // Lobby Controller
    @Getter
    private Lobby lobby = null;

    // Get 1.8 Titles Api
    @Getter
    private TitleMaker titleApi;

    @Getter
    private LobbyManager lobbyManager;

    // Executor that runs tasks on the primary thread
    @Getter
    private DirectExecutor mainExecutor;

    @Getter
    private PlayerSettingsLoader playerSettingsLoader;

    @Getter
    private Path path = Paths.get(getDataFolder().toString() + File.separator);



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
        scheduler = new Scheduler(2, false);
        mainExecutor = new DirectExecutor();
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        msgBuilder = new MsgBuilder();
        // this.vills=new ArrayList<>();
/*       msgBuilder.
                setInfoColor("&e")
                .setErrorColor("&c")
                .setAdminColor("&a")
                .setAnnounceColor("&6")
                .setPrefix("&8[&61&b-V-&61")
                .setPrefixNoColor("[1v1]")
                .setLogPrefix("&e[&a1v1&e]")
                .setLogInfoColor("&b")
                .setLogSevereColor("&4")
                .setLogWarningColor("&d")
                .setHighLight("&8")
                .setHeader(StringStyles.fullLine(ChatColor.RED, ChatColor.GREEN, ChatColor.ITALIC, '-'))
                .setFooter(StringStyles.fullLine(ChatColor.GREEN, ChatColor.RED, ChatColor.ITALIC, '-'));*/

        //System.out.println(msgBuilder.toString());

        this.playerSettingsLoader = new PlayerSettingsLoader(this);


/*        playerSettings = new PlayerSettings();

        try {
            //  writer = new JsonWriter(Files.newBufferedWriter(Paths.get(getDataFolder().toString() + File.separator, "msg.json")));
            //  writer.setIndent("    ");
            // gson.toJson(msgBuilder, msgType, writer);

            playerSettings = GsonIO.readFromFile(Paths.get(getDataFolder().toString() + File.separator, "playerTemplate.json"), playSetType, playerSettings);
            playerSettings.duplicateProperties(PlayerSettings.PROPERTIES_TYPE.LOBBY, PlayerSettings.PROPERTIES_TYPE.WOODEN);
            playerSettings.getPlayerProperties(PlayerSettings.PROPERTIES_TYPE.WOODEN).setAllowedFly(true);
            GsonIO.writeToFile(Paths.get(getDataFolder().toString() + File.separator, "playerTemplate.json"), playerSettings,playSetType);

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(playerSettings.toString());*/

        Callable<MsgBuilder> callable = new FileLoader(getPath("msg.json"), msgType);
        try {
            msgBuilder = scheduler.submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
/*        try {

         msgBuilder=GsonIO.readFromFile(getPath("msg.json"),type,msgBuilder);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        msg = msgBuilder.createMsg();

        msg.logFormatted(Level.INFO, "Log testing");

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
        register.register(new Trader(this));
        ALEVEL1 = ImmutableList.of("setlobby", "status", "lobbytp", "trader");
        PLEVEL1 = ImmutableList.of("join", "leave");

        register.endRegistration();


        settings = new Settings(this, "Settings");

        settings.doInit();


        // System.out.println(json);


        startChecks();

        try {
            titleApi = TitleApi.get().getTitleApi(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        version = this.getDescription().getVersion();
        this.fileManager = new FileManager(this);
        prefix = getFileManager().getPrefix();
        this.setupYMLs();

        setupCommands();

        this.setupEconomy();
        this.duelManager = new DuelManager(this);
        PlayerEvents pe = new PlayerEvents(this);
        pe.getInventory();
        this.itemManager = new ItemManager(this);
        this.mySql = new MySql(this);
        getCommand("duel").setExecutor(new DuelExecutor(this));
        getCommand("dueladmin").setExecutor(new DuelAdminExecutor(this));
        this.getFileManager().loadDuelArenas();
        this.checkErrors();
  /*
      //  Gson gson = GsonLoader.gsond;
       //     ArmorInventory ainv = new ArmorInventory();
        ArmorInventory ainv = new ArmorInventory(Ipsum.getInstance()
                .getSimpleItemFactory()
                .getSetOfArmourBuilder(ArmourMaterials.DIAMOND)
                .addUnsafeEnchantment(Enchant.get(Enchantment.DURABILITY, 7, true))
                .setDisplayName(ChatColor.GOLD + "Super Strong")
                .buildArmor(), InvType.ARMOR);

        String line;
        StrBuilder sb = new StrBuilder();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(getDataFolder().toString() + File.separator, "diamond.json"), Charset.defaultCharset())) {
            while ((line = reader.readLine()) !=null){
                 sb.append(line);
            }

        try {
            Files.newBufferedReader(Paths.get(getDataFolder().toString() + File.separator, "diamond.json"), Charset.defaultCharset()).lines().forEach(sb::appendln);
            // reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(sb.toString());
        ainv = gson.fromJson(sb.toString(), ArmorInventory.class);
       Lobby2 lobby2=new Lobby2(lobby.getSpawn(),new BlockPoint("world",34,67,90),new BlockPoint("world",36,78,12));

       Type dtype = new TypeToken<ArmorInventory>() {
        }.getType();
        try {

         // ainv = GsonIO.readFromFile(Paths.get(getDataFolder().toString() + File.separator, "diamond.json"), dtype, ainv);
            GsonIO.writeToFile(Paths.get(getDataFolder().toString() + File.separator, "diamond.json"),ainv,dtype);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(ainv.toString());


        ItemStack[] armor = new ItemStack[4];
        armor[0]=new ItemStack(Material.DIAMOND_BOOTS);
        armor[1]=new ItemStack(Material.DIAMOND_LEGGINGS);
        armor[2]=new ItemStack(Material.DIAMOND_CHESTPLATE);
        armor[3]=new ItemStack(Material.DIAMOND_HELMET);


        ItemStack i1 = new ItemStack(Material.DIAMOND_AXE,1);
        ItemMeta meta = i1.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD +"Super Axe");
        meta.addEnchant(Enchantment.DIG_SPEED,2,true);
        meta.setLore(Arrays.asList(ChatColor.GREEN + "The first", ChatColor.RED + "The second"));
        i1.setItemMeta(meta);


        PlayerInventory inv = (PlayerInventory) Bukkit.createInventory(null, InventoryType.PLAYER);
        inv.setItem(3,i1);
        inv.setItem(22,new ItemStack(Material.GOLD_BOOTS,1));
        inv.setItem(0,new ItemStack(Material.GOLD_CHESTPLATE,1));
        inv.setItem(35,new ItemStack(Material.GOLD_HELMET,1));
        Type bity = new TypeToken<BaseInventory>(){}.getType();

        BaseInventory bi = new BaseInventory();
        bi.setArmor(armor);
        bi.setContents(inv);

      //  GsonIO.readFromFile(Paths.get(getDataFolder().toString() + File.separator, "lobby.json"),lType,lobby2);

        try {
            GsonIO.writeToFile(Paths.get(getDataFolder().toString() + File.separator, "inv.json"),bi,bity);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();

        String json=gson.toJson(bi,bity);

        System.out.println(json);
*/


    }

    @Override
    public void onDisable() {
        SendConsoleMessage.info("Disabling.");
        SendConsoleMessage.info("Saving Settings");
        this.settings.saveConfigs();
        scheduler.shutdown();
        mainExecutor = null;
        register.destroy();
        register = null;
        // lobbyManager.destroy();
        //  lobbyManager=null;
        if (settings.getEnable()) {
            this.endAllRunningDuels();
            this.getFileManager().saveDuelArenas();
            saveConfig();
        }

    }

    public Path getPath(String fileName) {

        return Paths.get(getDataFolder().toString() + File.separator, fileName);
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

    public Msg getMsg() {

        return this.msg;
    }

    public TitleMaker getTitleApi() {

        return titleApi;
    }

    private void startChecks() {

        if (!settings.getEnable())
            getServer().getPluginManager().disablePlugin(this);

        if (settings.getLobbyReady() && lobbyExists()) {
            loadLobby();
            lobbyManager = new LobbyManager(this);
            getServer().getPluginManager().registerEvents(lobbyManager, this);
            SendConsoleMessage.info("Lobby has been loaded");
        }
    }

    public void loadLobby() {
        // this.lobby = (Lobby) getConfig().get("lobby.settings");

        try {

            this.lobby = GsonIO.readFromFile(Paths.get(getDataFolder().toString() + File.separator, "lobby.json"), lobbyType, lobby);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean lobbyExists() {

        return Files.exists(Paths.get(getDataFolder().toString() + File.separator, "lobby.json"));

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
