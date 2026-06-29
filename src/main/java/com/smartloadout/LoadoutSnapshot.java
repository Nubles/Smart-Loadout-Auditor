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
        this.inventory = new HashMap<>(inventory);
        this.equipment = new HashMap<>(equipment);
        this.runePouch = new HashMap<>(runePouch);
        this.spellbook = spellbook == null ? "" : spellbook;
        this.attackStyle = attackStyle == null ? "" : attackStyle;
        this.wildernessLevel = wildernessLevel;
    }

    public int countInventoryItems(Set<Integer> itemIds)
    {
        int total = 0;
        for (int itemId : itemIds)
        {
            total += inventory.getOrDefault(itemId, 0);
        }
        return total;
    }

    public boolean hasInventoryOrEquipmentItem(Set<Integer> itemIds)
    {
        for (int itemId : itemIds)
        {
            if (inventory.containsKey(itemId) || equipment.containsKey(itemId))
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
            if (equipment.containsKey(itemId))
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
            total += runePouch.getOrDefault(itemId, 0);
        }
        return total;
    }

    public Set<Integer> presentInventoryItems(Set<Integer> itemIds)
    {
        Set<Integer> present = new HashSet<>();
        for (int itemId : itemIds)
        {
            if (inventory.containsKey(itemId))
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
}
