package nuparu.sevendaystomine.block;

import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IUpgradeable {

public ItemStack[] getItems();
public SoundEvent getSound();
public BlockState getPrev(World world, BlockPos pos, BlockState original);
public BlockState getResult(World world, BlockPos pos);
public void onUpgrade(World world, BlockPos pos, BlockState oldState);
public void onDowngrade(World world, BlockPos pos, BlockState oldState);

}