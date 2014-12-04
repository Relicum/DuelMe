package com.relicum.dual.Configuration.Interfaces;

/**
 * The interface Health settings.
 */
public interface HealthSettings {

    /**
     * Sets player health.
     *
     * @param health the health of the player
     */
    public void setHealth(double health);

    /**
     * Sets player food level.
     *
     * @param food the food left to set the player to.
     */
    public void setFood(int food);

    /**
     * Sets players saturation.
     *
     * @param saturation the saturation to set the player to.
     */
    public void setSaturation(float saturation);

    /**
     * Sets player exhaustion.
     *
     * @param exhaustion the exhaustion to set the player to.
     */
    public void setExhaustion(float exhaustion);

    /**
     * Gets player health.
     *
     * @return the health of the player.
     */
    public double getHealth();

    /**
     * Gets player food level.
     *
     * @return the food level of the player.
     */
    public int getFood();

    /**
     * Gets player saturation.
     *
     * @return the saturation of the player.
     */
    public float getSaturation();

    /**
     * Gets player exhaustion.
     *
     * @return the exhaustion of the player.
     */
    public float getExhaustion();
}
