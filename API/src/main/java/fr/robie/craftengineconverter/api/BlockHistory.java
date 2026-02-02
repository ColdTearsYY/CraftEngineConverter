/**
 * Represents the history record for a single block conversion in the world.
 * Stores original and converted block data, coordinates, and revert state.
 * Used for restoration and tracking of conversion history.
 */
package fr.robie.craftengineconverter.api;

import fr.maxlego08.sarah.Column;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockHistory {
    @Column(value = "id", autoIncrement = true)
    private final Long id;
    @Column(value="world_name")
    private final String world_name;
    @Column(value="chunk_x")
    private final Integer chunk_x;
    @Column(value="chunk_z")
    private final Integer chunk_z;
    @Column(value="block_x")
    private final Integer block_x;
    @Column(value="block_y")
    private final Integer block_y;
    @Column(value="block_z")
    private final Integer block_z;
    @Column("original_block")
    private final String original_block;
    @Column("converted_block")
    private final String converted_block;
    @Column("reverted")
    private final Boolean reverted;

    /**
     * Constructs a new BlockHistory record.
     *
     * @param id Unique database identifier for the record.
     * @param world_name Name of the world containing this block.
     * @param chunk_x X coordinate of the chunk.
     * @param chunk_z Z coordinate of the chunk.
     * @param block_x X coordinate of the block.
     * @param block_y Y coordinate of the block.
     * @param block_z Z coordinate of the block.
     * @param original_block Block type before conversion.
     * @param converted_block Block type after conversion.
     * @param reverted Whether the block was reverted to its original state.
     */
    public BlockHistory(Long id, @NotNull String world_name, Integer chunk_x, Integer chunk_z, Integer block_x, Integer block_y, Integer block_z, @NotNull String original_block, @NotNull String converted_block, Boolean reverted) {
        this.id = id;
        this.world_name = world_name;
        this.chunk_x = chunk_x;
        this.chunk_z = chunk_z;
        this.block_x = block_x;
        this.block_y = block_y;
        this.block_z = block_z;
        this.original_block = original_block;
        this.converted_block = converted_block;
        this.reverted = reverted;
    }

    /**
     * Gets the unique database ID for this record.
     * @return Record ID, or null if not persisted yet.
     */
    @Nullable
    public Long getId() {
        return this.id;
    }

    /**
     * @return World name for this block conversion (never null).
     */
    @NotNull
    public String getWorldName() {
        return this.world_name;
    }

    /**
     * @return Chunk X coordinate of the block's chunk.
     */
    @Nullable
    public Integer getChunkX() {
        return this.chunk_x;
    }

    /**
     * @return Chunk Z coordinate of the block's chunk.
     */
    @Nullable
    public Integer getChunkZ() {
        return this.chunk_z;
    }

    /**
     * @return X coordinate of the block.
     */
    @Nullable
    public Integer getBlockX() {
        return this.block_x;
    }

    /**
     * @return Y coordinate of the block.
     */
    @Nullable
    public Integer getBlockY() {
        return this.block_y;
    }

    /**
     * @return Z coordinate of the block.
     */
    @Nullable
    public Integer getBlockZ() {
        return this.block_z;
    }

    /**
     * @return Block type before conversion.
     */
    @NotNull
    public String getOriginalBlock() {
        return this.original_block;
    }

    /**
     * @return Block type after conversion.
     */
    @NotNull
    public String getConvertedBlock() {
        return this.converted_block;
    }

    /**
     * @return True if reverted, false if not, null if unknown.
     */
    @Nullable
    public Boolean isReverted() {
        return this.reverted;
    }
}