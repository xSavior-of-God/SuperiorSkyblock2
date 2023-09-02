package com.bgsoftware.superiorskyblock.api.service.world;

import com.bgsoftware.superiorskyblock.api.key.Key;
import com.bgsoftware.superiorskyblock.api.key.KeyMap;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import javax.annotation.Nullable;

public interface WorldRecordService {

    /**
     * Record a block place.
     *
     * @param block         The block that was placed.
     * @param blockCount    The amount of blocks that were placed.
     * @param oldBlockState The state of the block before placing the new one.
     * @param recordFlag    See {@link WorldRecordFlag}
     * @return The result of recording the block placement.
     */
    RecordResult recordBlockPlace(Block block, int blockCount, @Nullable BlockState oldBlockState, WorldRecordFlag recordFlag);

    /**
     * Record a block place.
     *
     * @param blockKey      The key of the block that was placed.
     * @param blockLocation The location of the block.
     * @param blockCount    The amount of blocks that were placed.
     * @param oldBlockState The state of the block before placing the new one.
     * @param recordFlag    See {@link WorldRecordFlag}
     * @return The result of recording the block placement.
     */
    RecordResult recordBlockPlace(Key blockKey, Location blockLocation, int blockCount, @Nullable BlockState oldBlockState, WorldRecordFlag recordFlag);

    /**
     * Record multiple block placements.
     *
     * @param blockCounts KeyMap containing the counts of the blocks that were placed.
     * @param location    The location of where the blocks were placed at.
     * @param recordFlag  See {@link WorldRecordFlag}
     * @return The result of recording the block placements.
     */
    RecordResult recordMultiBlocksPlace(KeyMap<Integer> blockCounts, Location location, WorldRecordFlag recordFlag);

    /**
     * Record a block break.
     *
     * @param block      The block that was broken
     * @param recordFlag See {@link WorldRecordFlag}
     * @return The result of recording the block break.
     */
    RecordResult recordBlockBreak(Block block, WorldRecordFlag recordFlag);

    /**
     * Record a block break.
     *
     * @param block      The block that was broken.
     * @param blockCount The amount of blocks that were broken.
     * @param recordFlag See {@link WorldRecordFlag}
     * @return The result of recording the block break.
     */
    RecordResult recordBlockBreak(Block block, int blockCount, WorldRecordFlag recordFlag);

    /**
     * Record a block break.
     *
     * @param blockKey      The key of the block that was broken.
     * @param blockLocation The location of the block.
     * @param blockCount    The amount of blocks that were broken.
     * @param recordFlag    See {@link WorldRecordFlag}
     * @return The result of recording the block break.
     */
    RecordResult recordBlockBreak(Key blockKey, Location blockLocation, int blockCount, WorldRecordFlag recordFlag);

    /**
     * Record multiple block breaks.
     *
     * @param blockCounts KeyMap containing the counts of the blocks that were broken.
     * @param location    The location of where the blocks were broken at.
     * @param recordFlag  See {@link WorldRecordFlag}
     * @return The result of recording the block breaks.
     */
    RecordResult recordMultiBlocksBreak(KeyMap<Integer> blockCounts, Location location, WorldRecordFlag recordFlag);

    /**
     * Record an entity spawning into the world.
     *
     * @param entity The entity that spawned.
     * @return The result of recording the entity spawn.
     */
    RecordResult recordEntitySpawn(Entity entity);

    /**
     * Record an entity spawning into the world.
     *
     * @param entityType The type of the entity that spawned.
     * @param location   The location of where the entity was spawned.
     * @return The result of recording the entity spawn.
     */
    RecordResult recordEntitySpawn(EntityType entityType, Location location);

    /**
     * Record an entity despawning from the world.
     *
     * @param entity The entity that despawned.
     * @return The result of recording the entity despawn.
     */
    RecordResult recordEntityDespawn(Entity entity);

    /**
     * Record an entity despawning from the world.
     *
     * @param entityType The type of the entity that despawned.
     * @param location   The location of where the entity was despawned.
     * @return The result of recording the entity despawn.
     */
    RecordResult recordEntityDespawn(EntityType entityType, Location location);

}
