package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import nuparu.sevendaystomine.block.BlockWoodenLogSpike;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityWoodenLogSpike extends TileEntity {

	public int health = 1000;

	public TileEntityWoodenLogSpike() {
		super(ModTileEntities.LOG_SPIKES.get());
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.health = compound.getInt("health");
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("health", this.health);
		return compound;
	}

	public void dealDamage(int damage) {
		if (level.isClientSide())
			return;
		health -= damage;
		if (health <= 0) {
			BlockWoodenLogSpike.degradeBlock(worldPosition, level);
		}
	}
}
