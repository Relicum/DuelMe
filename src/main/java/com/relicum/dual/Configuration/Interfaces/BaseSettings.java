package com.relicum.dual.Configuration.Interfaces;

/**
 * The interface Base settings.
 */
public interface BaseSettings {

    /**
     * Sets game mode.
     *
     * @param mode gamemode represented as a string.
     */
    public void setGameMode(String mode);

    /**
     * Sets total player experience.
     *
     * @param experience the experience to set the player to
     */
    public void setTotalExperience(int experience);

    /**
     * Sets fire ticks.
     *
     * @param ticks the number of fire ticks
     */
    public void setFireTicks(int ticks);

    /**
     * Gets game mode.
     *
     * @return the game mode represented in string format
     */
    public String getGameMode();

    /**
     * Gets players total experience.
     *
     * @return the total experience of the player.
     */
    public int getTotalExperience();

    /**
     * Gets fire ticks left for the player.
     *
     * @return the number of fire ticks left.
     */
    public int getFireTicks();

    /**
     * Sets the players current experience points towards the next level.
     * <p>This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @param xp New experience points
     */
    public void setExp(float xp);

    /**
     * Gets the players current experience points towards the next level.
     * <p>This is a percentage value. 0 is "no progress" and 1 is "next level".
     *
     * @return Current experience points
     */
    public float getExp();

    /**
     * Sets the players current experience level.
     *
     * @param level - New experience level.
     */
    public void setLevel(int level);

    /**
     * Gets the players current experience level.
     *
     * @return Current experience level.
     */
    public int getLevel();
}
