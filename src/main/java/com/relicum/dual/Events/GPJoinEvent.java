package com.relicum.dual.Events;

import com.relicum.ipsum.Location.SpawnPoint;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * Name: GPJoinEvent.java Created: 16 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
public class GPJoinEvent extends GamePlayerEvent {

    private SpawnPoint joinedFrom;

    public GPJoinEvent(Player who, SpawnPoint joinedFrom) {
        super(who);
        this.joinedFrom = joinedFrom;

    }
}
