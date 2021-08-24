package nuparu.sevendaystomine.block;

import javax.annotation.Nullable;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityAirplaneRotor;

public class BlockAirplaneRotor extends BlockHorizontalBase implements IScrapable {

	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 2;

	public BlockAirplaneRotor(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
	}

	@Override
	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	@Override
	public EnumMaterial getItemMaterial() {
		return material;
	}

	@Override
	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	@Override
	public int getWeight() {
		return weight;
	}

	@Override
	public boolean canBeScraped() {
		return true;
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityAirplaneRotor();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (playerIn.isCrouching())
			return ActionResultType.FAIL;
		TileEntity te = worldIn.getBlockEntity(pos);
		if (te instanceof TileEntityAirplaneRotor && playerIn.getItemInHand(Hand.MAIN_HAND).isEmpty()) {
			((TileEntityAirplaneRotor) te).switchState();
			return ActionResultType.SUCCESS;
		}
		return ActionResultType.FAIL;
	}

}
