package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModItems;

public class BlockBurntPlanks extends BlockUpgradeable {
	public BlockBurntPlanks() {
		super(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2,5f).harvestTool(ToolType.AXE).harvestLevel(0));
		setResult(Blocks.OAK_PLANKS.defaultBlockState());
	}

	@Override
	public BlockState getPrev(World world, BlockPos pos, BlockState original) {
		return ModBlocks.BURNT_FRAME.get().defaultBlockState();
	}
	
	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
}
