package com.relicum.dual.Configuration;

import com.relicum.dual.lobby.PlayerLobbyData;
import com.relicum.ipsum.io.GsonIO;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.libs.com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * Name: PlayerSettingsManager.java Created: 19 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
public class PlayerSettingsLoader {

    private Map<UUID, PlayerProperties> playerProperties = null;

    private PlayerLobbyData lobbyData = null;

    private final DuelMe plugin;
    @Setter
    private String path;
    @Setter
    private String lobbyDataFilename;

    public String tmpLobbyData;
    public Type lobbyType = new TypeToken<PlayerLobbyData>() {
    }.getType();

    public PlayerSettingsLoader(DuelMe plugin) {
        this.plugin = plugin;
        this.playerProperties = new HashMap<>();
        this.path = plugin.getDataFolder().toString() + File.separator + "players" + File.separator;
        this.lobbyDataFilename = "lobbyData.json";

        if (!Files.exists(Paths.get(path, lobbyDataFilename))) {
            try {
                System.out.println("Player Dir does not exist so makeing it");
                Files.createDirectory(Paths.get(path));
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("No file found creating new");
            this.lobbyData = getPlayerLobbyData();
            writeToLobbyFile();

        } else {
            System.out.println("File found trying to load it");
            readLobbyFil();

        }


    }

    public void writeToFile(PlayerSettings defaultSetting, String filename) {

        plugin.getScheduler().execute(new SettingsWriter(defaultSetting, Paths.get(path, filename)));
    }

    public void writeToLobbyFile() {

        try {
            GsonIO.writeToFile(Paths.get(path, lobbyDataFilename), lobbyData, lobbyType);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readLobbyFil() {

        try {
            lobbyData = GsonIO.readFromFile(Paths.get(path, lobbyDataFilename), lobbyType, lobbyData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayerLobbyData readLobbyFile() {
        Callable<PlayerLobbyData> callable = new SettingsReader<>(Paths.get(path, lobbyDataFilename));
        try {
            return plugin.getScheduler().submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;

    }

    public PlayerProperties readFromFile(String filename) {
        Callable<PlayerProperties> callable = new SettingsReader<>(Paths.get(path, filename));
        try {
            return plugin.getScheduler().submit(callable).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    private PlayerLobbyData getPlayerLobbyData() {

        return new PlayerLobbyData(20.0d, 20, 5.0f, 0.2f, GameMode.ADVENTURE.name(), 0, 0, 0.0f, 0, false, false, 0.2f, 0.1f);

    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayerSettingsLoader{");
        sb.append("playerProperties=").append(playerProperties);
        sb.append(", lobbyData=").append(lobbyData);
        sb.append(", path='").append(path).append('\'');
        sb.append(", lobbyDataFilename='").append(lobbyDataFilename).append('\'');
        sb.append(", tmpLobbyData='").append(tmpLobbyData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
