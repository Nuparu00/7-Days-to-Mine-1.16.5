package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModFood;

public class ItemCorn extends BlockNamedItem implements net.minecraftforge.common.IPlantable {
	public ItemCorn() {
		super(ModBlocks.CORN_PLANT.get(),new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_FOOD).food(ModFood.BLUEBERRY));
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return ModBlocks.CORN_PLANT.get().defaultBlockState();
	}
}
