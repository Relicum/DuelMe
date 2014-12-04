package com.relicum.dual.lobby;

import com.relicum.dual.Events.GPJoinEvent;
import com.relicum.dual.Events.GPLeaveLobbyEvent;
import com.relicum.dual.Player.GamePlayer;
import com.relicum.ipsum.Effect.Game.Region;
import com.relicum.ipsum.Location.SpawnPoint;
import com.relicum.ipsum.Utils.Msg;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * LobbyManager controls everything connected to running and managing the lobby.
 * <p>Essentially the lobby manager is central to the entire game as players are either join from outside, join after gains, leaving to be put in game and leaving the game completely.
 *
 * @author Relicum
 * @version 0.0.1
 */
@Getter
public class LobbyManager implements Listener {


    private DuelMe plugin;

    private ConcurrentHashMap<String, GamePlayer> players;

    private ScheduledFuture<?> chunkTask;


    @Setter
    private SpawnPoint lobbySpawn;
    @Setter
    private Region region;
    @Setter
    private boolean joinAble = false;
    @Setter
    private boolean ready = false;

    private Msg msg;

    public LobbyManager(DuelMe plugin) {
        this.plugin = plugin;
        this.msg = plugin.getMsg();
        setUp();
    }

    private void setUp() {
        setReady(plugin.getSettings().getLobbyReady());
        setJoinAble(plugin.getSettings().getCanJoin());
        if (!isJoinAble()) {
            getMsg().logFormatted(Level.INFO, "The lobby setting canJoin is set to false.");
            destroy();
            return;
        }

        players = new ConcurrentHashMap<>(16, 90, 1);
        getMsg().logFormatted(Level.INFO, "Concurrent Player HashMap Initialized");

        chunkTask = startChunkLoader();
        getMsg().logFormatted(Level.INFO, "Initialized lobby spawn chunk loader task");

        getMsg().logFormatted(Level.INFO, "The lobby manager loaded");


    }

    private ScheduledFuture<?> startChunkLoader() {

        return plugin.getScheduler().runRepeatedly(new Runnable() {
            @Override
            public void run() {

                plugin.getMainExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        getMsg().log(Level.INFO, "Checking the lobby spawn chunks are loaded");
                        if (!getLobbySpawn().toLocation().getChunk().isLoaded()) {
                            getLobbySpawn().toLocation().getChunk().load();
                            getMsg().log(Level.INFO, "Lobby Spawn Chunks loaded");
                        }
                    }
                });
            }
        }, 30l, 300l, TimeUnit.SECONDS);

    }

    public boolean stopChunkLoader() {

        if (chunkTask.cancel(true)) {
            getMsg().logFormatted(Level.INFO, "Successfully stopped Lobby Chunk Loader");
            return true;
        } else {
            getMsg().logFormatted(Level.SEVERE, "Error failed to stop Lobby Chunk Loader");
            return false;

        }

    }

    /**
     * Remove {@link com.relicum.dual.Player.GamePlayer} from the map, you still need to close down the returned GamePlayer Object.
     *
     * @param player the {@link org.bukkit.entity.Player} to remove from the map.
     * @return the associated {@link com.relicum.dual.Player.GamePlayer} object, or null if there was nothing to remove.
     */
    public GamePlayer removePlayer(Player player) {

        return players.remove(player.getUniqueId().toString());
    }

    /**
     * Add a new {@link org.bukkit.entity.Player} to the map.
     *
     * @param player the {@link org.bukkit.entity.Player} to add to the map.
     * @return the players instance of the {@link com.relicum.dual.Player.GamePlayer} object.
     */
    public GamePlayer addPlayer(Player player) {

        return players.putIfAbsent(player.getUniqueId().toString(), new GamePlayer(player, getPlugin()));
    }

    @EventHandler
    public void gpJoin(GPJoinEvent e) {

        if (!isJoinAble()) {

            getMsg().sendErrorMessage(e.getPlayer(), "Error: Currently the 1-V-1 lobby is not open to players");
            return;
        }

        if (!e.getPlayer().hasPermission("dual.player.join")) {
            getMsg().sendErrorMessage(e.getPlayer(), "Error: You do not have permission to join the 1-V-1 lobby");
            return;
        }
        if (isInLobby(e.getPlayer().getLocation().toVector())) {

            msg.sendErrorMessage(e.getPlayer(), "You can not tp to the lobby as you are already in it");
            return;
        }


        (addPlayer(e.getPlayer())).joinLobby();


        e.getGamePlayer().destroy();

    }

    @EventHandler
    public void onLeave(GPLeaveLobbyEvent e) {

        getMsg().sendMessage(e.getPlayer(), "Hi thanks for coming, come back soon");
    }

    /**
     * Check to see if a layers current location is in the lobby.
     *
     * @param vector the vector to search for in the lobby region.
     * @return true if the vector is found in the arena region, false if it isn't
     */
    public boolean isInLobby(Vector vector) {

        return getRegion().isRegion(vector);

    }

    public void clearArenaAndLobby() {

        for (GamePlayer player : players.values()) {
            player.clearCurrentInventory();
            player.restoreDefaultInventory(false);
            player.teleport(player.getLastLocation());
            player.destroy();
        }

    }

    public void destroy() {
        plugin = null;
        region = null;
        lobbySpawn = null;
        stopChunkLoader();
        getMsg().logFormatted(Level.INFO, "Destroying lobby manager object");

    }

    public void teleportToLobby(GamePlayer gamePlayer) {


    }
}
