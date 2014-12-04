package com.relicum.dual;

import com.relicum.dual.Configuration.Interfaces.IPlayerInventory;
import com.relicum.dual.Configuration.Interfaces.TypedInventory;
import com.teozcommunity.teozfrank.duelme.main.DuelMe;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

/**
 * GameInventory stores all the inventories a player might need.
 *
 * @author Relicum
 * @version 0.0.1
 */
public class GameInventory implements IPlayerInventory, TypedInventory {


    private Map<String, Map<String, ItemStack[]>> inventoryMap;
    private String current;

    /**
     * Instantiates a new Game inventory.
     */
    public GameInventory() {

        this.inventoryMap = new HashMap<>();
        current = InvType.PLAYER.name();

    }

    public Player apply(InvType type, Player player, boolean saveToDisk) {

        if (current.equalsIgnoreCase(type.name()) && !current.equalsIgnoreCase("PLAYER"))
            return player;

        if (type.name().equalsIgnoreCase("PLAYER")) {
            setInventory(player.getInventory());
            setContents(player.getInventory().getContents(), type);
            setArmor(player.getInventory().getArmorContents(), type);


            new BukkitRunnable() {

                int count = 1;

                @Override
                public void run() {

                    if (count > 4) {
                        System.out.println("GameInventory");
                        return;
                    }

                    if (count == 1) {

                        if (type.name().equalsIgnoreCase("PLAYER")) {
                            setInventory(player.getInventory());
                        } else {
                            setContents(player.getInventory().getContents(), type);
                            setArmor(player.getInventory().getArmorContents(), type);
                        }

                    }

                    if (count == 2) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(new ItemStack[4]);
                    }
                    if (count == 3) {
                        player.getInventory().setArmorContents(getArmor(type));
                        player.getInventory().setContents(getInvContents(type));
                    }
                    if (count == 4) {
                        player.updateInventory();
                    }
                    count++;
                }
            }.runTaskTimer(DuelMe.getInstance(), 0l, 1l);
        }
        return player;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInventory(PlayerInventory inventory) {
        if (!inventoryMap.containsKey(InvType.PLAYER.name())) {
            setNewInvType(InvType.PLAYER);
        }


        //String[] inv = InventoryFactory.playerInventoryToBase64(inventory);
        getInvType(InvType.PLAYER).put("contents", convertContents(inventory.getContents()));
        setArmor(convertArmor(inventory.getArmorContents()));

    }

    @Override
    public ItemStack[] getArmor() {
        return new ItemStack[0];
    }

    @Override
    public void setArmor(ItemStack[] armor) {
        getInvType(InvType.PLAYER).put("armor", armor);
    }

    private ItemStack[] convertArmor(ItemStack[] toConvert) {
        ItemStack[] ta = new ItemStack[4];
        int count = 0;
        for (ItemStack stack : toConvert) {
            ta[count] = stack;
            count++;
        }

        return ta;
    }

    private ItemStack[] convertContents(ItemStack[] toConvert) {
        ItemStack[] tc = new ItemStack[36];
        int count = 0;
        for (ItemStack stack : toConvert) {
            tc[count] = stack;
            count++;
        }

        return tc;
    }

    /**
     * Set contents for inventory for the specified type
     *
     * @param inventory the inventory
     * @param type      the {@link com.relicum.dual.InvType}
     */
    public void setContents(ItemStack[] inventory, InvType type) {
        if (!inventoryMap.containsKey(type.name())) {
            setNewInvType(type);
        }
        getInvType(type).put("contents", inventory);

    }

    /**
     * Set armor for inventory for the specified type
     *
     * @param theArmor the the armor
     * @param type     the {@link com.relicum.dual.InvType}
     */

    public void setArmor(ItemStack[] theArmor, InvType type) {
        if (!inventoryMap.containsKey(type.name())) {
            setNewInvType(type);
        }
        getInvType(type).put("armor", theArmor);

    }


    public ItemStack[] getArmor(InvType type) {

        return getInvType(type).get("armor");
    }

/*
    public ItemStack[] getInvContents(InvType type) {

        return getInvType(type).get("contents");
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack[] getInvContents() {
        throw new UnsupportedOperationException("Not Supported in this implementation");

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInvContents(ItemStack[] contents) {

    }


    /**
     * Sets Inventory Type, either its a player which is unique to the player or its a game inventory which all users could use.
     *
     * @param invType the type of Inventory. {@link com.relicum.dual.InvType}
     */
    public void setNewInvType(InvType invType) {

        Map<String, ItemStack[]> tmp = new HashMap<>(2);
        tmp.put("armor", new ItemStack[4]);
        tmp.put("contents", new ItemStack[36]);
        this.inventoryMap.put(invType.name(), tmp);


    }


    /**
     * Gets the inventory type.
     *
     * @param type the type of inventory.
     * @return the {@link com.relicum.dual.InvType}
     */
    public Map<String, ItemStack[]> getInvType(InvType type) {
        if (!inventoryMap.containsKey(type.name()))
            System.out.println("Error could not find the key " + type.name());

        return this.inventoryMap.get(type.name());
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("GameInventory{");
        sb.append("inventoryMap=").append(inventoryMap);
        sb.append('}');
        return sb.toString();
    }

    public void updateCurrent(InvType type) {

        this.current = type.name();
    }

    public InvType getCurrent() {

        return InvType.valueOf(current);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setInvContents(ItemStack[] contents, InvType type) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ItemStack[] getInvContents(InvType type) {
        return new ItemStack[0];
    }
}
