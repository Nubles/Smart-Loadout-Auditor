package com.smartloadout;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(SmartLoadoutAuditorConfig.GROUP)
public interface SmartLoadoutAuditorConfig extends Config
{
	String GROUP = "smartloadoutauditor";

	@ConfigItem(
		keyName = "auditOnBankClose",
		name = "Audit when bank closes",
		description = "Run the selected loadout audit when the bank closes.",
		position = 0
	)
	default boolean auditOnBankClose()
	{
		return true;
	}

	@ConfigItem(
		keyName = "notifyOnFailures",
		name = "Notify on failures",
		description = "Show a RuneLite notification when unresolved loadout failures are found.",
		position = 1
	)
	default boolean notifyOnFailures()
	{
		return true;
	}
}
