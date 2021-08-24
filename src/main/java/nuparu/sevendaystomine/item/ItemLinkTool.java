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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemLinkTool extends ItemScrapable {

	public ItemLinkTool() {
		super(new Item.Properties().tab(ModItemGroups.TAB_ELECTRICITY).stacksTo(1), EnumMaterial.COPPER, 1);
	}

	@Override
	public ActionResultType useOn(ItemUseContext context) {
		PlayerEntity player = context.getPlayer();
		Hand hand = context.getHand();
		BlockPos pos = context.getClickedPos();
		World worldIn = context.getLevel();

		ItemStack stack = player.getItemInHand(hand);

		TileEntity te = worldIn.getBlockEntity(pos);

		if (te == null || !(te instanceof INetwork))
			return ActionResultType.PASS;

		INetwork net = (INetwork) te;

		if (stack.getOrCreateTag() == null) {
			stack.setTag(new CompoundNBT());
		}

		if (player.isCrouching()) {
			stack.getOrCreateTag().putLong("from", pos.asLong());
			if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) {
				long l = stack.getOrCreateTag().getLong("to");
				BlockPos to = BlockPos.of(l);
				TileEntity te2 = worldIn.getBlockEntity(to);
				if (l != pos.asLong() && te2 != null && te2 instanceof INetwork) {
					net.connectTo((INetwork) te2);
					if (!player.isCreative()) {
						player.setItemInHand(hand, ItemStack.EMPTY);
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

				if (l != pos.asLong() && te2 != null && te2 instanceof INetwork) {
					net.connectTo((INetwork) te2);
					if (!player.isCreative()) {
						player.setItemInHand(hand, ItemStack.EMPTY);
					}
					stack.setTag(new CompoundNBT());
				}
			}
		}

		return ActionResultType.SUCCESS;
	}

	@Override
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		/*
		 * if (isSelected) { if (stack.getOrCreateTag() == null) return; if
		 * (stack.getOrCreateTag().contains("from", Constants.NBT.TAG_LONG)) { long l =
		 * stack.getOrCreateTag().getLong("from"); BlockPos from = BlockPos.of(l); if
		 * (entityIn.getDistance(from.getX(), from.getY(), from.getZ()) >
		 * ModConstants.MAXIMAL_LENGTH) { stack.setTag(new CompoundNBT());
		 * worldIn.playSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
		 * SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false); }
		 * else { TileEntity te = worldIn.getBlockEntity(from); if (te == null || !(te
		 * instanceof INetwork)) { stack.setTag(new CompoundNBT());
		 * worldIn.playSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
		 * SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false); }
		 * 
		 * }
		 * 
		 * }
		 * 
		 * if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) { long l =
		 * stack.getOrCreateTag().getLong("to"); BlockPos to = BlockPos.of(l); if
		 * (entityIn.getDistance(to.getX(), to.getY(), to.getZ()) >
		 * ModConstants.MAXIMAL_LENGTH) { stack.setTag(new CompoundNBT());
		 * worldIn.playSound(entityIn.getX(), entityIn.getY(), entityIn.getZ(),
		 * SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL, 1, 1, false); } }
		 * }
		 */
	}

	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
		/*
		 * if (stack.getOrCreateTag() != null) { if
		 * (stack.getOrCreateTag().contains("from", Constants.NBT.TAG_LONG)) { long l =
		 * stack.getOrCreateTag().getLong("from"); BlockPos from = BlockPos.of(l); if
		 * (entityItem.getDistance(from.getX(), from.getY(), from.getZ()) >
		 * ModConstants.MAXIMAL_LENGTH) { stack.setTag(new CompoundNBT());
		 * entityItem.level.playSound(entityItem.getX(), entityItem.getY(),
		 * entityItem.getZ(), SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL,
		 * 1, 1, false); }
		 * 
		 * }
		 * 
		 * if (stack.getOrCreateTag().contains("to", Constants.NBT.TAG_LONG)) { long l =
		 * stack.getOrCreateTag().getLong("to"); BlockPos to = BlockPos.of(l); if
		 * (entityItem.getDistance(to.getX(), to.getY(), to.getZ()) >
		 * ModConstants.MAXIMAL_LENGTH) { stack.setTag(new CompoundNBT());
		 * entityItem.level.playSound(entityItem.getX(), entityItem.getY(),
		 * entityItem.getZ(), SoundEvents.ENTITY_LEASHKNOT_BREAK, SoundCategory.NEUTRAL,
		 * 1, 1, false); } } }
		 */
		return false;
	}

}
