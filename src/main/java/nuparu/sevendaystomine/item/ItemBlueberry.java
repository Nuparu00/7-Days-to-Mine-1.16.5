package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModFood;

public class ItemBlueberry extends BlockNamedItem implements net.minecraftforge.common.IPlantable {

	public ItemBlueberry() {
		super(ModBlocks.BLUEBERRY_PLANT.get(),new Item.Properties().stacksTo(64).food(ModFood.BLUEBERRY).tab(ItemGroup.TAB_FOOD));
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return ModBlocks.BLUEBERRY_PLANT.get().defaultBlockState();
	}
}