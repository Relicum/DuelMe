package com.teozcommunity.teozfrank.duelme.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

/**
 * SerializedLocation hold instances of a Location or a Block Location that is serializable.
 *
 * @author Relicum
 */
@SerializableAs("SerializedLocation")
public class SerializedLocation implements ConfigurationSerializable {

    private double X;
    private double Y;
    private double Z;
    private float yaw = 0.0f;
    private float pitch = 0.0f;
    private String world;


    /**
     * Instantiates a new Serialized location.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     * @param yaw   the yaw
     * @param pitch the pitch
     */
    public SerializedLocation(String world, double x, double y, double z, float yaw, float pitch) {
        X = x;
        Y = y;
        Z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }


    /**
     * Instantiates a new Serialized block location.
     *
     * @param world the world
     * @param x     the x
     * @param y     the y
     * @param z     the z
     */
    public SerializedLocation(String world, double x, double y, double z) {
        X = x;
        Y = y;
        Z = z;
        this.world = world;
    }

    public SerializedLocation(Location location) {
        this.X = location.getBlockX();
        this.Y = location.getBlockY();
        this.Z = location.getBlockZ();
        this.world = location.getWorld().getName();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();

    }


    /**
     * Gets Y.
     *
     * @return Value of Y.
     */
    public double getY() {
        return Y;
    }

    /**
     * Gets pitch.
     *
     * @return Value of pitch.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Gets Z.
     *
     * @return Value of Z.
     */
    public double getZ() {
        return Z;
    }

    /**
     * Gets yaw.
     *
     * @return Value of yaw.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Gets X.
     *
     * @return Value of X.
     */
    public double getX() {
        return X;
    }

    /**
     * Gets world.
     *
     * @return Value of world.
     */
    public String getWorld() {
        return world;
    }


    /**
     * Get location as a {@link org.bukkit.Location} object
     *
     * @return the location
     */
    public Location getLocation() {

        return new Location(Bukkit.getServer().getWorld(world), X, Y, Z, yaw, pitch);

    }

    /**
     * Get {@link org.bukkit.util.Vector} object
     *
     * @return the {@link org.bukkit.util.Vector}
     */
    public Vector getVector() {

        return new Vector(X, Y, Z);
    }

    /**
     * Deserialize serialized location.
     *
     * @param map the map of values
     * @return the serialized location
     */
    public static SerializedLocation deserialize(Map<String, Object> map) {

        Object xObject = map.get("x"), yObject = map.get("y"), zObject = map.get("z"), worldObject = map.get("world"), yawObject = map.get("yaw"), pitchObject = map.get("pitch");
        if (xObject == null || yObject == null || zObject == null || worldObject == null || yawObject == null || pitchObject == null) {
            throw new NullPointerException("Something here is null");
        }

        Double x = (double) xObject, y = (double) yObject, z = (double) zObject;
        Double yaw = (Double) yawObject, pitch = (Double) pitchObject;
        String worldString = worldObject.toString();

        return new SerializedLocation(worldString, x, y, z, yaw.floatValue(), pitch.floatValue());

    }

    /**
     * Serialize map.
     * <p/>
     * <b>This should never be called directly</b>
     *
     * @return the map ready to be serialised
     */
    @Override
    public Map<String, Object> serialize() {

        Map<String, Object> map = new HashMap<>();
        map.put("x", X);
        map.put("y", Y);
        map.put("z", Z);
        map.put("yaw", yaw);
        map.put("pitch", pitch);
        map.put("world", world);

        return map;

    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SerializedLocation{");
        sb.append("X=").append(X);
        sb.append(", Y=").append(Y);
        sb.append(", Z=").append(Z);
        sb.append(", yaw=").append(yaw);
        sb.append(", pitch=").append(pitch);
        sb.append(", world='").append(world).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
