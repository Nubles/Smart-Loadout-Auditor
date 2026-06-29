package com.smartloadout;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class SmartLoadoutAuditorPluginRunner
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(SmartLoadoutAuditorPlugin.class);
        RuneLite.main(args);
    }
}
