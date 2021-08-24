package nuparu.sevendaystomine.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;

public class BlockLandmine extends BlockBase implements IScrapable {

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 2;

	private static final VoxelShape SHAPE = Block.box(0.25D*16, 0.0D, 0.25D*16, 0.75D*16, 0.1875D*16, 0.75D*16);

	public BlockLandmine() {
		super(AbstractBlock.Properties.of(Material.METAL).sound(SoundType.METAL).strength(1,1).noCollission());
	}

	@Override
	public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if(worldIn.isClientSide()) return;
		if (!(entityIn instanceof LivingEntity)) {
			return;
		}
		if (entityIn instanceof PlayerEntity) {
			if (((PlayerEntity) entityIn).isCreative() || ((PlayerEntity) entityIn).isSpectator()) {
				return;
			}
		}
		worldIn.setBlockAndUpdate(pos,Blocks.AIR.defaultBlockState());
		worldIn.explode(null, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 4, true,Explosion.Mode.DESTROY);
	}

	@Override
	public void onBlockExploded(BlockState state, World worldIn, BlockPos pos, Explosion explosionIn) {
		if(worldIn.isClientSide()) return;
		worldIn.explode(null, pos.getX() + 0.5, pos.getY() + 0.25, pos.getZ() + 0.5, 4, true,Explosion.Mode.DESTROY);
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		return SHAPE;
	}
	
	@Override
	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		BlockPos blockpos = p_196260_3_.below();
		BlockState blockstate = p_196260_2_.getBlockState(blockpos);
		return this.canSurviveOn(p_196260_2_, blockpos, blockstate);
	}

	private boolean canSurviveOn(IBlockReader p_235552_1_, BlockPos p_235552_2_, BlockState p_235552_3_) {
		return p_235552_3_.isFaceSturdy(p_235552_1_, p_235552_2_, Direction.UP) || p_235552_3_.is(Blocks.HOPPER);
	}

	@Override
	public void neighborChanged(BlockState p_220069_1_, World p_220069_2_, BlockPos p_220069_3_, Block p_220069_4_,
			BlockPos p_220069_5_, boolean p_220069_6_) {
		if (!p_220069_2_.isClientSide) {
			if (!p_220069_1_.canSurvive(p_220069_2_, p_220069_3_)) {
				dropResources(p_220069_1_, p_220069_2_, p_220069_3_);
				p_220069_2_.removeBlock(p_220069_3_, false);
			}

		}
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

}
