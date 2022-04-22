package com.bgsoftware.superiorskyblock.schematic.impl;

import com.bgsoftware.common.reflection.ReflectMethod;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.schematic.Schematic;
import com.bgsoftware.superiorskyblock.key.KeyImpl;
import com.bgsoftware.superiorskyblock.schematic.BaseSchematic;
import com.bgsoftware.superiorskyblock.utils.LocationUtils;
import com.bgsoftware.superiorskyblock.utils.debug.PluginDebugger;
import com.bgsoftware.superiorskyblock.utils.events.EventsCaller;
import com.bgsoftware.superiorskyblock.world.chunks.ChunkPosition;
import com.boydti.fawe.object.clipboard.FaweClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;

public final class WorldEditSchematic extends BaseSchematic implements Schematic {

    private static ReflectMethod<Object> AT;
    private static ReflectMethod<EditSession> PASTE;

    private static final ReflectMethod<Object> GET_BLOCK_TYPE = new ReflectMethod<>(BaseBlock.class, "getBlockType");
    private static final ReflectMethod<Integer> GET_INTERNAL_ID = new ReflectMethod<>(BaseBlock.class, "getInternalId");
    private static final ReflectMethod<Material> ADAPT = new ReflectMethod<>("com.sk89q.worldedit.bukkit.BukkitAdapter", "adapt",
            "com.sk89q.worldedit.world.block.BlockTypes");

    private static final ReflectMethod<Integer> GET_ID = new ReflectMethod<>(BaseBlock.class, "getId");
    private static final ReflectMethod<Integer> GET_DATA = new ReflectMethod<>(BaseBlock.class, "getData");

    private final com.boydti.fawe.object.schematic.Schematic schematic;

    static {
        try {
            Class<?> blockVectorClass = Class.forName("com.sk89q.worldedit.math.BlockVector3");
            AT = new ReflectMethod<>(blockVectorClass, "at", int.class, int.class, int.class);
            PASTE = new ReflectMethod<>(com.boydti.fawe.object.schematic.Schematic.class,
                    "paste", World.class, blockVectorClass, boolean.class, boolean.class, Transform.class);
        } catch (ClassNotFoundException ignored) {

        }
    }

    public WorldEditSchematic(String name, com.boydti.fawe.object.schematic.Schematic schematic) {
        super(name);
        this.schematic = schematic;
        readBlocks();
    }

    @Override
    public void pasteSchematic(Island island, Location location, Runnable callback) {
        pasteSchematic(island, location, callback, null);
    }

    @Override
    public void pasteSchematic(Island island, Location location, Runnable callback, Consumer<Throwable> onFailure) {
        try {
            PluginDebugger.debug("Action: Paste Schematic, Island: " + island.getOwner().getName() + ", Location: " + LocationUtils.getLocation(location) + ", Schematic: " + name);

            Object _point = AT.invoke(null, location.getBlockX(), location.getBlockY(), location.getBlockZ());
            EditSession editSession = PASTE.invoke(schematic, new BukkitWorld(location.getWorld()), _point, false, true, null);

            if (editSession == null) {
                com.sk89q.worldedit.Vector point = new com.sk89q.worldedit.Vector(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                editSession = schematic.paste(new BukkitWorld(location.getWorld()), point, true, true, null);
            }

            editSession.addNotifyTask(() -> {
                try {
                    island.handleBlocksPlace(cachedCounts);

                    EventsCaller.callIslandSchematicPasteEvent(island, name, location);

                    callback.run();
                } catch (Throwable ex) {
                    if (onFailure != null)
                        onFailure.accept(ex);
                }
            });
        } catch (Throwable ex) {
            if (onFailure != null)
                onFailure.accept(ex);
        }
    }

    @Override
    public Location adjustRotation(Location location) {
        return location;
    }

    @Override
    public Set<ChunkPosition> getLoadedChunks() {
        return Collections.emptySet();
    }

    private void readBlocks() {
        BlockArrayClipboard clipboard = (BlockArrayClipboard) schematic.getClipboard();

        assert clipboard != null;

        try {
            clipboard.IMP.forEach(new BlockReader() {
                @Override
                public void run(int x, int y, int z, BaseBlock block) {
                    readBlock(block);
                }
            }, false);
        } catch (Throwable ex) {
            clipboard.IMP.forEach(new FaweClipboard.BlockReader() {
                @Override
                public void run(int x, int y, int z, BlockState block) {
                    readBlock(block);
                }
            }, false);
        }
    }

    private void readBlock(Object baseBlock) {
        Key key;

        if (ADAPT.isValid() && GET_BLOCK_TYPE.isValid() && GET_INTERNAL_ID.isValid()) {
            Material material = ADAPT.invoke(null, GET_BLOCK_TYPE.invoke(baseBlock));
            int data = GET_INTERNAL_ID.invokeWithDef(baseBlock, 0);
            key = KeyImpl.of(material, (byte) data);
        } else {
            int id = GET_ID.invoke(baseBlock);
            int data = GET_DATA.invoke(baseBlock);
            //noinspection deprecation
            key = KeyImpl.of(Material.getMaterial(id), (byte) data);
        }

        cachedCounts.put(key, cachedCounts.getRaw(key, 0) + 1);
    }

    private static abstract class BlockReader extends FaweClipboard.BlockReader {

        public abstract void run(int x, int y, int z, BaseBlock block);

        public void run(int x, int y, int z, BlockState block) {
            // Do nothing.
        }

    }

}