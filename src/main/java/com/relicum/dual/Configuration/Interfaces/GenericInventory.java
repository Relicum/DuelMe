package com.relicum.dual.Configuration.Interfaces;

import org.bukkit.inventory.ItemStack;

/**
 * GenericInventory is the base interface for getting an Inventory that's applied to a player.
 * <p>How they are created and stored is not specified here to allow for different mechanisms.
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface GenericInventory {


    /**
     * Get main convents of an inventory.
     *
     * @return the player contents Inventory
     */
    public ItemStack[] getInvContents();

    /**
     * Sets the contents of the inventory
     *
     * @param contents the inventory contents
     */
    public void setInvContents(ItemStack[] contents);
}
