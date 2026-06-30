package com.smartloadout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StarterTemplates
{
    private StarterTemplates()
    {
    }

    public static List<ActivityTemplate> templates()
    {
        List<ActivityTemplate> templates = new ArrayList<>();
        templates.add(wildernessClue());
        templates.add(genericBossTrip());
        templates.add(raidPrecheck());
        return templates;
    }

    private static ActivityTemplate wildernessClue()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("level20Teleports", Arrays.asList(8007, 8010, 8013));
        groups.put("antipoison", Arrays.asList(175, 177, 179, 2446, 5952));
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.WILDERNESS_TELEPORT_LIMIT, "Bring a level 20 Wilderness teleport", "level20Teleports", 1, "", 20));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring antipoison", "antipoison", 1, "", 0));
        return new ActivityTemplate("Wilderness clue", "clue", rules, groups);
    }

    private static ActivityTemplate genericBossTrip()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("food", Arrays.asList(385, 391, 13441));
        groups.put("teleports", Arrays.asList(8007, 8010, 8013, 19564));
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 8 food", "food", 8, "", 0));
        rules.add(rule(LoadoutRuleType.TELEPORT_CATEGORY, "Bring a teleport out", "teleports", 1, "", 0));
        return new ActivityTemplate("Generic boss trip", "bossing", rules, groups);
    }

    private static ActivityTemplate raidPrecheck()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("runes", Arrays.asList(556, 557, 558, 560, 565));
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.REQUIRED_SPELLBOOK, "Use standard spellbook", "", 0, "standard", 0));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring at least one raid rune type", "runes", 1, "", 0));
        return new ActivityTemplate("Raid pre-check", "raid", rules, groups);
    }

    private static LoadoutRule rule(
		LoadoutRuleType type,
		String title,
		String itemGroup,
		int minimumQuantity,
		String expectedValue,
		int wildernessLimit)
    {
        LoadoutRule rule = new LoadoutRule(type, title);
        rule.setItemGroup(itemGroup);
        rule.setMinimumQuantity(minimumQuantity);
        rule.setExpectedValue(expectedValue);
        rule.setWildernessLimit(wildernessLimit);
        return rule;
    }
}

