package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.item.*;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.init.ModItems;

public class BlockCornPlant extends BushBlock implements IGrowable,IBlockBase {

	 public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;
		public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

	public BlockCornPlant() {
		super(AbstractBlock.Properties.of(Material.PLANT).noCollission().instabreak().randomTicks().sound(SoundType.CROP));
		this.registerDefaultState(this.defaultBlockState().setValue(HALF, DoubleBlockHalf.LOWER).setValue(AGE, 0));
	}

	@Override
	 public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		if(!(p_196260_2_.getRawBrightness(p_196260_3_, 0) >= 8 || p_196260_2_.canSeeSky(p_196260_3_))) return false;
		if (p_196260_1_.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return super.canSurvive(p_196260_1_, p_196260_2_, p_196260_3_);
		} else {
			BlockState blockstate = p_196260_2_.getBlockState(p_196260_3_.below());
			if (p_196260_1_.getBlock() != this) return super.canSurvive(p_196260_1_, p_196260_2_, p_196260_3_); //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
			return blockstate.is(this) && blockstate.getValue(HALF) == DoubleBlockHalf.LOWER;
		}
	   }
	
	protected boolean mayPlaceOn(BlockState state, IBlockReader p_200014_2_, BlockPos p_200014_3_) {
		return state.is(Blocks.FARMLAND) || state.is(Blocks.DIRT) || state.is(Blocks.GRASS);
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 3;
	}

	protected int getAge(BlockState p_185527_1_) {
		return p_185527_1_.getValue(this.getAgeProperty());
	}

	public BlockState getStateForAge(int p_185528_1_) {
		return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(p_185528_1_));
	}

	public boolean isMaxAge(BlockState p_185525_1_) {
		return p_185525_1_.getValue(this.getAgeProperty()) >= this.getMaxAge();
	}

	public boolean isRandomlyTicking(BlockState p_149653_1_) {
		return !this.isMaxAge(p_149653_1_);
	}
	
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!world.isAreaLoaded(pos, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (world.getRawBrightness(pos, 0) >= 9) {
			int i = this.getAge(state);
			int j = this.getMaxAge();
			if (i < j) {
				float f = getGrowthSpeed(this, world, pos);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state,
						random.nextInt((int) (25.0F / f) + 1) == 0)) {
					DoubleBlockHalf half = state.getValue(HALF);

					int newAge = i+1;
					if(half == DoubleBlockHalf.LOWER){
						BlockState topState = world.getBlockState(pos.above());
						if(i <= 1){
							world.setBlock(pos, this.getStateForAge(newAge), 2);
						}
						else if(i==2) {

							if (!(topState.getBlock() instanceof BlockCornPlant)) {
								world.setBlock(pos, this.getStateForAge(i), 2);
								world.setBlock(pos.above(), this.getStateForAge(0).setValue(HALF, DoubleBlockHalf.UPPER), 2);
								System.out.println("XXX");
							} else {
								int topAge = getAge(topState);
								if (topAge == 2) {
									world.setBlock(pos, this.getStateForAge(newAge), 2);
									world.setBlock(pos.above(), this.getStateForAge(newAge).setValue(HALF, DoubleBlockHalf.UPPER), 2);
									System.out.println("FFF");
								}
							}
						}
					}
					else if (newAge < getMaxAge()){
						world.setBlock(pos, this.getStateForAge(newAge), 2);
						System.out.println("AAA");
					}
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state);
				}
			}
		}

	}

	public void growCrops(World world, BlockPos pos, BlockState state) {
		int increase = this.getBonemealAgeIncrease(world);;
		int i = this.getAge(state) + increase;
		int j = this.getMaxAge();

		if(i > j){
			i = j;
		}

		DoubleBlockHalf half = state.getValue(HALF);

		if(half == DoubleBlockHalf.LOWER){
			BlockState topState = world.getBlockState(pos.above());
			if(i==j-1 && topState.getBlock() != this){
				world.setBlock(pos, this.getStateForAge(i), 2);
				world.setBlock(pos.above(), this.getStateForAge(0).setValue(HALF,DoubleBlockHalf.UPPER), 2);
				return;
			}
			else if(topState.getBlock() == this){
				int topAge = getAge(topState);
				if(topAge == getMaxAge()-1){
					world.setBlock(pos, this.getStateForAge(i), 2);
					world.setBlock(pos.above(), this.getStateForAge(i).setValue(HALF,DoubleBlockHalf.UPPER), 2);
					return;
				}
				else{
					int topAgeNew = topAge+increase;
					if(topAgeNew == getMaxAge()){
						world.setBlock(pos, this.getStateForAge(topAgeNew), 2);
					}
					world.setBlock(pos.above(), this.getStateForAge(topAgeNew).setValue(HALF,DoubleBlockHalf.UPPER), 2);
					return;
				}
			}
		}
		else{
			BlockState lowerState = world.getBlockState(pos.below());
			if(lowerState.getBlock() != this){
				return;
			}
			growCrops(world,pos.below(),lowerState);
		}

	}

	protected int getBonemealAgeIncrease(World p_185529_1_) {
		return MathHelper.nextInt(p_185529_1_.random, 0,2);
	}

	protected static float getGrowthSpeed(Block p_180672_0_, IBlockReader p_180672_1_, BlockPos p_180672_2_) {
		float f = 1.0F;
		BlockPos blockpos = p_180672_2_.below();

		for (int i = -1; i <= 1; ++i) {
			for (int j = -1; j <= 1; ++j) {
				float f1 = 0.0F;
				BlockState blockstate = p_180672_1_.getBlockState(blockpos.offset(i, 0, j));
				if (blockstate.canSustainPlant(p_180672_1_, blockpos.offset(i, 0, j), net.minecraft.util.Direction.UP,
						(net.minecraftforge.common.IPlantable) p_180672_0_)) {
					f1 = 1.0F;
					if (blockstate.isFertile(p_180672_1_, p_180672_2_.offset(i, 0, j))) {
						f1 = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					f1 /= 4.0F;
				}

				f += f1;
			}
		}

		BlockPos blockpos1 = p_180672_2_.north();
		BlockPos blockpos2 = p_180672_2_.south();
		BlockPos blockpos3 = p_180672_2_.west();
		BlockPos blockpos4 = p_180672_2_.east();
		boolean flag = p_180672_0_ == p_180672_1_.getBlockState(blockpos3).getBlock()
				|| p_180672_0_ == p_180672_1_.getBlockState(blockpos4).getBlock();
		boolean flag1 = p_180672_0_ == p_180672_1_.getBlockState(blockpos1).getBlock()
				|| p_180672_0_ == p_180672_1_.getBlockState(blockpos2).getBlock();
		if (flag && flag1) {
			f /= 2.0F;
		} else {
			boolean flag2 = p_180672_0_ == p_180672_1_.getBlockState(blockpos3.north()).getBlock()
					|| p_180672_0_ == p_180672_1_.getBlockState(blockpos4.north()).getBlock()
					|| p_180672_0_ == p_180672_1_.getBlockState(blockpos4.south()).getBlock()
					|| p_180672_0_ == p_180672_1_.getBlockState(blockpos3.south()).getBlock();
			if (flag2) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity p_196262_4_) {
		if (p_196262_4_ instanceof RavagerEntity
				&& net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(p_196262_2_, p_196262_4_)) {
			p_196262_2_.destroyBlock(p_196262_3_, true, p_196262_4_);
		}

		super.entityInside(p_196262_1_, p_196262_2_, p_196262_3_, p_196262_4_);
	}

	protected IItemProvider getBaseSeedId() {
		return ModItems.CORN.get();
	}

	public ItemStack getCloneItemStack(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
		return new ItemStack(this.getBaseSeedId());
	}

	public boolean isValidBonemealTarget(IBlockReader p_176473_1_, BlockPos p_176473_2_, BlockState p_176473_3_,
			boolean p_176473_4_) {
		return !this.isMaxAge(p_176473_3_);
	}

	public boolean isBonemealSuccess(World p_180670_1_, Random p_180670_2_, BlockPos p_180670_3_,
			BlockState p_180670_4_) {
		return true;
	}

	public void performBonemeal(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_,
			BlockState p_225535_4_) {
		this.growCrops(p_225535_1_, p_225535_3_, p_225535_4_);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
		p_206840_1_.add(AGE);
		p_206840_1_.add(HALF);
	}

	@Override
	public BlockItem createBlockItem() {
		final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		return new BlockItem(this, properties);
	}
}
