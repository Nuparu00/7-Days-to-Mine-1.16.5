package nuparu.sevendaystomine.world.prefab.buffered;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;

public class BufferedBlock {

    private int x;
    private int y;
    private int z;
    private String state;
    private Block block;
    private CompoundNBT nbt;
    protected String lootTable;

    public BufferedBlock(int x, int y, int z, Block block, String state) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setState(state);
        this.setBlock(block);
        this.setNBT(null);
        this.lootTable = null;
    }

    public BufferedBlock(int x, int y, int z, Block block, String state, CompoundNBT nbt) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setState(state);
        this.setBlock(block);
        this.setNBT(nbt);
        this.lootTable = null;
    }

    public BufferedBlock(int x, int y, int z, Block block, String state, CompoundNBT nbt, String lootTable) {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        this.setState(state);
        this.setBlock(block);
        this.setNBT(nbt);
        this.lootTable = lootTable;
    }

    public BlockState getBlockState() {
        if(getBlock() == null) { return null;}
        return getBlock().defaultBlockState();
    }
    public BlockPos getPos(BlockPos origin) {
        return origin.offset(getX(), getY(), getZ());
    }

    public CompoundNBT getNBT() {
        return nbt;
    }

    public BufferedBlock rotate(float angle) {
        if(angle % 360 == 0) return this;

        int x1 = (int) Math.round(x * Math.cos(Math.toRadians(angle)) - z * Math.sin(Math.toRadians(angle)));
        int z1 = (int) Math.round(x * Math.sin(Math.toRadians(angle)) + z * Math.cos(Math.toRadians(angle)));
        return new BufferedBlock(x1, y, z1, getBlock(), getState(), getNBT(), lootTable);
    }

    public BufferedBlock flipX() {
        return new BufferedBlock(-x, y, z, getBlock(), getState(), getNBT(), lootTable);
    }
    public BufferedBlock flipZ() {
        return new BufferedBlock(x, y, -z, getBlock(), getState(), getNBT(), lootTable);
    }
    public BufferedBlock flipY() {
        return new BufferedBlock(x, -y, z, getBlock(), getState(), getNBT(), lootTable);
    }

    public BufferedBlock copy() {
        return new BufferedBlock(getX(), getY(), getZ(), getBlock(), getState(), getNBT(), lootTable);
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setNBT(CompoundNBT nbt) {
        this.nbt = nbt;
    }
}
