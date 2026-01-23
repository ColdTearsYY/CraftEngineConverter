package fr.robie.craftengineconverter.common.records;

public record ChunkPosition(String worldName, int x, int z){
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkPosition that = (ChunkPosition) o;
        return x == that.x && z == that.z && worldName.equals(that.worldName);
    }
    @Override
    public int hashCode() {
        return worldName.hashCode() * 31 * 31 + x * 31 + z;
    }
}