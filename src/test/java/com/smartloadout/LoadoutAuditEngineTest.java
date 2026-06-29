package com.smartloadout;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LoadoutAuditEngineTest
{
    private static final int LAW_RUNE = 563;
    private static final int ROYAL_SEED_POD = 19564;
    private static final int VARROCK_TELEPORT = 8007;
    private static final int SHARK = 385;

    @Test
    public void requiredItemAcceptsItemGroupAlternative()
    {
        LoadoutRule rule = rule(LoadoutRuleType.REQUIRED_ITEM, "Bring teleport", "teleports", 0, "", 0);
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("teleports", Arrays.asList(ROYAL_SEED_POD, VARROCK_TELEPORT));
        ActivityTemplate template = new ActivityTemplate("Wildy clue", "clue", Collections.singletonList(rule), groups);

        LoadoutSnapshot snapshot = snapshot(Collections.singletonMap(ROYAL_SEED_POD, 1), Collections.emptyMap(), Collections.emptyMap(), "standard", "ranged", 0);

        assertEquals(AuditSeverity.PASS, new LoadoutAuditEngine().audit(template, snapshot).get(0).getSeverity());
    }

    @Test
    public void minimumQuantityFailsWhenTooLow()
    {
        LoadoutRule rule = rule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring food", "", 10, "", 0);
        rule.getItemIds().add(SHARK);
        ActivityTemplate template = template(rule);

        LoadoutSnapshot snapshot = snapshot(Collections.singletonMap(SHARK, 4), Collections.emptyMap(), Collections.emptyMap(), "standard", "melee", 0);

        assertEquals(AuditSeverity.FAIL, new LoadoutAuditEngine().audit(template, snapshot).get(0).getSeverity());
    }

    @Test
    public void spellbookRuleComparesCaseInsensitively()
    {
        ActivityTemplate template = template(rule(LoadoutRuleType.REQUIRED_SPELLBOOK, "Use ancients", "", 0, "ancient", 0));
        LoadoutSnapshot snapshot = snapshot(Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), "ANCIENT", "magic", 0);

        assertEquals(AuditSeverity.PASS, new LoadoutAuditEngine().audit(template, snapshot).get(0).getSeverity());
    }

    @Test
    public void runePouchRuleFailsWhenRuneMissing()
    {
        LoadoutRule rule = rule(LoadoutRuleType.RUNE_POUCH_CONTAINS, "Law rune in pouch", "", 1, "", 0);
        rule.getItemIds().add(LAW_RUNE);
        ActivityTemplate template = template(rule);

        LoadoutSnapshot snapshot = snapshot(Collections.emptyMap(), Collections.emptyMap(), Collections.emptyMap(), "standard", "magic", 0);

        assertEquals(AuditSeverity.FAIL, new LoadoutAuditEngine().audit(template, snapshot).get(0).getSeverity());
    }

    @Test
    public void wildernessTeleportLimitFailsWhenTooDeep()
    {
        LoadoutRule rule = rule(LoadoutRuleType.WILDERNESS_TELEPORT_LIMIT, "Teleport works here", "level20Teleports", 0, "", 20);
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("level20Teleports", Collections.singletonList(VARROCK_TELEPORT));
        ActivityTemplate template = new ActivityTemplate("Wildy clue", "clue", Collections.singletonList(rule), groups);

        LoadoutSnapshot snapshot = snapshot(Collections.singletonMap(VARROCK_TELEPORT, 1), Collections.emptyMap(), Collections.emptyMap(), "standard", "melee", 31);

        assertEquals(AuditSeverity.FAIL, new LoadoutAuditEngine().audit(template, snapshot).get(0).getSeverity());
    }

    @Test
    public void duplicateSupplyWarningWarnsWhenMultipleVariantsPresent()
    {
        LoadoutRule rule = rule(LoadoutRuleType.DUPLICATE_SUPPLY_WARNING, "Duplicate food", "food", 0, "", 0);
        Map<String, List<Integer>> groups = new HashMap<>();
        groups.put("food", Arrays.asList(SHARK, 391));
        ActivityTemplate template = new ActivityTemplate("Boss", "boss", Collections.singletonList(rule), groups);
        Map<Integer, Integer> inventory = new HashMap<>();
        inventory.put(SHARK, 3);
        inventory.put(391, 2);

        assertEquals(AuditSeverity.WARNING, new LoadoutAuditEngine().audit(template, snapshot(inventory, Collections.emptyMap(), Collections.emptyMap(), "standard", "melee", 0)).get(0).getSeverity());
    }

    private static ActivityTemplate template(LoadoutRule rule)
    {
        return new ActivityTemplate("Template", "test", Collections.singletonList(rule), Collections.emptyMap());
    }

    private static LoadoutRule rule(LoadoutRuleType type, String title, String itemGroup, int minimumQuantity, String expectedValue, int wildernessLimit)
    {
        LoadoutRule rule = new LoadoutRule(type, title);
        rule.setItemGroup(itemGroup);
        rule.setMinimumQuantity(minimumQuantity);
        rule.setExpectedValue(expectedValue);
        rule.setWildernessLimit(wildernessLimit);
        return rule;
    }

    private static LoadoutSnapshot snapshot(
        Map<Integer, Integer> inventory,
        Map<Integer, Integer> equipment,
        Map<Integer, Integer> runePouch,
        String spellbook,
        String attackStyle,
        int wildernessLevel)
    {
        return new LoadoutSnapshot(inventory, equipment, runePouch, spellbook, attackStyle, wildernessLevel);
    }
}
