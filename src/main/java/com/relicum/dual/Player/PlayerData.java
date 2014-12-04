package com.relicum.dual.Player;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

/**
 * PlayerData stores a snapshot of the players settings, which can be re applied if they leave the game.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayerData {

    private double health;
    private int food;
    private float saturation;
    private float exhaustion;
    private String gameMode;
    private int experience;
    private int level;
    private float exp;
    private int fireTicks;
    private boolean flying;
    private boolean allowedFlying;
    private float walkSpeed;
    private float flySpeed;

    public PlayerData() {
    }

    /**
     * Apply the stored settings back to the player.
     *
     * @param player the player to apply their stored settings to.
     */
    public void apply(Player player) {

        player.setHealth(health);
        player.setFoodLevel(food);
        player.setSaturation(saturation);
        player.setExhaustion(exhaustion);
        player.setGameMode(GameMode.valueOf(gameMode));
        player.setTotalExperience(experience);
        player.setLevel(level);
        player.setExp(exp);
        player.setFireTicks(fireTicks);
        player.setFlying(flying);
        player.setAllowFlight(allowedFlying);
        player.setWalkSpeed(walkSpeed);
        player.setFlySpeed(flySpeed);

    }

    /**
     * Save the players settings before they join the lobby
     *
     * @param player the player to save their settings for
     */
    public void save(Player player) {
        health = player.getHealth();
        food = player.getFoodLevel();
        saturation = player.getSaturation();
        exhaustion = player.getExhaustion();
        gameMode = player.getGameMode().name();
        experience = player.getTotalExperience();
        level = player.getLevel();
        exp = player.getExp();
        fireTicks = player.getFireTicks();
        flying = player.isFlying();
        allowedFlying = player.getAllowFlight();
        walkSpeed = player.getWalkSpeed();
        flySpeed = player.getFlySpeed();

    }

}
