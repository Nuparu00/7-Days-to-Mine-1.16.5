package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
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

public class BlockBurntFrame extends BlockUpgradeable {

	public BlockBurntFrame() {
		super(AbstractBlock.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(0.8f,2f).harvestTool(ToolType.AXE).harvestLevel(0).noOcclusion());
	}
	@Override
	public BlockState getResult(World world, BlockPos pos) {
		return ModBlocks.BURNT_PLANKS.get().defaultBlockState();
	}
	
	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
	
}