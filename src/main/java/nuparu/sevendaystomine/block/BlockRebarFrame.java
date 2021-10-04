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
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockRebarFrame extends BlockUpgradeable {

	public BlockRebarFrame() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(2, 10)
				.harvestTool(ToolType.PICKAXE).harvestLevel(1).noOcclusion());

	}

	@Override
	public BlockState getResult(World world, BlockPos pos) {
		return ModBlocks.REBAR_FRAME_WOOD.get().defaultBlockState();
	}
	
	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
}
