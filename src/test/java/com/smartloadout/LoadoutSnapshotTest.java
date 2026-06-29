package com.smartloadout;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LoadoutSnapshotTest
{
    @Test
    public void countsInventoryAlternativesAndDetectsEquipment()
    {
        Map<Integer, Integer> inventory = new HashMap<>();
        inventory.put(385, 7);
        inventory.put(391, 2);
        Map<Integer, Integer> equipment = new HashMap<>();
        equipment.put(9185, 1);
        Map<Integer, Integer> runePouch = new HashMap<>();
        runePouch.put(563, 150);

        LoadoutSnapshot snapshot = new LoadoutSnapshot(inventory, equipment, runePouch, "standard", "ranged", 21);
        Set<Integer> food = new HashSet<>(Arrays.asList(385, 391));

        assertEquals(9, snapshot.countInventoryItems(food));
        assertTrue(snapshot.hasInventoryOrEquipmentItem(Collections.singleton(9185)));
        assertTrue(snapshot.hasEquippedItem(Collections.singleton(9185)));
        assertEquals(150, snapshot.countRunePouchItems(Collections.singleton(563)));
        assertEquals(21, snapshot.getWildernessLevel());
        assertEquals("standard", snapshot.getSpellbook());
        assertEquals("ranged", snapshot.getAttackStyle());
        assertFalse(snapshot.presentInventoryItems(Collections.singleton(9999)).contains(9999));
    }
}
