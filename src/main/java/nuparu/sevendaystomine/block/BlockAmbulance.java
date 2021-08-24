package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.chunk.ChunkRenderCache;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.tileentity.TileEntityCar;
import nuparu.sevendaystomine.world.gen.city.Cars;

public class BlockAmbulance extends BlockCar {

	public BlockAmbulance() {
		super(AbstractBlock.Properties.of(Material.METAL).strength(5,20),new byte[][][] { { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } },
				{ { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 }, { 0, 0, 0 } } });
		//CityHelper.cars.add(this);
		Cars.addCar(1,this);
		lootTable = ModLootTables.MEDICAL_CABINET;
		special = true;
	}

	/*
	 * Have to cache this somewhere someday so we do not have to chec kall the
	 * blocks and tileentites each tick
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
			ISelectionContext p_220053_4_) {

		//The client's antialiasing seems to crash the gmes, as for some reason getBlockState sometimes fidnds a block with a negative ID, so we just return this
		if(world instanceof ChunkRenderCache){
			return VoxelShapes.box(0.25,0.25,0.25,0.75,0.75,0.75);
		}

		double height = 1;
		double width = 1;
		double length = 1;
		
		if(!(world.getBlockEntity(pos) instanceof TileEntityCar)) {
			return VoxelShapes.block();
		}
		
		if(world == null || pos == null) {
			return VoxelShapes.block();
		}

		TileEntityCar te = (TileEntityCar) world.getBlockEntity(pos);
		TileEntity teUp = world.getBlockEntity(pos.above());

		Direction facing = state.getValue(BlockCar.FACING);
		if (world.getBlockState(pos.below()).getBlock() instanceof BlockAmbulance
				&& (te == null || world.getBlockEntity(pos.below()) == null || ((TileEntityCar) world.getBlockEntity(pos.below())).getMaster() == te.getMaster())) {
			height = 1d;
			BlockState front = world.getBlockState(pos.relative(facing, 1));
			BlockState back = world.getBlockState(pos.relative(facing, -1));

			TileEntity teFront = world.getBlockEntity(pos.relative(facing, 1));
			TileEntity teBack = world.getBlockEntity(pos.relative(facing, -1));

			if (!(front.getBlock() instanceof BlockCar) || te == null || teFront == null
					|| ((TileEntityCar) teFront).getMaster() != te.getMaster()) {
				if (facing.getAxis() == Direction.Axis.X) {
					if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
						width = 0.5f;
					} else {
						width = -0.5f;

					}
				}else {
					if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
						length = 0.5f;
					} else {
						length = -0.5f;
					}
				}
			}

			else if (!(back.getBlock() instanceof BlockCar) || te == null || teBack == null
					|| ((TileEntityCar) teBack).getMaster() != te.getMaster()) {
				if (facing.getAxis() == Direction.Axis.X) {
					if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
						width = 0.5f;
					} else {
						width = -0.5f;

					}
				}
			}
		}

		Direction rightDirection = facing.getClockWise();
		BlockState right = world.getBlockState(pos.relative(rightDirection, 1));
		Direction leftDirection = facing.getCounterClockWise();
		BlockState left = world.getBlockState(pos.relative(leftDirection, 1));

		TileEntity teRight = world.getBlockEntity(pos.relative(rightDirection, 1));
		TileEntity teLeft = world.getBlockEntity(pos.relative(leftDirection, 1));

		if (!(right.getBlock() instanceof BlockCar) || te == null || teRight == null
				|| ((TileEntityCar) teRight).getMaster() != te.getMaster()) {
			if (rightDirection.getAxis() == Direction.Axis.X) {
				if (rightDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					width = 0.5f;
				} else {
					width = -0.5f;

				}
			} else {
				if (rightDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					length = 0.5f;
				} else {
					length = -0.5f;
				}
			}
		}

		if (!(left.getBlock() instanceof BlockCar) || te == null || teLeft == null
				|| ((TileEntityCar) teLeft).getMaster() != te.getMaster()) {
			if (leftDirection.getAxis() == Direction.Axis.X) {
				if (leftDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					width = 0.5f;
				} else {
					width = -0.5f;

				}
			} else {
				if (facing.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
					length = -0.5f;
				} else {
					length = 0.5f;

				}
			}
		}
		return Block.box(width > 0 ? 0 : -width*16, 0, length > 0 ? 0 : -length*16,
				width > 0 ? width*16 : 16, height*16, length > 0 ? length*16 : 16);
	}
}
