package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockUpgradeable extends BlockBase implements IUpgradeable {

	private ItemStack[] items = new ItemStack[0];
	private SoundEvent sound = null;
	private BlockState result = null;
	private BlockState prev = Blocks.AIR.defaultBlockState();

	public BlockUpgradeable(AbstractBlock.Properties properties)
    {
        super(properties);
    }

	public ItemStack[] getItems() {
		return items;
	}

	public SoundEvent getSound() {
		return sound;
	}

	public BlockState getResult(IWorld world, BlockPos pos) {
		return result;
	}

	public BlockUpgradeable setItems(ItemStack[] items) {
		this.items = items;
		return this;
	}

	public BlockUpgradeable setSound(SoundEvent sound) {
		this.sound = sound;
		return this;
	}

	public BlockUpgradeable setResult(BlockState result) {
		this.result = result;
		return this;    
	}

	public BlockState getPrev(IWorld world, BlockPos pos, BlockState original) {
		return prev;
	}

	public void setPrev(BlockState prev) {
		this.prev = prev;
	}

	@Override
	public void onUpgrade(IWorld world, BlockPos pos, BlockState oldState) {

	}

	@Override
	public void onDowngrade(IWorld world, BlockPos pos, BlockState oldState) {
		
	}

}
