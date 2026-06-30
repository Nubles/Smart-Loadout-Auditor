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
        templates.add(barrowsRun());
        templates.add(zulrahTrip());
        templates.add(vorkathTrip());
        templates.add(wintertodtBasic());
        return templates;
    }

    private static ActivityTemplate wildernessClue()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("level20Teleports", Arrays.asList(8007, 8009, 8010, 8013));
        groups.put("antipoison", Arrays.asList(175, 177, 179, 2446, 5952));
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.WILDERNESS_TELEPORT_LIMIT, "Bring a level 20 Wilderness teleport", "level20Teleports", 1, "", 20));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring antipoison", "antipoison", 1, "", 0));
        return new ActivityTemplate("Wilderness clue", "clue", rules, groups);
    }

    private static ActivityTemplate genericBossTrip()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("food", food());
        groups.put("teleports", teleports());
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

    private static ActivityTemplate barrowsRun()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("spade", Arrays.asList(952));
        groups.put("food", food());
        groups.put("prayerRestores", prayerRestores());
        groups.put("teleports", teleports());
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring a spade", "spade", 1, "", 0));
        rules.add(rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 6 food", "food", 6, "", 0));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring prayer restoration", "prayerRestores", 1, "", 0));
        rules.add(rule(LoadoutRuleType.TELEPORT_CATEGORY, "Bring a teleport out", "teleports", 1, "", 0));
        return new ActivityTemplate("Barrows run", "bossing", rules, groups);
    }

    private static ActivityTemplate zulrahTrip()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("venomProtection", venomProtection());
        groups.put("food", food());
        groups.put("teleports", teleports());
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring venom protection", "venomProtection", 1, "", 0));
        rules.add(rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 8 food", "food", 8, "", 0));
        rules.add(rule(LoadoutRuleType.TELEPORT_CATEGORY, "Bring a teleport out", "teleports", 1, "", 0));
        return new ActivityTemplate("Zulrah trip", "bossing", rules, groups);
    }

    private static ActivityTemplate vorkathTrip()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("antifireProtection", antifireProtection());
        groups.put("food", food());
        groups.put("teleports", teleports());
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring antifire protection", "antifireProtection", 1, "", 0));
        rules.add(rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 8 food", "food", 8, "", 0));
        rules.add(rule(LoadoutRuleType.TELEPORT_CATEGORY, "Bring a teleport out", "teleports", 1, "", 0));
        return new ActivityTemplate("Vorkath trip", "bossing", rules, groups);
    }

    private static ActivityTemplate wintertodtBasic()
    {
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("fireSources", Arrays.asList(590, 2946, 20720, 29777));
        groups.put("knife", Arrays.asList(946));
        groups.put("axes", axes());
        groups.put("food", food());
        List<LoadoutRule> rules = new ArrayList<>();
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring a fire source", "fireSources", 1, "", 0));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring a knife", "knife", 1, "", 0));
        rules.add(rule(LoadoutRuleType.REQUIRED_ITEM, "Bring an axe", "axes", 1, "", 0));
        rules.add(rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 4 food", "food", 4, "", 0));
        return new ActivityTemplate("Wintertodt basic", "minigame", rules, groups);
    }

    private static List<Integer> food()
    {
        return Arrays.asList(385, 391, 397, 3144, 7946, 13441);
    }

    private static List<Integer> teleports()
    {
        return Arrays.asList(8007, 8009, 8010, 8013, 19564);
    }

    private static List<Integer> prayerRestores()
    {
        return Arrays.asList(139, 141, 143, 2434, 3024, 3026, 3028, 3030);
    }

    private static List<Integer> venomProtection()
    {
        return Arrays.asList(12905, 12907, 12909, 12911, 12913, 12915, 12917, 12919, 29824, 29827, 29830, 29833);
    }

    private static List<Integer> antifireProtection()
    {
        return Arrays.asList(2452, 2454, 2456, 2458, 11951, 11953, 11955, 11957, 21978, 21981, 21984, 21987, 22209, 22212, 22215, 22218);
    }

    private static List<Integer> axes()
    {
        return Arrays.asList(1349, 1351, 1353, 1355, 1357, 1359, 1361, 6739, 13241, 23673);
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
