package nuparu.sevendaystomine.events;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class RenderHolsteredItemEvent extends Event {

	public enum EnumType {
		BACK, BACKPACK, LEFT_HIP, RIGHT_HIP
    }

	private PlayerEntity player;
	private ItemStack stack;
	private EnumType type;

	public RenderHolsteredItemEvent(PlayerEntity player, ItemStack stack, EnumType type) {
		this.player = player;
		this.stack = stack;
		this.type = type;
	}

	public PlayerEntity getPlayer() {
		return this.player;
	}

	public ItemStack getStack() {
		return this.stack;
	}

	public EnumType getType() {
		return this.type;
	}
}
