package com.relicum.dual.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * PlayerJoinLobby called when player is being tp'd to the lobby.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayerJoinLobby extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private final Player player;

    public PlayerJoinLobby(Player player) {
        this.player = player;

    }

    /**
     * Gets player.
     *
     * @return the player joining the lobby
     */
    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {

        return handlers;

    }


    public static HandlerList getHandlerList() {

        return handlers;

    }

    @Override
    public boolean isCancelled() {

        return cancelled;

    }

    @Override
    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;

    }
}
