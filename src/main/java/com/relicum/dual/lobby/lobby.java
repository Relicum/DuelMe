package com.relicum.dual.lobby;

import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Location.SpawnPoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Name: lobby.java Created: 08 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@SerializableAs("Lobby")
public class Lobby implements ConfigurationSerializable {
    @Setter
    @Getter
    private Region region;
    @Getter
    @Setter
    private SpawnPoint spawn;

    public Lobby(Region region, SpawnPoint spawn) {
        this.region = region;
        this.spawn = spawn;
    }


    public static Lobby deserialize(Map<String, Object> map) {

        return new Lobby((Region) map.get("region"), (SpawnPoint) map.get("spawnpoint"));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("region", region);
        map.put("spawnpoint", spawn);
        return map;
    }
}
