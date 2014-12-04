package com.relicum.dual.Configuration.Interfaces;

import com.relicum.dual.InvType;
import org.bukkit.inventory.ItemStack;

/**
 * Name: TypedInventory.java Created: 24 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface TypedInventory {

    /**
     * Sets inv contents.
     *
     * @param contents the contents
     * @param type     the type
     */
    public void setInvContents(ItemStack[] contents, InvType type);

    /**
     * Get inv contents.
     *
     * @param type the type
     * @return the contents as ItemStack[]
     */
    public ItemStack[] getInvContents(InvType type);


}
