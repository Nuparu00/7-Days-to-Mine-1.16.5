package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import nuparu.sevendaystomine.item.EnumMaterial;

public class BlockLadderMetal extends LadderBlock implements IBlockBase {

	public BlockLadderMetal() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).noOcclusion());
	}

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 3;

	@Override
	public BlockItem createBlockItem() {
		 final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		 return new BlockItem(this, properties);
	}
	
	@Override
	public boolean isLadder(BlockState state, IWorldReader world, BlockPos pos, LivingEntity entity)
    {
        return true;
    }
}
