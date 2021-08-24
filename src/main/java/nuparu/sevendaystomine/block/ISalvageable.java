package nuparu.sevendaystomine.block;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISalvageable {

	public List<ItemStack> getItems(World world, BlockPos pos, BlockState oldState, PlayerEntity player);

	public SoundEvent getSound();
	
	public float getUpgradeRate(World world, BlockPos pos, BlockState state, PlayerEntity player);

	public void onSalvage(World world, BlockPos pos, BlockState oldState);

}