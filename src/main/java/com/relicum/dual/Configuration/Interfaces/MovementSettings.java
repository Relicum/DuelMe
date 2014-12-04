package com.relicum.dual.Configuration.Interfaces;

/**
 * The interface Movement settings.
 */
public interface MovementSettings {

    /**
     * Sets flying.
     *
     * @param flying set true to set them flying
     */
    public void setFlying(boolean flying);

    /**
     * Sets allowed fly.
     *
     * @param allowed set true to give them permission to fly
     */
    public void setAllowedFly(boolean allowed);

    /**
     * Sets walk speed.
     *
     * @param walkSpeed the walk speed
     */
    public void setWalkSpeed(float walkSpeed);

    /**
     * Sets fly speed.
     *
     * @param flySpeed the fly speed
     */
    public void setFlySpeed(float flySpeed);

    /**
     * Gets flying.
     *
     * @return if player is flying or not
     */
    public boolean getFlying();

    /**
     * Gets allowed fly.
     *
     * @return returns if the player is allowed to fly
     */
    public boolean getAllowedFly();

    /**
     * Gets walk speed.
     *
     * @return the player walk speed
     */
    public float getWalkSpeed();

    /**
     * Gets fly speed.
     *
     * @return the player fly speed
     */
    public float getFlySpeed();
}
