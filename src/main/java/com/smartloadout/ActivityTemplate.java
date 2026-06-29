package com.smartloadout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityTemplate
{
    private String name;
    private String category;
    private String notes;
    private List<LoadoutRule> rules = new ArrayList<>();
    private Map<String, List<Integer>> itemGroups = new HashMap<>();

    public ActivityTemplate()
    {
    }

    public ActivityTemplate(String name, String category, List<LoadoutRule> rules, Map<String, List<Integer>> itemGroups)
    {
        this.name = name;
        this.category = category;
        this.rules = new ArrayList<>(rules);
        this.itemGroups = new HashMap<>(itemGroups);
    }

    public String getName()
    {
        return name == null ? "" : name;
    }

    public String getCategory()
    {
        return category == null ? "" : category;
    }

    public String getNotes()
    {
        return notes == null ? "" : notes;
    }

    public List<LoadoutRule> getRules()
    {
        return rules == null ? Collections.emptyList() : rules;
    }

    public Map<String, List<Integer>> getItemGroups()
    {
        return itemGroups == null ? Collections.emptyMap() : itemGroups;
    }
}
