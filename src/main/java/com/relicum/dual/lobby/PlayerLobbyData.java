package com.relicum.dual.lobby;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * PlayerLobbyData
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayerLobbyData {

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

    public PlayerLobbyData() {

    }

    public PlayerLobbyData(double health, int food, float saturation, float exhaustion,
                           String gameMode, int experience, int level, float exp, int fireTicks,
                           boolean flying, boolean allowedFlying, float walkSpeed, float flySpeed) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
        this.exhaustion = exhaustion;
        this.gameMode = gameMode;
        this.experience = experience;
        this.level = level;
        this.exp = exp;
        this.fireTicks = fireTicks;
        this.flying = flying;
        this.allowedFlying = allowedFlying;
        this.walkSpeed = walkSpeed;
        this.flySpeed = flySpeed;
    }

    /**
     * Apply lobby settings to the current player, if removeInventory is set to true it will clear their inventory
     *
     * @param player the {@link org.bukkit.entity.Player} to apply the lobby settings to.
     */
    public void apply(Player player, boolean removeInventory) {

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

        if (removeInventory) {

            player.getInventory().setArmorContents(new ItemStack[4]);
            player.getInventory().clear();
            player.updateInventory();
        }
    }

    public void destroy() {

        this.health = Double.parseDouble(null);
        this.food = Integer.parseInt(null);
        this.saturation = Float.parseFloat(null);
        this.exhaustion = Float.parseFloat(null);
        this.gameMode = null;
        this.experience = Integer.parseInt(null);
        this.level = Integer.parseInt(null);
        this.exp = Float.parseFloat(null);
        this.fireTicks = Integer.parseInt(null);
        this.flying = Boolean.parseBoolean(null);
        this.allowedFlying = Boolean.parseBoolean(null);
        this.walkSpeed = Float.parseFloat(null);
        this.flySpeed = Float.parseFloat(null);

    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayerLobbyData{");
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
        sb.append('}');
        return sb.toString();
    }
}
