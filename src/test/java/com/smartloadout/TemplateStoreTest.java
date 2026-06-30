package com.smartloadout;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TemplateStoreTest
{
    @Test
    public void roundTripTemplatesAsJson()
    {
        Map<String, List<Integer>> itemGroups = new HashMap<>();
        itemGroups.put("food", Arrays.asList(385, 391));
        LoadoutRule rule = new LoadoutRule(LoadoutRuleType.MINIMUM_QUANTITY, "Bring at least 8 food");
        rule.setItemGroup("food");
        rule.setMinimumQuantity(8);
        ActivityTemplate template = new ActivityTemplate("Generic boss trip", "bossing", Collections.singletonList(rule), itemGroups);
        template.setNotes("Bring a teleport out.");

        String json = TemplateStore.exportTemplates(Collections.singletonList(template));
        List<ActivityTemplate> imported = TemplateStore.importTemplates(json);

        assertEquals(1, imported.size());
        assertEquals("Generic boss trip", imported.get(0).getName());
        assertEquals("bossing", imported.get(0).getCategory());
        assertEquals("Bring a teleport out.", imported.get(0).getNotes());
        assertEquals(Arrays.asList(385, 391), imported.get(0).getItemGroups().get("food"));
        assertEquals(1, imported.get(0).getRules().size());
        assertEquals(LoadoutRuleType.MINIMUM_QUANTITY, imported.get(0).getRules().get(0).getType());
        assertEquals("food", imported.get(0).getRules().get(0).getItemGroup());
        assertEquals(8, imported.get(0).getRules().get(0).getMinimumQuantity());
    }

    @Test
    public void importBlankJsonReturnsEmptyList()
    {
        assertTrue(TemplateStore.importTemplates(" ").isEmpty());
    }

    @Test
    public void importMalformedJsonReturnsEmptyList()
    {
        assertTrue(TemplateStore.importTemplates("not template json").isEmpty());
    }

    @Test
    public void importNonArrayJsonReturnsEmptyList()
    {
        assertTrue(TemplateStore.importTemplates("{\"name\":\"Not an array\"}").isEmpty());
    }

    @Test
    public void importJsonArrayFiltersNullEntries()
    {
        List<ActivityTemplate> templates = TemplateStore.importTemplates("[null,{\"name\":\"Valid template\",\"category\":\"bossing\"}]");

        assertEquals(1, templates.size());
        assertEquals("Valid template", templates.get(0).getName());
    }

    @Test
    public void starterTemplatesAreAvailable()
    {
        List<ActivityTemplate> templates = StarterTemplates.templates();

        assertFalse(templates.isEmpty());
        assertTrue(hasTemplateNamed(templates, "Wilderness clue"));
        assertTrue(hasTemplateNamed(templates, "Generic boss trip"));
        assertTrue(hasTemplateNamed(templates, "Raid pre-check"));
        assertTrue(hasRuleTitle(templates, "Raid pre-check", "Bring at least one raid rune type"));
    }

    private boolean hasRuleTitle(List<ActivityTemplate> templates, String templateName, String ruleTitle)
    {
        for (ActivityTemplate template : templates)
        {
            if (!templateName.equals(template.getName()))
            {
                continue;
            }

            for (LoadoutRule rule : template.getRules())
            {
                if (ruleTitle.equals(rule.getTitle()))
                {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasTemplateNamed(List<ActivityTemplate> templates, String name)
    {
        for (ActivityTemplate template : templates)
        {
            if (name.equals(template.getName()))
            {
                return true;
            }
        }
        return false;
    }
}
