package nuparu.sevendaystomine.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.util.ModConstants;

public class ItemWire extends ItemScrapable {

	public ItemWire() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY), EnumMaterial.COPPER, 1);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		World worldIn = context.getLevel();
		BlockPos pos = context.getClickedPos();
		ItemStack stack = player.getItemInHand(hand);

		TileEntity te = worldIn.getBlockEntity(pos);

		if (te == null || !(te instanceof IVoltage))
			return ActionResultType.PASS;

		IVoltage voltage = (IVoltage) te;

		if (player.isCrouching()) {
			stack.getOrCreateTag().putLong("from", pos.asLong());
			if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("to");
				BlockPos to = BlockPos.of(l);
				TileEntity te2 = worldIn.getBlockEntity(to);
				if (l != pos.asLong() && te2 != null && te2 instanceof IVoltage) {
					ElectricConnection connection = new ElectricConnection(pos, BlockPos.of(l));
					System.out.println("FFFF " + voltage.canConnect(connection) + " " + ((IVoltage) te2).canConnect(connection) + " " + te2.getClass());
					if (voltage.canConnect(connection) && ((IVoltage) te2).canConnect(connection)) {
						voltage.tryToConnect(connection);
						((IVoltage) te2).tryToConnect(connection);
						if (!player.isCreative()) {
							player.setItemInHand(hand, ItemStack.EMPTY);
						}
					}
					stack.setTag(new CompoundNBT());
				}
			}
		} else {
			stack.getOrCreateTag().putLong("to", pos.asLong());
			if (stack.getOrCreateTag().contains("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("from");
				BlockPos from = BlockPos.of(l);
				TileEntity te2 = worldIn.getBlockEntity(from);

				if (l != pos.asLong() && te2 != null && te2 instanceof IVoltage) {
					ElectricConnection connection = new ElectricConnection(BlockPos.of(l), pos);

					System.out.println("FFFF " + voltage.canConnect(connection) + " "  + ((IVoltage) te2).canConnect(connection));
					if (voltage.canConnect(connection) && ((IVoltage) te2).canConnect(connection)) {
						voltage.tryToConnect(connection);
						((IVoltage) te2).tryToConnect(connection);
						if (!player.isCreative()) {
							player.setItemInHand(hand, ItemStack.EMPTY);
						}
					}
					stack.setTag(new CompoundNBT());
				}
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (isSelected) {
			if (stack.getOrCreateTag() == null)
				return;
			if (stack.getOrCreateTag().contains("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("from");
				BlockPos from = BlockPos.of(l);
				double dst = entityIn.distanceToSqr(from.getX(), from.getY(), from.getZ());
				if (dst > ModConstants.MAXIMAL_LENGTH) {
					stack.setTag(new CompoundNBT());
					worldIn.playLocalSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
							SoundEvents.LEASH_KNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				} else {
					TileEntity te = worldIn.getBlockEntity(from);
					if (te == null || !(te instanceof IVoltage)) {
						stack.setTag(new CompoundNBT());
						worldIn.playLocalSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
								SoundEvents.LEASH_KNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
					}

				}

			}

			if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("to");
				BlockPos to = BlockPos.of(l);
				double dst = entityIn.distanceToSqr(to.getX(), to.getY(), to.getZ());
				if (dst > ModConstants.MAXIMAL_LENGTH) {
					stack.setTag(new CompoundNBT());
					worldIn.playLocalSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
							SoundEvents.LEASH_KNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				}
			}
		}
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		if (stack.getOrCreateTag() != null) {
			if (stack.getOrCreateTag().contains("from", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("from");
				BlockPos from = BlockPos.of(l);
				if (entityItem.distanceToSqr(from.getX(), from.getY(), from.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTag(new CompoundNBT());
					entityItem.level.playLocalSound(entityItem.getX(), entityItem.getY(), entityItem.getZ(),
							SoundEvents.LEASH_KNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				}

			}

			if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("to");
				BlockPos to = BlockPos.of(l);
				if (entityItem.distanceToSqr(to.getX(), to.getY(), to.getZ()) > ModConstants.MAXIMAL_LENGTH) {
					stack.setTag(new CompoundNBT());
					entityItem.level.playLocalSound(entityItem.getX(), entityItem.getY(), entityItem.getZ(),
							SoundEvents.LEASH_KNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false);
				}
			}
		}

		return false;
	}

}
