package nuparu.sevendaystomine.tileentity;

import java.util.Arrays;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.inventory.block.ContainerSmall;
import nuparu.sevendaystomine.inventory.block.ContainerTurretAdvanced;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.inventory.itemhandler.wraper.NameableCombinedInvWrapper;
import nuparu.sevendaystomine.item.ItemCircuit;

public class TileEntityTurretAdvanced extends TileEntityTurret {

	public TileEntityTurretAdvanced() {
		super(ModTileEntities.TURRET_ADVANCED.get());
	}

	@Override
	public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity player) {
		return ContainerTurretAdvanced.createContainerServerSide(windowID, playerInventory, this);
	}
	
	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(10, DEFAULT_NAME);
	}

	public boolean hasWhitelist() {
		ItemStack usb = this.getInventory().getStackInSlot(9);
		if (usb.isEmpty() || !(usb.getItem() instanceof ItemCircuit))
			return false;
		CompoundNBT nbt = usb.getOrCreateTag();
		if (nbt == null || !nbt.contains("data", Constants.NBT.TAG_STRING))
			return false;
		return true;
	}

	public boolean isOnWhitelist(String name) {
		if (!hasWhitelist())
			return false;

		ItemStack usb = this.getInventory().getStackInSlot(9);
		String whitelist = usb.getOrCreateTag().getString("data");

		String[] names = whitelist.split(" ");

		return Arrays.stream(names).anyMatch(name::equals);
	}

	public class AITurretTargetAdvanced extends AITurretTarget {
		public AITurretTargetAdvanced(TileEntityTurret te) {
			super(te);
		}

		@Override
		public void setTarget(Entity seenEntity) {
			if (seenEntity == te.target) {
				return;
			}
			if (seenEntity instanceof PlayerEntity
					&& (((PlayerEntity) seenEntity).isCreative() || ((PlayerEntity) seenEntity).isSpectator())) {
				return;
			}

			if (isOnWhitelist(seenEntity.getDisplayName().getString())) {
				return;
			}
			if (te.target == null || distanceSqrtTo(seenEntity) < distanceSqrtTo(te.target)) {
				te.target = seenEntity;
			}
		}
	}
}
