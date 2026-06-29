package com.smartloadout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ActivityTemplateTest
{
    @Test
    public void nullRulesAndItemGroupsBehaveAsEmpty()
    {
        ActivityTemplate template = new ActivityTemplate("Zulrah", "Boss", null, null);

        assertEquals("Zulrah", template.getName());
        assertEquals("Boss", template.getCategory());
        assertTrue(template.getRules().isEmpty());
        assertTrue(template.getItemGroups().isEmpty());
    }

    @Test
    public void itemGroupListsAreDefensivelyCopied()
    {
        List<Integer> foodIds = new ArrayList<>();
        foodIds.add(385);
        Map<String, List<Integer>> itemGroups = new HashMap<>();
        itemGroups.put("food", foodIds);

        ActivityTemplate template = new ActivityTemplate("Vorkath", "Boss", new ArrayList<LoadoutRule>(), itemGroups);
        foodIds.add(391);

        assertEquals(1, template.getItemGroups().get("food").size());
        assertEquals(Integer.valueOf(385), template.getItemGroups().get("food").get(0));
    }
}
