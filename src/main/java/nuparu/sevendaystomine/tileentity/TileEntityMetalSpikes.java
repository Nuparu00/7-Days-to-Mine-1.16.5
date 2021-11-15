package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import nuparu.sevendaystomine.block.BlockMetalSpikes;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityMetalSpikes extends TileEntity {

    public int health = 900;
    private boolean retracted = true;

    public TileEntityMetalSpikes() {
        super(ModTileEntities.METAL_SPIKES.get());
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        this.health = compound.getInt("health");
        this.retracted = compound.getBoolean("retracted");
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putInt("health", this.health);
        compound.putBoolean("retracted", retracted);
        return compound;
    }

    public void dealDamage(int damage) {
        if (level.isClientSide())
            return;
        health -= damage;
        if (health <= 0) {
            level.destroyBlock(worldPosition, false);
        }
    }

    public void setRetracted(boolean state, BlockState oldState) {
        if (state != retracted) {
            retracted = state;
            level.playSound(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5,
                    SoundEvents.SHEEP_SHEAR, SoundCategory.BLOCKS, 1.5f, 0.5f);
            if (state && oldState.getBlock() == ModBlocks.METAL_SPIKES_EXTENDED.get()) {
                level.setBlockAndUpdate(worldPosition,
                        ModBlocks.METAL_SPIKES.get().defaultBlockState().setValue(BlockMetalSpikes.FACING,
                                oldState.getValue(BlockMetalSpikes.FACING)).setValue(BlockMetalSpikes.FACE, oldState.getValue(BlockMetalSpikes.FACE)));
            }
            else if (!state && oldState.getBlock() == ModBlocks.METAL_SPIKES.get()) {
                level.setBlockAndUpdate(worldPosition,
                        ModBlocks.METAL_SPIKES_EXTENDED.get().defaultBlockState().setValue(BlockMetalSpikes.FACING,
                                oldState.getValue(BlockMetalSpikes.FACING)).setValue(BlockMetalSpikes.FACE, oldState.getValue(BlockMetalSpikes.FACE)));
            }
            markForUpdate();
        }
    }

    public void markForUpdate() {
        level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
                level.getBlockState(worldPosition), 3);
        setChanged();
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbtTag = new CompoundNBT();
        this.save(nbtTag);
        return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT tag = pkt.getTag();
        load(level.getBlockState(pkt.getPos()), tag);
        if (hasLevel()) {
            level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
                    level.getBlockState(pkt.getPos()), 2);
        }
    }

    public boolean isRetracted() {
        return retracted;
    }
}
