package com.relicum.dual.Configuration;

import com.relicum.dual.GameInventory;

import java.util.HashMap;
import java.util.Map;

/**
 * Name: InventoryStore.java Created: 20 November 2014
 *
 * @author Relicum
 * @version 0.0.1
 */
public class InventoryStore {

    private Map<String, GameInventory> inventoryStore;

    public InventoryStore() {

        this.inventoryStore = new HashMap<>();
    }

    public GameInventory addPlayersInventories(String uuid, GameInventory inventory) {

        return this.inventoryStore.putIfAbsent(uuid, inventory);
    }

    public GameInventory getPlayerInventories(String uuid) {

        return this.inventoryStore.get(uuid);
    }


}
