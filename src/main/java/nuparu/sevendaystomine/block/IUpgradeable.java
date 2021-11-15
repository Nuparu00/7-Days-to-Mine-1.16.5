package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public interface IUpgradeable {

ItemStack[] getItems();
SoundEvent getSound();
BlockState getPrev(IWorld world, BlockPos pos, BlockState original);
BlockState getResult(IWorld world, BlockPos pos);
void onUpgrade(IWorld world, BlockPos pos, BlockState oldState);
void onDowngrade(IWorld world, BlockPos pos, BlockState oldState);

}