package com.smartloadout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoadoutRule
{
    private LoadoutRuleType type;
    private String title;
    private String itemGroup;
    private List<Integer> itemIds = new ArrayList<>();
    private int minimumQuantity;
    private String expectedValue;
    private int wildernessLimit;

    public LoadoutRule()
    {
    }

    public LoadoutRule(LoadoutRuleType type, String title)
    {
        this.type = type;
        this.title = title;
    }

    public LoadoutRuleType getType()
    {
        return type;
    }

    public String getTitle()
    {
        return title == null ? "" : title;
    }

    public String getItemGroup()
    {
        return itemGroup == null ? "" : itemGroup;
    }

    public void setItemGroup(String itemGroup)
    {
        this.itemGroup = itemGroup;
    }

    public List<Integer> getItemIds()
    {
        return itemIds == null ? Collections.emptyList() : itemIds;
    }

    public int getMinimumQuantity()
    {
        return minimumQuantity;
    }

    public void setMinimumQuantity(int minimumQuantity)
    {
        this.minimumQuantity = minimumQuantity;
    }

    public String getExpectedValue()
    {
        return expectedValue == null ? "" : expectedValue;
    }

    public void setExpectedValue(String expectedValue)
    {
        this.expectedValue = expectedValue;
    }

    public int getWildernessLimit()
    {
        return wildernessLimit;
    }

    public void setWildernessLimit(int wildernessLimit)
    {
        this.wildernessLimit = wildernessLimit;
    }
}
