package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.init.ModSounds;

public class BlockPlanksReinforcedIron extends BlockUpgradeable {
	public BlockPlanksReinforcedIron(AbstractBlock.Properties properties) {
		super(properties);
	}
	
	@Override
	public SoundEvent getSound() {
		return ModSounds.UPGRADE_WOOD.get();
	}
	
}
