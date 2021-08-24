package nuparu.sevendaystomine.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;

@OnlyIn(value = Dist.CLIENT, _interface = IChestLid.class)
public class TileEntityOldChest extends TileEntityItemHandler<ItemHandlerNameable>
		implements IChestLid, ITickableTileEntity {

	private static final int INVENTORY_SIZE = 9;
	private static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.corpse");

	protected float openness;
	protected float oOpenness;
	protected int openCount;
	private int tickInterval;

	public TileEntityOldChest() {
		super(ModTileEntities.TRASH_CAN.get());
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(INVENTORY_SIZE, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {

	}

	@Override
	public void onContainerClosed(PlayerEntity player) {

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this
				&& player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
						this.worldPosition.getZ() + 0.5) <= 64;
	}

	public void setDisplayName(String displayName) {
		getInventory().setDisplayName(new StringTextComponent(displayName));
	}

	@Override
	public ResourceLocation getLootTable() {
		return null;
	}

	@Override
	public ITextComponent getDisplayName() {
		return ((ItemHandlerNameable) this.getInventory()).getDisplayName();
	}

	@Override
	public ChestContainer createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_,
			PlayerEntity p_createMenu_3_) {
		return null;
	}

	public void tick() {
		int i = this.worldPosition.getX();
		int j = this.worldPosition.getY();
		int k = this.worldPosition.getZ();
		++this.tickInterval;
		this.openCount = getOpenCount(this.level, this, this.tickInterval, i, j, k, this.openCount);
		this.oOpenness = this.openness;
		float f = 0.1F;
		if (this.openCount > 0 && this.openness == 0.0F) {
			this.playSound(SoundEvents.CHEST_OPEN);
		}

		if (this.openCount == 0 && this.openness > 0.0F || this.openCount > 0 && this.openness < 1.0F) {
			float f1 = this.openness;
			if (this.openCount > 0) {
				this.openness += 0.1F;
			} else {
				this.openness -= 0.1F;
			}

			if (this.openness > 1.0F) {
				this.openness = 1.0F;
			}

			float f2 = 0.5F;
			if (this.openness < 0.5F && f1 >= 0.5F) {
				this.playSound(SoundEvents.CHEST_CLOSE);
			}

			if (this.openness < 0.0F) {
				this.openness = 0.0F;
			}
		}

	}

	public static int getOpenCount(World p_213977_0_, TileEntityOldChest p_213977_1_, int p_213977_2_, int p_213977_3_,
			int p_213977_4_, int p_213977_5_, int p_213977_6_) {
		if (!p_213977_0_.isClientSide && p_213977_6_ != 0
				&& (p_213977_2_ + p_213977_3_ + p_213977_4_ + p_213977_5_) % 200 == 0) {
			p_213977_6_ = getOpenCount(p_213977_0_, p_213977_1_, p_213977_3_, p_213977_4_, p_213977_5_);
		}

		return p_213977_6_;
	}

	public static int getOpenCount(World p_213976_0_, TileEntityOldChest p_213976_1_, int p_213976_2_, int p_213976_3_,
			int p_213976_4_) {
		int i = 0;
		float f = 5.0F;
		/*
		 * for(PlayerEntity playerentity :
		 * p_213976_0_.getEntitiesOfClass(PlayerEntity.class, new
		 * AxisAlignedBB((double)((float)p_213976_2_ - 5.0F),
		 * (double)((float)p_213976_3_ - 5.0F), (double)((float)p_213976_4_ - 5.0F),
		 * (double)((float)(p_213976_2_ + 1) + 5.0F), (double)((float)(p_213976_3_ + 1)
		 * + 5.0F), (double)((float)(p_213976_4_ + 1) + 5.0F)))) { if
		 * (playerentity.containerMenu instanceof ChestContainer) { IInventory
		 * iinventory = ((ChestContainer)playerentity.containerMenu).getContainer(); if
		 * (iinventory == p_213976_1_ || iinventory instanceof DoubleSidedInventory &&
		 * ((DoubleSidedInventory)iinventory).contains(p_213976_1_)) { ++i; } } }
		 */

		return i;
	}

	private void playSound(SoundEvent p_195483_1_) {
		ChestType chesttype = this.getBlockState().getValue(ChestBlock.TYPE);
		if (chesttype != ChestType.LEFT) {
			double d0 = (double) this.worldPosition.getX() + 0.5D;
			double d1 = (double) this.worldPosition.getY() + 0.5D;
			double d2 = (double) this.worldPosition.getZ() + 0.5D;
			if (chesttype == ChestType.RIGHT) {
				Direction direction = ChestBlock.getConnectedDirection(this.getBlockState());
				d0 += (double) direction.getStepX() * 0.5D;
				d2 += (double) direction.getStepZ() * 0.5D;
			}

			this.level.playSound((PlayerEntity) null, d0, d1, d2, p_195483_1_, SoundCategory.BLOCKS, 0.5F,
					this.level.random.nextFloat() * 0.1F + 0.9F);
		}
	}

	public boolean triggerEvent(int p_145842_1_, int p_145842_2_) {
		if (p_145842_1_ == 1) {
			this.openCount = p_145842_2_;
			return true;
		} else {
			return super.triggerEvent(p_145842_1_, p_145842_2_);
		}
	}

	public void startOpen(PlayerEntity p_174889_1_) {
		if (!p_174889_1_.isSpectator()) {
			if (this.openCount < 0) {
				this.openCount = 0;
			}

			++this.openCount;
			this.signalOpenCount();
		}

	}

	public void stopOpen(PlayerEntity p_174886_1_) {
		if (!p_174886_1_.isSpectator()) {
			--this.openCount;
			this.signalOpenCount();
		}

	}

	protected void signalOpenCount() {
		Block block = this.getBlockState().getBlock();
		if (block instanceof ChestBlock) {
			this.level.blockEvent(this.worldPosition, block, 1, this.openCount);
			this.level.updateNeighborsAt(this.worldPosition, block);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public float getOpenNess(float p_195480_1_) {
		return MathHelper.lerp(p_195480_1_, this.oOpenness, this.openness);
	}

	public static int getOpenCount(IBlockReader p_195481_0_, BlockPos p_195481_1_) {
		BlockState blockstate = p_195481_0_.getBlockState(p_195481_1_);
		if (blockstate.hasTileEntity()) {
			TileEntity tileentity = p_195481_0_.getBlockEntity(p_195481_1_);
			if (tileentity instanceof TileEntityOldChest) {
				return ((TileEntityOldChest) tileentity).openCount;
			}
		}

		return 0;
	}

}