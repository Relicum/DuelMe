package com.relicum.dual.Events;

import com.relicum.dual.Player.GamePlayer;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * GamePlayerEvent base event what all Game Player extend extend from.
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
public class GamePlayerEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;
    private GamePlayer gamePlayer;

    public GamePlayerEvent(Player who, GamePlayer gamePlayer) {
        super(who);
        this.gamePlayer = gamePlayer;

    }

    public GamePlayerEvent(Player who) {
        super(who);

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
