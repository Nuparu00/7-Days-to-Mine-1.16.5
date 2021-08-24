package nuparu.sevendaystomine.block;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.item.EnumMaterial;
import nuparu.sevendaystomine.item.IScrapable;
import nuparu.sevendaystomine.tileentity.TileEntityCodeSafe;

public class BlockCodeSafe extends BlockBase implements IScrapable, IWaterLoggable {

	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final DirectionProperty FACING = DirectionalBlock.FACING;
	public static final BooleanProperty LOCKED = BooleanProperty.create("locked");

	private EnumMaterial material = EnumMaterial.IRON;
	private int weight = 2;

	public BlockCodeSafe(AbstractBlock.Properties properties) {
		super(properties);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.SOUTH).setValue(LOCKED, true).setValue(WATERLOGGED, Boolean.valueOf(false)));
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
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		if (tileentity instanceof TileEntityCodeSafe) {

			TileEntityCodeSafe safe = (TileEntityCodeSafe) tileentity;
			CompoundNBT nbt = stack.getOrCreateTag();
			if (nbt != null && nbt.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
				CompoundNBT tileNBT = nbt.getCompound("BlockEntityTag");
				tileNBT.putInt("x", pos.getX());
				tileNBT.putInt("y", pos.getY());
				tileNBT.putInt("z", pos.getZ());
				safe.load(state, tileNBT);
			}
			if (stack.hasCustomHoverName()) {
				String displayName = stack.getHoverName().getString();
				if (isNumeric(displayName)) {
					double d = Double.parseDouble(displayName);
					if ((d % 1) == 0 && d >= 0 && d < 1000) {
						safe.setCorrectCode((int) d);
						int selectedCode = 0;
						while ((int) d == selectedCode) {
							Random random = worldIn.random;
							selectedCode = random.nextInt(1000);
						}
						safe.setSelectedCode(selectedCode, null);
						safe.setInit(true);
						return;
					}
				}
				safe.setCustomInventoryName(displayName);
			}
		}
	}

	public static boolean isNumeric(String str) {
		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public static void setState(boolean locked, World worldIn, BlockPos pos) {
		TileEntity tileentity = worldIn.getBlockEntity(pos);
		BlockState BlockState = worldIn.getBlockState(pos);

		worldIn.setBlock(pos, ModBlocks.CODE_SAFE.get().defaultBlockState()
				.setValue(FACING, BlockState.getValue(FACING)).setValue(LOCKED, locked), 3);
		worldIn.sendBlockUpdated(pos, BlockState, worldIn.getBlockState(pos), 2);

		if (tileentity != null) {
			worldIn.setBlockEntity(pos, tileentity);
		}
	}

	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack p_190948_1_, @Nullable IBlockReader p_190948_2_,
			List<ITextComponent> p_190948_3_, ITooltipFlag p_190948_4_) {
		super.appendHoverText(p_190948_1_, p_190948_2_, p_190948_3_, p_190948_4_);
		CompoundNBT compoundnbt = p_190948_1_.getTagElement("BlockEntityTag");
		if (compoundnbt != null) {
			if (compoundnbt.contains("LootTable", 8)) {
				p_190948_3_.add(new StringTextComponent("???????"));
			}

			if (compoundnbt.contains("Items", 9)) {
				NonNullList<ItemStack> nonnulllist = NonNullList.withSize(27, ItemStack.EMPTY);
				ItemStackHelper.loadAllItems(compoundnbt, nonnulllist);
				int i = 0;
				int j = 0;

				for (ItemStack itemstack : nonnulllist) {
					if (!itemstack.isEmpty()) {
						++j;
						if (i <= 4) {
							++i;
							IFormattableTextComponent iformattabletextcomponent = itemstack.getHoverName().copy();
							iformattabletextcomponent.append(" x").append(String.valueOf(itemstack.getCount()));
							p_190948_3_.add(iformattabletextcomponent);
						}
					}
				}

				if (j - i > 0) {
					p_190948_3_.add((new TranslationTextComponent("container.shulkerBox.more", j - i))
							.withStyle(TextFormatting.ITALIC));
				}
			}
		}

	}

	@Override
	@Nullable
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new TileEntityCodeSafe();
	}

	@Override
	public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand,
			BlockRayTraceResult rayTraceResult) {
		if (worldIn.isClientSide())
			return ActionResultType.SUCCESS;

		INamedContainerProvider namedContainerProvider = this.getMenuProvider(state, worldIn, pos);
		if (namedContainerProvider != null) {
			if (!(player instanceof ServerPlayerEntity))
				return ActionResultType.FAIL;
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) player;
			NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> {
			});
		}
		return ActionResultType.SUCCESS;
	}

	@Nullable
	public INamedContainerProvider getMenuProvider(BlockState p_220052_1_, World p_220052_2_, BlockPos p_220052_3_) {
		TileEntity tileentity = p_220052_2_.getBlockEntity(p_220052_3_);
		return tileentity instanceof INamedContainerProvider ? (INamedContainerProvider) tileentity : null;
	}

	@Override
	public void onRemove(BlockState state, World world, BlockPos blockPos, BlockState newState, boolean isMoving) {

		if (state.getBlock() != newState.getBlock()) {
			TileEntity tileentity = world.getBlockEntity(blockPos);

			if (!world.isClientSide()) {
				if (tileentity instanceof TileEntityCodeSafe) {
					{
						TileEntityCodeSafe codeSafe = (TileEntityCodeSafe) tileentity;
						ItemStack itemstack = new ItemStack(Item.byBlock(this));
						if (codeSafe.locked) {
							CompoundNBT CompoundNBT = new CompoundNBT();
							CompoundNBT nbttagcompound1 = new CompoundNBT();
							CompoundNBT.put("BlockEntityTag", codeSafe.save(nbttagcompound1));
							itemstack.setTag(CompoundNBT);
						} else {
							InventoryHelper.dropContents(world, blockPos, codeSafe.getDrops());
						}

					}
				}
			}
		}

		super.onRemove(state, world, blockPos, newState, isMoving);
	}

	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(FACING, LOCKED, WATERLOGGED);
	}
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite()).setValue(WATERLOGGED, Boolean.valueOf(fluidstate.getType() == Fluids.WATER));
	}


	@Override
	public BlockState updateShape(BlockState p_196271_1_, Direction p_196271_2_, BlockState p_196271_3_, IWorld p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
		if (p_196271_1_.getValue(WATERLOGGED)) {
			p_196271_4_.getLiquidTicks().scheduleTick(p_196271_5_, Fluids.WATER, Fluids.WATER.getTickDelay(p_196271_4_));
		}

		return super.updateShape(p_196271_1_, p_196271_2_, p_196271_3_, p_196271_4_, p_196271_5_, p_196271_6_);
	}

	@Override
	public FluidState getFluidState(BlockState p_204507_1_) {
		return p_204507_1_.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(p_204507_1_);
	}
}