package com.relicum.dual.Configuration;

import com.relicum.dual.Configuration.Interfaces.IPlayerInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;
import java.util.Map;

/**
 * PlayersInventory Holds and saves a players default inventory and armor.
 * <p>If you do not use the {@link #setInventory(org.bukkit.inventory.PlayerInventory)} method, you just manually set the UUID of the player before saving.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class PlayersInventory implements IPlayerInventory {

    private String uuid;
    private Map<String, ItemStack[]> playerInventory;


    /**
     * Instantiates a new PlayersInventory.
     */
    public PlayersInventory() {
        this.playerInventory = new HashMap<>(2);
        this.playerInventory.put("contents", new ItemStack[36]);
        this.playerInventory.put("armor", new ItemStack[4]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInventory(PlayerInventory inventory) {
        if (uuid == null)
            this.uuid = inventory.getHolder().getUniqueId().toString();
        setArmor(convertArmor(inventory.getArmorContents()));
        setInvContents(convertContents(inventory.getContents()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack[] getArmor() {
        return playerInventory.get("armor");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArmor(ItemStack[] armor) {
        playerInventory.put("armor", armor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack[] getInvContents() {
        return playerInventory.get("contents");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInvContents(ItemStack[] contents) {
        playerInventory.put("contents", contents);
    }

    private ItemStack[] convertArmor(ItemStack[] toConvert) {
        ItemStack[] ta = new ItemStack[4];
        int count = 0;
        for (ItemStack stack : toConvert) {
            if (stack != null)
                ta[count] = new ItemStack(stack);

            count++;
        }

        return ta;
    }

    private ItemStack[] convertContents(ItemStack[] toConvert) {
        ItemStack[] tc = new ItemStack[36];
        int count = 0;
        for (ItemStack stack : toConvert) {
            if (stack != null)
                tc[count] = new ItemStack(stack);
            count++;
        }

        return tc;
    }


    /**
     * Gets uuid.
     *
     * @return Value of uuid.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets new uuid.
     *
     * @param uuid New value of uuid.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PlayersInventory{");
        sb.append("uuid='").append(uuid).append('\'');
        sb.append(", playerInventory=").append(playerInventory);
        sb.append('}');
        return sb.toString();
    }
}
