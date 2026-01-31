package fr.robie.craftengineconverter.api;

import fr.maxlego08.sarah.Column;
import org.jetbrains.annotations.NotNull;

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

    public Long getId() {
        return id;
    }

    @NotNull
    public String getWorldName() {
        return world_name;
    }

    public Integer getChunkX() {
        return chunk_x;
    }

    public Integer getChunkZ() {
        return chunk_z;
    }

    public Integer getBlockX() {
        return block_x;
    }

    public Integer getBlockY() {
        return block_y;
    }

    public Integer getBlockZ() {
        return block_z;
    }

    @NotNull
    public String getOriginalBlock() {
        return original_block;
    }

    @NotNull
    public String getConvertedBlock() {
        return converted_block;
    }

    public Boolean isReverted() {
        return reverted;
    }
}