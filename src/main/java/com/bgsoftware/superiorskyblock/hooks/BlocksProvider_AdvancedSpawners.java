package com.bgsoftware.superiorskyblock.hooks;

import com.bgsoftware.superiorskyblock.SuperiorSkyblockPlugin;
import com.bgsoftware.superiorskyblock.api.objects.Pair;
import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public final class BlocksProvider_AdvancedSpawners implements BlocksProvider {

    private static boolean registered = false;

    public BlocksProvider_AdvancedSpawners(){
        if(!registered) {
            registered = true;
            SuperiorSkyblockPlugin.log("Using AdvancedSpawners as a spawners provider.");
        }
    }

    @Override
    public Pair<Integer, String> getSpawner(Location location) {
        Preconditions.checkNotNull(location, "location parameter cannot be null.");
        return null;
    }

    @Override
    public String getSpawnerType(ItemStack itemStack) {
        Preconditions.checkNotNull(itemStack, "itemStack parameter cannot be null.");
        return null;
    }


}
