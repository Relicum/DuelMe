package com.relicum.dual.Configuration;

import com.relicum.ipsum.Location.SpawnPoint;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.Validate;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Name: PlayerSettings.java Created: 15 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
@AllArgsConstructor
public class PlayerProperties {

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
    private PlayersInventory playersInventory;
    private SpawnPoint reSpawn;


    /**
     * Instantiates a new Player settings object.
     * <p>This is used to save different settings for a player, one the settings he had before he started the game.
     * <p>Various settings for different parts of the game or variations of the game. For each set of settings you create a new instance of this object.
     * <p>The only set that is unique is the set that stores the players pre game settings.
     */
    public PlayerProperties() {
        this.playersInventory = new PlayersInventory();
    }

    public PlayerProperties(PlayerProperties properties) {
        playersInventory = properties.playersInventory;
        health = properties.health;
        food = properties.food;
        saturation = properties.saturation;
        exhaustion = properties.exhaustion;
        gameMode = properties.gameMode;
        experience = properties.experience;
        level = properties.level;
        exp = properties.exp;
        fireTicks = properties.fireTicks;
        flying = properties.flying;
        allowedFlying = properties.allowedFlying;
        walkSpeed = properties.walkSpeed;
        flySpeed = properties.flySpeed;
        reSpawn = properties.reSpawn;

    }

    /**
     * Apply settings to the current player.
     *
     * @param player the {@link org.bukkit.entity.Player} to apply the settings to.
     */
    public void applySettings(Player player) {

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

    public void applyInventory(Player player) {
        Validate.isTrue((playersInventory.getUuid().equals(player.getUniqueId().toString())), "Players UUID and the one stored for this inventory do not match. Contact developer");

        player.getInventory().clear();
        player.getInventory().setArmorContents(new ItemStack[4]);

        player.getInventory().setContents(playersInventory.getInvContents());
        player.getInventory().setArmorContents(playersInventory.getArmor());

    }

    public void saveInventory(PlayerInventory inv) {
        if (playersInventory == null)
            this.playersInventory = new PlayersInventory();

        playersInventory.setInventory(inv);

    }

    /**
     * Save player settings.
     *
     * @param player the {@link org.bukkit.entity.Player} to save the settings for.
     */
    public void saveSettings(Player player) {

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
        reSpawn = new SpawnPoint(player.getWorld().getName(), player.getLocation().getBlockX(), player.getLocation().getBlockY(), player.getLocation().getBlockZ());
    }

    public SpawnPoint getReSpawn() {

        return reSpawn;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayerProperties{");
        sb.append("health=").append(health);
        sb.append(", food=").append(food);
        sb.append(", saturation=").append(saturation);
        sb.append(", exhaustion=").append(exhaustion);
        sb.append(", gameMode='").append(gameMode).append('\'');
        sb.append(", experience=").append(experience);
        sb.append(", level=").append(level);
        sb.append(", exp=").append(exp);
        sb.append(", fireTicks=").append(fireTicks);
        sb.append(", flying=").append(flying);
        sb.append(", allowedFlying=").append(allowedFlying);
        sb.append(", walkSpeed=").append(walkSpeed);
        sb.append(", flySpeed=").append(flySpeed);
        sb.append(", playersInventory=").append(playersInventory.toString());
        sb.append(", reSpawn=").append(reSpawn);
        sb.append('}');
        return sb.toString();
    }
}
