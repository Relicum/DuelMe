package com.relicum.dual.Configuration.Interfaces;

import com.relicum.dual.InvType;
import org.bukkit.inventory.ItemStack;

/**
 * TypedArmorStack
 *
 * @author Relicum
 * @version 0.0.1
 */
public interface TypedArmorStack {


    /**
     * Sets armor.
     *
     * @param armor the armor stack
     * @param type  the type {@link com.relicum.dual.InvType}
     */
    public void setArmor(ItemStack[] armor, InvType type);

    /**
     * Get armor the ItemStack[]
     *
     * @return the ItemStack[]
     */
    public ItemStack[] getArmor();
}
