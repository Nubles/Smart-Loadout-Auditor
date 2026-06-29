package com.smartloadout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class TemplateStore
{
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private TemplateStore()
    {
    }

    public static String exportTemplates(List<ActivityTemplate> templates)
    {
        return GSON.toJson(templates == null ? Collections.emptyList() : templates);
    }

    public static List<ActivityTemplate> importTemplates(String json)
    {
        if (json == null || json.trim().isEmpty())
        {
            return Collections.emptyList();
        }

        ActivityTemplate[] templates;
        try
        {
            templates = GSON.fromJson(json, ActivityTemplate[].class);
        }
        catch (JsonParseException ex)
        {
            return Collections.emptyList();
        }

        if (templates == null)
        {
            return Collections.emptyList();
        }

        List<ActivityTemplate> imported = new ArrayList<>();
        for (ActivityTemplate template : Arrays.asList(templates))
        {
            if (template != null)
            {
                imported.add(template);
            }
        }
        return imported;
    }
}
