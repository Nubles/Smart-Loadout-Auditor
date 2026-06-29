package com.smartloadout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LoadoutAuditEngine
{
    public List<AuditResult> audit(ActivityTemplate template, LoadoutSnapshot snapshot)
    {
        List<AuditResult> results = new ArrayList<>();
        if (template == null)
        {
            return results;
        }

        for (LoadoutRule rule : template.getRules())
        {
            if (rule == null)
            {
                results.add(new AuditResult(AuditSeverity.WARNING, "Invalid rule", "Template contains an empty rule."));
            }
            else if (snapshot == null)
            {
                results.add(new AuditResult(AuditSeverity.WARNING, rule.getTitle(), "Current loadout state is unavailable."));
            }
            else
            {
                results.add(evaluate(template, rule, snapshot));
            }
        }
        return results;
    }

    private AuditResult evaluate(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        if (rule.getType() == null)
        {
            return new AuditResult(AuditSeverity.WARNING, rule.getTitle(), "Unsupported rule type.");
        }

        switch (rule.getType())
        {
            case REQUIRED_ITEM:
                return requiredItem(template, rule, snapshot);
            case MINIMUM_QUANTITY:
                return minimumQuantity(template, rule, snapshot);
            case REQUIRED_SPELLBOOK:
                return equalsText(rule, snapshot.getSpellbook(), "Spellbook is correct", "Wrong spellbook");
            case RUNE_POUCH_CONTAINS:
                return runePouchContains(template, rule, snapshot);
            case AMMO_OR_RANGED_WEAPON:
                return equippedGroup(template, rule, snapshot, "Ranged equipment present", "No matching ammo or ranged weapon equipped");
            case ATTACK_STYLE:
                return equalsText(rule, snapshot.getAttackStyle(), "Attack style is correct", "Wrong attack style");
            case TELEPORT_CATEGORY:
                return requiredItem(template, rule, snapshot);
            case WILDERNESS_TELEPORT_LIMIT:
                return wildernessTeleportLimit(template, rule, snapshot);
            case DUPLICATE_SUPPLY_WARNING:
                return duplicateSupplyWarning(template, rule, snapshot);
            default:
                return new AuditResult(AuditSeverity.WARNING, rule.getTitle(), "Unsupported rule type.");
        }
    }

    private AuditResult requiredItem(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        Set<Integer> itemIds = resolveItems(template, rule);
        boolean present = snapshot.hasInventoryOrEquipmentItem(itemIds);
        return new AuditResult(present ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), present ? "Required item is present." : "Required item is missing.");
    }

    private AuditResult minimumQuantity(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        Set<Integer> itemIds = resolveItems(template, rule);
        int count = snapshot.countInventoryItems(itemIds);
        boolean enough = count >= rule.getMinimumQuantity();
        return new AuditResult(enough ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), enough ? "Quantity requirement met." : "Only found " + count + " of " + rule.getMinimumQuantity() + " required.");
    }

    private AuditResult runePouchContains(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        Set<Integer> itemIds = resolveItems(template, rule);
        int count = snapshot.countRunePouchItems(itemIds);
        boolean enough = count >= Math.max(1, rule.getMinimumQuantity());
        return new AuditResult(enough ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), enough ? "Rune pouch requirement met." : "Rune pouch is missing required rune.");
    }

    private AuditResult equippedGroup(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot, String pass, String fail)
    {
        boolean present = snapshot.hasEquippedItem(resolveItems(template, rule));
        return new AuditResult(present ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), present ? pass : fail);
    }

    private AuditResult equalsText(LoadoutRule rule, String actual, String pass, String fail)
    {
        String expected = normalize(rule.getExpectedValue());
        if (expected.isEmpty())
        {
            return new AuditResult(AuditSeverity.WARNING, rule.getTitle(), "Rule is missing an expected value.");
        }

        boolean matches = normalize(actual).equals(expected);
        return new AuditResult(matches ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), matches ? pass : fail + ": expected " + rule.getExpectedValue() + ".");
    }

    private AuditResult wildernessTeleportLimit(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        boolean present = snapshot.hasInventoryOrEquipmentItem(resolveItems(template, rule));
        if (!present)
        {
            return new AuditResult(AuditSeverity.FAIL, rule.getTitle(), "No matching teleport found.");
        }
        boolean usable = snapshot.getWildernessLevel() <= rule.getWildernessLimit();
        return new AuditResult(usable ? AuditSeverity.PASS : AuditSeverity.FAIL, rule.getTitle(), usable ? "Teleport is usable at this Wilderness level." : "Teleport is blocked above level " + rule.getWildernessLimit() + " Wilderness.");
    }

    private AuditResult duplicateSupplyWarning(ActivityTemplate template, LoadoutRule rule, LoadoutSnapshot snapshot)
    {
        Set<Integer> present = snapshot.presentInventoryItems(resolveItems(template, rule));
        boolean duplicate = present.size() > 1;
        return new AuditResult(duplicate ? AuditSeverity.WARNING : AuditSeverity.PASS, rule.getTitle(), duplicate ? "Multiple supply variants from this group are present." : "No duplicate supply variants found.");
    }

    private Set<Integer> resolveItems(ActivityTemplate template, LoadoutRule rule)
    {
        Set<Integer> itemIds = new HashSet<>(rule.getItemIds());
        if (!rule.getItemGroup().isEmpty() && template.getItemGroups().containsKey(rule.getItemGroup()))
        {
            itemIds.addAll(template.getItemGroups().get(rule.getItemGroup()));
        }
        return itemIds;
    }

    private static String normalize(String value)
    {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
