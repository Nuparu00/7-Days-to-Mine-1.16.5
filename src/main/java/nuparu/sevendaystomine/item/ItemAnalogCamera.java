package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.capability.IItemHandlerExtended;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SchedulePhotoMessage;

public class ItemAnalogCamera extends Item {

	public ItemAnalogCamera() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return ActionResult.success(itemstack);
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			int dur = this.getUseDuration(stack) - timeLeft;
			if (dur > 10) {
				IItemHandlerExtended inv = stack.getCapability(ExtendedInventoryProvider.EXTENDED_INV_CAP,
						Direction.UP).orElseGet(null);
				
				if (!worldIn.isClientSide()) {
					PacketManager.sendTo(PacketManager.schedulePhoto, new SchedulePhotoMessage(), (ServerPlayerEntity) player);
				}
				
				if (inv == null)
					return;
				ItemStack paper = inv.getStackInSlot(0);
				if (paper.isEmpty() || paper.getItem() != Items.PAPER) {
					worldIn.playSound(null, player.blockPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS,
							worldIn.random.nextFloat() * 0.2f + 0.9f, worldIn.random.nextFloat() * 0.2f + 1.9f);
					return;
				}
				if (CommonConfig.allowPhotos.get()) {
					if (!worldIn.isClientSide()) {
						PacketManager.sendTo(PacketManager.schedulePhoto, new SchedulePhotoMessage(), (ServerPlayerEntity) player);
					}
				}
				paper.shrink(1);
			} else if (player.isCrouching()) {
				/*player.openGui(SevenDaysToMine.instance, 27, worldIn, (int) player.getX(), (int) player.getY(),
						(int) player.getZ());*/
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack itemStack) {
		return 82000;
	}

	@Override
	public UseAction getUseAnimation(ItemStack itemStack) {
		return UseAction.BOW;
	}
/*
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compound) {
		return new ExtendedInventoryProvider().setSize(1);
	}*/

	@Override
	@OnlyIn(Dist.CLIENT)
	public void fillItemCategory(ItemGroup tab, NonNullList<ItemStack> items) {
		if (this.allowdedIn(tab)) {
			PlayerEntity player = Minecraft.getInstance().player;
			ItemStack stack = new ItemStack(this, 1);
			if (player != null) {
				setupDimensions(stack, player);
			}
			items.add(stack);
		}
	}

	public static ItemStack setupDimensions(ItemStack stack, @Nullable PlayerEntity player) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putDouble("width", 0.75);
		nbt.putDouble("height", 0.75);
		nbt.putDouble("zoom", 1);
		return stack;
	}

	public static double getWidth(ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		return stack.getOrCreateTag().getDouble("width");
	}

	public static double getHeight(ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		return stack.getOrCreateTag().getDouble("height");
	}

	public static double getZoom(ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		return stack.getOrCreateTag().getDouble("zoom");
	}

	public static void setWidth(double width, ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		stack.getOrCreateTag().putDouble("width", width);
	}

	public static void setHeight(double height, ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		stack.getOrCreateTag().putDouble("height", height);
	}

	public static void setZoom(double zoom, ItemStack stack, @Nullable PlayerEntity player) {
		if (stack.getOrCreateTag() == null) {
			setupDimensions(stack, player);
		}
		stack.getOrCreateTag().putDouble("zoom", zoom);
	}
}
