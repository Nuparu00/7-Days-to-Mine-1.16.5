package nuparu.sevendaystomine.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.inventory.item.ContainerBackpack;
import nuparu.sevendaystomine.inventory.item.ItemNamedContainerProvider;

import javax.annotation.Nullable;

public class ItemBackpack extends Item {

	public ItemBackpack() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_CLOTHING));
	}
	
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		if (!playerIn.isCrouching() && playerIn instanceof ServerPlayerEntity) {
			INamedContainerProvider namedContainerProvider = new ItemNamedContainerProvider(stack,stack.getHoverName()){
				@Nullable
				@Override
				public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
					return ContainerBackpack.createContainerServerSide(windowID, playerInventory, this.stack);
				}
			};
			if (namedContainerProvider != null) {
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerIn;
				NetworkHooks.openGui(serverPlayerEntity, namedContainerProvider, (packetBuffer) -> packetBuffer.writeItem(stack));
				return ActionResult.success(stack);
			}
		}
		return ActionResult.pass(stack);
	}

	/*
	@Override
	@Nullable
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT compound) {
		return new ExtendedInventoryProvider().setSize(9);
	}*/
/*
	@Nullable
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt)
	{
		return new ExtendedInventoryProvider().setSize(9);
	}*/

}
