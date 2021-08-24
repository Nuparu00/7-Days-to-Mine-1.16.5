package nuparu.sevendaystomine.block;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.util.MathUtils;

public class BlockFruitBush extends BushBlock implements IGrowable, IBlockBase {

	public static final IntegerProperty AGE = BlockStateProperties.AGE_7;

	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(0.3125D * 16, 0.0D, 0.3125D * 16, 0.6875D * 16, 0.375D * 16, 0.6875D * 16),
			Block.box(0.25D * 16, 0.0D, 0.25D, 0.75D * 16, 0.5D * 16, 0.75D * 16),
			Block.box(0.21875D * 16, 0.0D, 0.21875D * 16, 0.78125D * 16, 0.5625D * 16, 0.78125D * 16),
			Block.box(0.1875D * 16, 0.0D, 0.1875D * 16, 0.8125D * 16, 0.625D * 16, 0.8125D * 16),
			Block.box(0.15625D * 16, 0.0D, 0.15625D * 16, 0.84375D * 16, 0.6875D * 16, 0.84375D * 16),
			Block.box(0.125D * 16, 0.0D, 0.125D * 16, 0.875D * 16, 0.75D * 16, 0.875D * 16),
			Block.box(0.09375D * 16, 0.0D, 0.09375D * 16, 0.90625D * 16, 0.8125D * 16, 0.90625D * 16),
			Block.box(0.03125D * 16, 0.0D, 0.03125D * 16, 0.96875D * 16, 0.9375D * 16, 0.96875D * 16) };

	public BlockFruitBush(AbstractBlock.Properties properties) {
		super(properties.noOcclusion());
	}

	@Override
	public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_,
			ISelectionContext p_220053_4_) {
		return SHAPE_BY_AGE[p_220053_1_.getValue(this.getAgeProperty())];
	}

	protected boolean mayPlaceOn(BlockState state, IBlockReader p_200014_2_, BlockPos p_200014_3_) {
		return state.is(Blocks.FARMLAND) || state.is(Blocks.DIRT) || state.is(Blocks.GRASS);
	}

	public IntegerProperty getAgeProperty() {
		return AGE;
	}

	public int getMaxAge() {
		return 7;
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

	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand,
			BlockRayTraceResult result) {
		if (!playerIn.isCrouching()) {
			int age = getAge(state);
			boolean shears = playerIn.getItemInHand(hand).getItem() == Items.SHEARS;
			if (age == getMaxAge()) {
				popResource(worldIn, pos, new ItemStack(getBaseSeedId(),shears ? 3 : 1));
				worldIn.setBlock(pos, getStateForAge(getMaxAge() - (shears ? 3 : 1)), 2);
				worldIn.playSound((PlayerEntity) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.GRASS_PLACE, SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f),
						MathUtils.getFloatInRange(1.2f, 1.5f));
				return ActionResultType.SUCCESS;
			} else if (age > 0 && shears) {
				worldIn.setBlock(pos, getStateForAge(age - 1), 2);
				InventoryHelper.dropItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(Items.STICK));
				if (playerIn instanceof ServerPlayerEntity) {
					playerIn.getItemInHand(hand).hurt(1, worldIn.random, (ServerPlayerEntity) playerIn);
				}
				worldIn.playSound((PlayerEntity) null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
						SoundEvents.SHEEP_SHEAR, SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.9f, 1.1f),
						MathUtils.getFloatInRange(0.95f, 1.05f));
			}
		}
		return ActionResultType.FAIL;
	}

	public void randomTick(BlockState p_225542_1_, ServerWorld p_225542_2_, BlockPos p_225542_3_, Random p_225542_4_) {
		if (!p_225542_2_.isAreaLoaded(p_225542_3_, 1))
			return; // Forge: prevent loading unloaded chunks when checking neighbor's light
		if (p_225542_2_.getRawBrightness(p_225542_3_, 0) >= 9) {
			int i = this.getAge(p_225542_1_);
			if (i < this.getMaxAge()) {
				float f = getGrowthSpeed(this, p_225542_2_, p_225542_3_);
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_225542_2_, p_225542_3_, p_225542_1_,
						p_225542_4_.nextInt((int) (25.0F / f) + 1) == 0)) {
					p_225542_2_.setBlock(p_225542_3_, this.getStateForAge(i + 1), 2);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_225542_2_, p_225542_3_, p_225542_1_);
				}
			}
		}

	}

	public void growCrops(World p_176487_1_, BlockPos p_176487_2_, BlockState p_176487_3_) {
		int i = this.getAge(p_176487_3_) + this.getBonemealAgeIncrease(p_176487_1_);
		int j = this.getMaxAge();
		if (i > j) {
			i = j;
		}

		p_176487_1_.setBlock(p_176487_2_, this.getStateForAge(i), 2);
	}

	protected int getBonemealAgeIncrease(World p_185529_1_) {
		return MathHelper.nextInt(p_185529_1_.random, 2, 5);
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

	public boolean canSurvive(BlockState p_196260_1_, IWorldReader p_196260_2_, BlockPos p_196260_3_) {
		return (p_196260_2_.getRawBrightness(p_196260_3_, 0) >= 8 || p_196260_2_.canSeeSky(p_196260_3_))
				&& super.canSurvive(p_196260_1_, p_196260_2_, p_196260_3_);
	}

	public void entityInside(BlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity p_196262_4_) {
		p_196262_4_.makeStuckInBlock(p_196262_1_, new Vector3d(0.25D, (double) 0.05F, 0.25D));
	}

	protected IItemProvider getBaseSeedId() {
		return Items.WHEAT_SEEDS;
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
	}

	@Override
	public BlockItem createBlockitem() {
		final Item.Properties properties = new Item.Properties().tab(getItemGroup());
		return new BlockItem(this, properties);
	}
}
