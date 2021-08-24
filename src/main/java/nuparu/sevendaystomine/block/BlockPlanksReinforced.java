package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModItems;

public class BlockPlanksReinforced extends BlockUpgradeable {
	public BlockPlanksReinforced(AbstractBlock.Properties properties, Block result) {
		super(properties);
		setResult(result.defaultBlockState());
	}
	
	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
}
