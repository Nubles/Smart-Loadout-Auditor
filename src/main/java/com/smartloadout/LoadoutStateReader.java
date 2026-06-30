package com.smartloadout;

import java.util.HashMap;
import java.util.Map;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.Player;
import net.runelite.api.Varbits;
import net.runelite.api.coords.WorldPoint;

public class LoadoutStateReader
{
	private static final int ATTACK_STYLE_VARP = 43;
	private static final int WILDERNESS_BASE_Y = 3520;

	private final Client client;

	public LoadoutStateReader(Client client)
	{
		this.client = client;
	}

	public LoadoutSnapshot read()
	{
		return new LoadoutSnapshot(
			items(InventoryID.INVENTORY),
			items(InventoryID.EQUIPMENT),
			runePouch(),
			spellbook(),
			attackStyle(),
			wildernessLevel());
	}

	private Map<Integer, Integer> items(InventoryID inventoryID)
	{
		Map<Integer, Integer> counts = new HashMap<>();
		ItemContainer container = client.getItemContainer(inventoryID);
		if (container == null)
		{
			return counts;
		}

		for (Item item : container.getItems())
		{
			if (item.getId() > 0 && item.getQuantity() > 0)
			{
				counts.put(item.getId(), counts.getOrDefault(item.getId(), 0) + item.getQuantity());
			}
		}
		return counts;
	}

	private Map<Integer, Integer> runePouch()
	{
		Map<Integer, Integer> runes = new HashMap<>();
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE1, Varbits.RUNE_POUCH_AMOUNT1);
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE2, Varbits.RUNE_POUCH_AMOUNT2);
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE3, Varbits.RUNE_POUCH_AMOUNT3);
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE4, Varbits.RUNE_POUCH_AMOUNT4);
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE5, Varbits.RUNE_POUCH_AMOUNT5);
		addRunePouchSlot(runes, Varbits.RUNE_POUCH_RUNE6, Varbits.RUNE_POUCH_AMOUNT6);
		return runes;
	}

	private void addRunePouchSlot(Map<Integer, Integer> runes, int runeVarbit, int amountVarbit)
	{
		int runeItemId = client.getVarbitValue(runeVarbit);
		int amount = client.getVarbitValue(amountVarbit);
		if (runeItemId > 0 && amount > 0)
		{
			runes.put(runeItemId, runes.getOrDefault(runeItemId, 0) + amount);
		}
	}

	private String spellbook()
	{
		int value = client.getVarbitValue(Varbits.SPELLBOOK);
		switch (value)
		{
			case 1:
				return "ancient";
			case 2:
				return "lunar";
			case 3:
				return "arceuus";
			default:
				return "standard";
		}
	}

	private String attackStyle()
	{
		return Integer.toString(client.getVarpValue(ATTACK_STYLE_VARP));
	}

	private int wildernessLevel()
	{
		if (client.getVarbitValue(Varbits.IN_WILDERNESS) != 1)
		{
			return 0;
		}

		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return 1;
		}

		WorldPoint location = player.getWorldLocation();
		if (location == null)
		{
			return 1;
		}

		return Math.max(1, ((location.getY() - WILDERNESS_BASE_Y) / 8) + 1);
	}
}
