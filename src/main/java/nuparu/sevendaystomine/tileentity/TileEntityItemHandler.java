package nuparu.sevendaystomine.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import nuparu.sevendaystomine.inventory.IContainerCallbacks;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.Utils;

public abstract class TileEntityItemHandler<INVENTORY extends ItemHandlerNameable>
		extends TileEntity implements IContainerCallbacks, INamedContainerProvider, ILootTableProvider {

	@Nullable
	protected ResourceLocation lootTable;
	protected long lootTableSeed;


	public TileEntityItemHandler(TileEntityType<?> p_i48289_1_) {
		super(p_i48289_1_);
	}

	protected final LazyOptional<INVENTORY> inventory = LazyOptional.of(this::createInventory);

	protected abstract INVENTORY createInventory();

	@Nullable
	public INVENTORY getInventory() {
		return this.inventory.orElse(null);
	}

	public NonNullList<ItemStack> getDrops() {
		if(this.lootTable != null){
			this.unpackLootTable(null);
		}
		return Utils.dropItemHandlerContents(getInventory(), level.random);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (!this.tryLoadLootTable(compound)) {
			if (getInventory() != null && compound.contains("ItemHandler")) {
				getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
			}
		}

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (!this.trySaveLootTable(compound)) {
			if (getInventory() != null) {
				compound.put("ItemHandler", getInventory().serializeNBT());
			}
		}

		return compound;
	}

	@Override
	@Nonnull
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if(this.lootTable != null){
				this.unpackLootTable(null);
			}
			return inventory.cast();
		}
		return super.getCapability(cap, side);
	}

	public ResourceLocation getLootTable() {
		return null;
	}

	public void setDisplayName(ITextComponent displayName) {
		getInventory().setDisplayName(displayName);
	}

	/*
	LOOT TABLE PART
	 */

	public boolean tryLoadLootTable(CompoundNBT p_184283_1_) {
		if (p_184283_1_.contains("LootTable", 8)) {
			this.lootTable = new ResourceLocation(p_184283_1_.getString("LootTable"));
			this.lootTableSeed = p_184283_1_.getLong("LootTableSeed");
			return true;
		} else {
			return false;
		}
	}

	public boolean trySaveLootTable(CompoundNBT p_184282_1_) {
		if (this.lootTable == null) {
			return false;
		} else {
			p_184282_1_.putString("LootTable", this.lootTable.toString());
			if (this.lootTableSeed != 0L) {
				p_184282_1_.putLong("LootTableSeed", this.lootTableSeed);
			}

			return true;
		}
	}

	public void unpackLootTable(@Nullable PlayerEntity p_184281_1_) {
		if (this.lootTable != null && this.level.getServer() != null) {
			LootTable loottable = this.level.getServer().getLootTables().get(this.lootTable);
			if (p_184281_1_ instanceof ServerPlayerEntity) {
				CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayerEntity)p_184281_1_, this.lootTable);
			}

			this.lootTable = null;
			LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.level)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(this.worldPosition)).withOptionalRandomSeed(this.lootTableSeed);
			if (p_184281_1_ != null) {
				lootcontext$builder.withLuck(p_184281_1_.getLuck()).withParameter(LootParameters.THIS_ENTITY, p_184281_1_);
			}

			ItemUtils.fill(loottable,this.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));
		}

	}

	public void setLootTable(ResourceLocation p_189404_1_, long p_189404_2_) {
		this.lootTable = p_189404_1_;
		this.lootTableSeed = p_189404_2_;
	}

	public void setLootTable(ResourceLocation p_189404_1_) {
		this.lootTable = p_189404_1_;
	}
}
