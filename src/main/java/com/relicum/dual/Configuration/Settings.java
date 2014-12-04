package com.relicum.dual.Configuration;

import lombok.Getter;
import lombok.Setter;
import net.cubespace.Yamler.Config.*;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;

/**
 * Name: Settings.java Created: 11 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class Settings extends Config {

    @Path("duelme")
    private ConfigSection duelme = new ConfigSection();

    @Comment("Stores the default settings")
    @Path("duelme.defaults")
    private HashMap<String, Object> defaults = new HashMap<>();

    @Getter
    @Setter
    @Path("duelme.defaults.enable")
    private Boolean enable = true;

    @Getter
    @Setter
    @Path("duelme.defaults.setupDone")
    private Boolean setupDone = false;

    @Getter
    @Setter
    @Path("duelme.defaults.useLobby")
    private Boolean useLobby = true;

    @Getter
    @Setter
    @Path("duelme.defaults.lobbyReady")
    private Boolean lobbyReady = false;
    @Getter
    @Setter
    @Path("duelme.defaults.joinBySign")
    private Boolean joinBySign = false;
    @Getter
    @Setter
    @Path("duelme.defaults.joinByCmd")
    private Boolean joinByCmd = true;
    @Getter
    @Setter
    @Path("duelme.defaults.canJoin")
    private Boolean canJoin = false;

    @Getter
    @Setter
    @Comments({"Auto Join will auto join then to the lobby on login", "This is used if the server is dedicated to the gamemode"})
    @Path("duelme.defaults.autoJoin")
    private Boolean autoJoin = false;

    @Getter
    @Setter
    @Comments({"Set to true to save a players inventory", "before joining and restoring it when they leave"})
    @Path("duelme.defaults.saveInv")
    private Boolean saveInv = true;

    @Getter
    @Setter
    @Comments({"Should we save players settings and inventory", "To hard disk in case of server crashes"})
    @Path("duelme.defaults.saveInvToDisk")
    private Boolean saveInvToDisk = true;

    @Getter
    @Setter
    @Path("duelme.defaults.adminMode")
    private Boolean adminMode = false;

    @Getter
    @Setter
    @Path("duelme.defaults.titleOnJoin")
    private Boolean titleOnJoin = false;

    @Getter
    @Setter
    @Comments({"When a player leaves 1v1 return them to", "the same location that they joined from.", "If false will return them to the current world spawn"})
    @Path("duelme.defaults.lastDestination")
    private Boolean lastDestination = true;

    @Getter
    @Setter
    @Path("duelme.defaults.bossOnJoin")
    private Boolean bossOnJoin = false;

    @Getter
    @Setter
    @Path("duelme.defaults.arenaSetup")
    private Boolean arenaSetup = false;


    public Settings(Plugin plugin, String name) {

        CONFIG_FILE = new File(plugin.getDataFolder(), File.separator + name + ".yml");
        CONFIG_HEADER = new String[]{"DuelMe Plugin Settings File"};
    }

    public void doInit() {

        try {
            this.init();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveConfigs() {

        try {
            this.save();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfigs() {

        try {
            this.reload();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
