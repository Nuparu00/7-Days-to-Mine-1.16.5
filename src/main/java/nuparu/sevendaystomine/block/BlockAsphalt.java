package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;

public class BlockAsphalt extends BlockBase {
	public static final BooleanProperty CITY = BooleanProperty.create("city");

	public BlockAsphalt() {
		super(AbstractBlock.Properties.of(Material.STONE).strength(7, 15));
		this.registerDefaultState(this.defaultBlockState().setValue(CITY, false));
	}

	public static boolean isCityAsphalt(BlockState state) {
		return state.getBlock() instanceof BlockAsphalt && state.getValue(CITY);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		return this.defaultBlockState().setValue(CITY, false);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(CITY);
	}
}
