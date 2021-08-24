package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class BlockPlanksFrame extends BlockUpgradeable {

	public BlockPlanksFrame(AbstractBlock.Properties properties, Block result) {
		super(properties.noOcclusion());
		setResult(result.defaultBlockState());
	}

	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
	
}