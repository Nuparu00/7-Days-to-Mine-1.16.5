package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModFood;

public class ItemBaneberry extends BlockNamedItem implements net.minecraftforge.common.IPlantable {

	public ItemBaneberry() {
		super(ModBlocks.BANEBERRY_PLANT.get(),new Item.Properties().stacksTo(64).tab(ItemGroup.TAB_FOOD).food(ModFood.BANEBERRY).tab(ItemGroup.TAB_FOOD));
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos) {
		return ModBlocks.BANEBERRY_PLANT.get().defaultBlockState();
	}
}
