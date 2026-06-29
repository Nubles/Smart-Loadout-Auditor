package com.smartloadout;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LoadoutSnapshot
{
    private final Map<Integer, Integer> inventory;
    private final Map<Integer, Integer> equipment;
    private final Map<Integer, Integer> runePouch;
    private final String spellbook;
    private final String attackStyle;
    private final int wildernessLevel;

    public LoadoutSnapshot(
        Map<Integer, Integer> inventory,
        Map<Integer, Integer> equipment,
        Map<Integer, Integer> runePouch,
        String spellbook,
        String attackStyle,
        int wildernessLevel)
    {
        this.inventory = inventory == null ? new HashMap<Integer, Integer>() : new HashMap<>(inventory);
        this.equipment = equipment == null ? new HashMap<Integer, Integer>() : new HashMap<>(equipment);
        this.runePouch = runePouch == null ? new HashMap<Integer, Integer>() : new HashMap<>(runePouch);
        this.spellbook = spellbook == null ? "" : spellbook;
        this.attackStyle = attackStyle == null ? "" : attackStyle;
        this.wildernessLevel = wildernessLevel;
    }

    public int countInventoryItems(Set<Integer> itemIds)
    {
        int total = 0;
        for (int itemId : itemIds)
        {
            total += quantity(inventory, itemId);
        }
        return total;
    }

    public boolean hasInventoryOrEquipmentItem(Set<Integer> itemIds)
    {
        for (int itemId : itemIds)
        {
            if (quantity(inventory, itemId) > 0 || quantity(equipment, itemId) > 0)
            {
                return true;
            }
        }
        return false;
    }

    public boolean hasEquippedItem(Set<Integer> itemIds)
    {
        for (int itemId : itemIds)
        {
            if (quantity(equipment, itemId) > 0)
            {
                return true;
            }
        }
        return false;
    }

    public int countRunePouchItems(Set<Integer> itemIds)
    {
        int total = 0;
        for (int itemId : itemIds)
        {
            total += quantity(runePouch, itemId);
        }
        return total;
    }

    public Set<Integer> presentInventoryItems(Set<Integer> itemIds)
    {
        Set<Integer> present = new HashSet<>();
        for (int itemId : itemIds)
        {
            if (quantity(inventory, itemId) > 0)
            {
                present.add(itemId);
            }
        }
        return present;
    }

    public Map<Integer, Integer> getInventory()
    {
        return Collections.unmodifiableMap(inventory);
    }

    public Map<Integer, Integer> getEquipment()
    {
        return Collections.unmodifiableMap(equipment);
    }

    public String getSpellbook()
    {
        return spellbook;
    }

    public String getAttackStyle()
    {
        return attackStyle;
    }

    public int getWildernessLevel()
    {
        return wildernessLevel;
    }

    private int quantity(Map<Integer, Integer> items, int itemId)
    {
        Integer quantity = items.get(itemId);
        return quantity == null ? 0 : quantity;
    }
}
