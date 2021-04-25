package com.bgsoftware.superiorskyblock.hooks;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public final class JetsMinionsHook implements Listener {

    private final SuperiorSkyblockPlugin plugin;

    private JetsMinionsHook(SuperiorSkyblockPlugin plugin){
        this.plugin = plugin;
    }

    public static void register(SuperiorSkyblockPlugin plugin){
        Bukkit.getPluginManager().registerEvents(new JetsMinionsHook(plugin), plugin);
    }

}
