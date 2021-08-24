package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.potions.Potions;

public class ItemDrink extends Item {
	private int thirst;
	private int stamina;

	public ItemDrink(Item.Properties properties, int thirst, int stamina) {
		super(properties.tab(ItemGroup.TAB_FOOD));
		this.thirst = thirst;
		this.stamina = stamina;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if (living instanceof PlayerEntity)
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		if (CommonConfig.thirstSystem.get()) {
			extendedPlayer.addThirst(thirst);
		}
		if (CommonConfig.staminaSystem.get()) {
			extendedPlayer.addThirst(thirst);
			extendedPlayer.addStamina(stamina);
		}
		player.removeEffect(Potions.THIRST.get());
		if (stack.getDamageValue() + 1 == stack.getMaxDamage() && this.hasContainerItem(stack)) {
			ItemStack itemStack = this.getContainerItem(stack);
			return itemStack.copy();
		}
		return stack;
	}

	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.DRINK;
	}
}
