package com.relicum.dual.Configuration;

import com.relicum.dual.Configuration.Interfaces.TypedArmorStack;
import com.relicum.dual.InvType;
import com.relicum.ipsum.Items.ArmorItems;
import org.apache.commons.lang.Validate;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

/**
 * ArmorInventory store and create different armor inventories.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class ArmorInventory implements TypedArmorStack {

    private ItemStack[] armor;
    private String type;

    public ArmorInventory() {
    }

    public ArmorInventory(ItemStack[] armor, InvType type) {
        checkInitialized();
        convertArmor(armor);
        this.type = type.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setArmor(ItemStack[] armor, InvType type) {
        checkInitialized();
        convertArmor(armor);
        this.type = type.name();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack[] getArmor() {
        return armor;
    }

    private void checkInitialized() {
        if (armor == null)
            this.armor = new ItemStack[4];
    }

    private void convertArmor(ItemStack[] toConvert) {

        int count = 0;
        for (ItemStack stack : toConvert) {
            if (stack != null)
                this.armor[count] = new ItemStack(stack);

            count++;
        }

    }

    /**
     * Set boots.
     *
     * @param boots the armor boots
     * @return instance of itself for chaining.
     */
    public ArmorInventory setBoots(ItemStack boots) {
        Validate.isTrue((ArmorItems.find(boots.getType().name()) || boots.getType().name().contains("BOOTS")), "The ItemStack is not an armor boot");
        checkInitialized();
        this.armor[0] = new ItemStack(boots);
        return this;

    }

    /**
     * Set leggings.
     *
     * @param leggings the armor leggings
     * @return instance of itself for chaining.
     */
    public ArmorInventory setLeggings(ItemStack leggings) {
        Validate.isTrue((ArmorItems.find(leggings.getType().name()) || leggings.getType().name().contains("LEGGINGS")), "The ItemStack is not an armor legging");
        checkInitialized();
        this.armor[1] = new ItemStack(leggings);
        return this;

    }

    /**
     * Set chest plate.
     *
     * @param chestPlate the armor chest plate
     * @return instance of itself for chaining.
     */
    public ArmorInventory setChestPlate(ItemStack chestPlate) {
        Validate.isTrue((ArmorItems.find(chestPlate.getType().name()) || chestPlate.getType().name().contains("CHESTPLATE")), "The ItemStack is not an armor chestplate");
        checkInitialized();
        this.armor[2] = new ItemStack(chestPlate);
        return this;

    }

    /**
     * Set helmet.
     *
     * @param helmet the armor helmet
     * @return instance of itself for chaining.
     */
    public ArmorInventory setHelmet(ItemStack helmet) {
        Validate.isTrue((ArmorItems.find(helmet.getType().name()) || helmet.getType().name().contains("HELMET")), "The ItemStack is not an armor helmet");
        checkInitialized();
        this.armor[3] = new ItemStack(helmet);
        return this;

    }


    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(InvType type) {
        this.type = type.name();
    }


    /**
     * Gets type.
     *
     * @return the type
     */
    public InvType getType() {
        return InvType.valueOf(type);
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ArmorInventory{");
        sb.append("armor=").append(armor == null ? "null" : Arrays.asList(armor).toString());
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
