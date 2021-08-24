package nuparu.sevendaystomine.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

public class ItemHandlerNameable extends ItemStackHandler implements IItemHandlerNameable {

	private final ITextComponent defaultName;

	private ITextComponent displayName;

	public ItemHandlerNameable(ITextComponent defaultName) {
		this.defaultName = defaultName.copy();
	}

	public ItemHandlerNameable(int size, ITextComponent defaultName) {
		super(size);
		this.defaultName = defaultName.copy();
	}

	public ItemHandlerNameable(NonNullList<ItemStack> stacks, ITextComponent defaultName) {
		super(stacks);
		this.defaultName = defaultName.copy();
	}

	@Override
	public ITextComponent getName() {
		return getDisplayName();
	}

	@Override
	public boolean hasCustomName() {
		return displayName != null;
	}

	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? displayName : defaultName;
	}

	public void setDisplayName(ITextComponent displayName) {
		this.displayName = displayName.copy();
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT tagCompound = super.serializeNBT();

		if (hasCustomName()) {
			tagCompound.putString("DisplayName", ITextComponent.Serializer.toJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);

		if (nbt.contains("DisplayName")) {
			setDisplayName(ITextComponent.Serializer.fromJson(nbt.getString("DisplayName")));
		}
	}

}
