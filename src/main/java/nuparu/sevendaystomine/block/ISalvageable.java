package nuparu.sevendaystomine.block;

import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ISalvageable {

	ResourceLocation getSalvageLootTable();

	void setSalvageLootTable(ResourceLocation resourceLocation);

	SoundEvent getSound();
	
	float getUpgradeRate(World world, BlockPos pos, BlockState state, PlayerEntity player);

	void onSalvage(World world, BlockPos pos, BlockState oldState);

}