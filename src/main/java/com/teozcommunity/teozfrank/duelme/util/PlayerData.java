package com.teozcommunity.teozfrank.duelme.util;

/**
 The MIT License (MIT)

 Copyright (c) 2014 teozfrank

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;

import java.util.*;

//todo.me impliment ConfigurationSerializable so this can be quick written and read from file
@SerializableAs("Player")
public class PlayerData implements ConfigurationSerializable {

    private UUID playerUUID;
    private ItemStack[] armour;
    private ItemStack[] inventory;
    private SerializedLocation location;
    private Float saturation;
    private int foodLevel;
    private int expLevel;
    private double health;

    public PlayerData(UUID playerUUIDIn, ItemStack[] armourIn, ItemStack[] inventoryIn, Location locationIn,
                      Float saturationIn, int foodLevelIn, int expLevelIn, double healthIn) {
        this.playerUUID = playerUUIDIn;
        this.armour = armourIn;
        this.inventory = inventoryIn;
        this.location = new SerializedLocation(locationIn);
        this.saturation = saturationIn;
        this.foodLevel = foodLevelIn;
        this.expLevel = expLevelIn;
        this.health = healthIn;
    }


    public UUID getUUID() {
        return playerUUID;
    }

    public void setUUID(UUID playerUUIDIn) {
        this.playerUUID = playerUUIDIn;
    }

    public ItemStack[] getArmour() {
        return armour;
    }

    public void setArmour(ItemStack[] armour) {
        this.armour = armour;
    }

    public ItemStack[] getInventory() {
        return inventory;
    }

    public void setInventory(ItemStack[] inventory) {
        this.inventory = inventory;
    }

    public Location getLocation() {
        return location.getLocation();
    }

    public void setLocation(Location location) {
        this.location = new SerializedLocation(location);
    }

    public Float getSaturation() {
        return saturation;
    }

    public void setSaturation(Float saturation) {
        this.saturation = saturation;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int hunger) {
        this.foodLevel = foodLevel;
    }

    public int getEXPLevel() {
        return expLevel;
    }

    public void setEXPLevel(int expIn) {
        this.expLevel = expIn;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Gets version of the serialized object.
     *
     * @return the version
     */
    public int getVersion() {
        return 1;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", playerUUID.toString());
        map.put("armour", armour);
        map.put("inventory", inventory);
        map.put("location", location);
        map.put("saturation", saturation);
        map.put("foodLevel", foodLevel);
        map.put("exp", expLevel);
        map.put("health", health);

        return map;
    }

    @SuppressWarnings("all")
    public static PlayerData deserialize(Map<String, Object> map) {

        Object idObj = map.get("uuid"), satObj = map.get("saturation"), foodObj = map.get("foodLevel"), xPobj = map.get("exp"), helObj = map.get("health");

        ItemStack[] ar = ((ArrayList<ItemStack>) map.get("armour")).toArray(new ItemStack[0]);
        ItemStack[] inv = ((ArrayList<ItemStack>) map.get("inventory")).toArray(new ItemStack[0]);
        SerializedLocation loc = (SerializedLocation) map.get("location");

        if (idObj == null || satObj == null || foodObj == null || xPobj == null || helObj == null || loc == null) {
            throw new RuntimeException("Some thing is null deserialzing playerdata");
        }

        return new PlayerData(UUID.fromString((String) idObj), ar, inv, loc.getLocation(), ((Double) satObj).floatValue(), (Integer) foodObj, (Integer) xPobj, (Double) helObj);

    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Player{");
        sb.append("playerUUID=").append(playerUUID);
        sb.append(", armour=").append(armour == null ? "null" : Arrays.asList(armour).toString());
        sb.append(", inventory=").append(inventory == null ? "null" : Arrays.asList(inventory).toString());
        sb.append(", location=").append(location);
        sb.append(", saturation=").append(saturation);
        sb.append(", foodLevel=").append(foodLevel);
        sb.append(", expLevel=").append(expLevel);
        sb.append(", health=").append(health);
        sb.append(", version=").append(getVersion());
        sb.append('}');
        return sb.toString();
    }
}
