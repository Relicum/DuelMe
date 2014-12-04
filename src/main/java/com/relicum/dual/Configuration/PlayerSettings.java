package com.relicum.dual.Configuration;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: PlayerSettings.java Created: 19 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayerSettings {

    public enum PROPERTIES_TYPE {
        PLAYER,
        LOBBY,
        WOODEN

    }

    private Map<String, PlayerProperties> playerProperties;

    public PlayerSettings() {

        this.playerProperties = new HashMap<>();
    }

    public void addPropertyType(PROPERTIES_TYPE type, PlayerProperties properties) {

        this.playerProperties.putIfAbsent(type.name(), properties);
    }

    public PlayerProperties getPlayerProperties(PROPERTIES_TYPE type) {

        return playerProperties.get(type.name());
    }

    public Player savePlayerProperties(PROPERTIES_TYPE type, Player player) {

        getPlayerProperties(type).saveSettings(player);

        return player;
    }

    public Player applyPlayerProperties(PROPERTIES_TYPE type, Player player) {

        getPlayerProperties(type).applySettings(player);

        return player;
    }

    public void duplicateProperties(PROPERTIES_TYPE copy, PROPERTIES_TYPE paste) {

        playerProperties.putIfAbsent(paste.name(), new PlayerProperties(getPlayerProperties(copy)));
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayerSettings{");
        sb.append("playerProperties=").append(playerProperties);
        sb.append('}');
        return sb.toString();
    }
}
