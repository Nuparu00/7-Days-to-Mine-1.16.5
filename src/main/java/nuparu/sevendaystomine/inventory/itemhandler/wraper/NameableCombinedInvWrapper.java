package nuparu.sevendaystomine.inventory.itemhandler.wraper;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.INameable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import nuparu.sevendaystomine.inventory.itemhandler.IItemHandlerNameable;

public class NameableCombinedInvWrapper extends CombinedInvWrapper implements IItemHandlerNameable {

	private final INameable worldNameable;

	public NameableCombinedInvWrapper(INameable worldNameable, IItemHandlerModifiable... itemHandler) {
		super(itemHandler);
		this.worldNameable = worldNameable;
	}

	@Override
	public ITextComponent getName() {
		return worldNameable.getDisplayName();
	}

	@Override
	public boolean hasCustomName() {
		return worldNameable.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName() {
		return worldNameable.getDisplayName();
	}
}