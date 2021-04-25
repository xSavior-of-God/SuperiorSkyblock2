package com.bgsoftware.superiorskyblock.hooks;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public final class VanishProvider_CMI implements VanishProvider, Listener {

    private static boolean alreadyEnabled = false;

    private final SuperiorSkyblockPlugin plugin;

    public VanishProvider_CMI(SuperiorSkyblockPlugin plugin){
        this.plugin = plugin;

        if(!alreadyEnabled){
            alreadyEnabled = true;
            Bukkit.getPluginManager().registerEvents(this, plugin);
        }

        SuperiorSkyblockPlugin.log("Hooked into CMI for support of vanish status of players.");
    }

    @Override
    public boolean isVanished(Player player) {
        return false;
    }

}
