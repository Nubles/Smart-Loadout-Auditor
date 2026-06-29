package com.smartloadout;

import java.util.Collections;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TemplateStoreTest
{
    @Test
    public void roundTripTemplatesAsJson()
    {
        ActivityTemplate template = new ActivityTemplate("Generic boss trip", "bossing", Collections.emptyList(), Collections.emptyMap());

        String json = TemplateStore.exportTemplates(Collections.singletonList(template));
        List<ActivityTemplate> imported = TemplateStore.importTemplates(json);

        assertEquals(1, imported.size());
        assertEquals("Generic boss trip", imported.get(0).getName());
        assertEquals("bossing", imported.get(0).getCategory());
    }

    @Test
    public void importBlankJsonReturnsEmptyList()
    {
        assertTrue(TemplateStore.importTemplates(" ").isEmpty());
    }

    @Test
    public void starterTemplatesAreAvailable()
    {
        List<ActivityTemplate> templates = StarterTemplates.templates();

        assertFalse(templates.isEmpty());
        assertEquals("Wilderness clue", templates.get(0).getName());
    }
}
