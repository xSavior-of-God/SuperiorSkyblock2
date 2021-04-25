package com.bgsoftware.superiorskyblock.hooks;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.objects.Pair;
import com.bgsoftware.superiorskyblock.utils.key.Key;
import com.bgsoftware.superiorskyblock.utils.legacy.Materials;
import com.google.common.base.Preconditions;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public final class BlocksProvider_MergedSpawner implements BlocksProvider {

    private static boolean registered = false;

    public BlocksProvider_MergedSpawner(){
        if(!registered) {
            registered = true;
            SuperiorSkyblockPlugin.log("Using MergedSpawner as a spawners provider.");
        }
    }

    @Override
    public Pair<Integer, String> getSpawner(Location location) {
        Preconditions.checkNotNull(location, "location parameter cannot be null.");
        return null;
    }

    @Override
    public String getSpawnerType(ItemStack itemStack) {
        return null;
    }

    public static boolean isRegistered(){
        return registered;
    }


}
