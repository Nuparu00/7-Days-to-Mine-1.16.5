package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.init.ModSounds;

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
