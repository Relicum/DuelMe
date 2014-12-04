package com.relicum.dual.Configuration.Interfaces;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Name: IPlayerInventory.java Created: 24 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface IPlayerInventory extends GenericInventory {

    /**
     * Set Player Inventory which is first serialised into a ymal format and then encode in base64 and started as a simple string.
     *
     * @param inventory the inventory
     */
    public void setInventory(PlayerInventory inventory);

    /**
     * Get an ItemStack array representing the armour slots in a players inventory.
     *
     * @return the armor ItemStack[]
     */
    public ItemStack[] getArmor();

    /**
     * Sets the armor
     *
     * @param armor the armor as a ItemStack[]
     */
    public void setArmor(ItemStack[] armor);
}
