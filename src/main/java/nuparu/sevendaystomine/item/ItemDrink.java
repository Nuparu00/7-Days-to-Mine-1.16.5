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
import nuparu.sevendaystomine.config.ServerConfig;
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
		ItemStack original = stack.copy();
		ItemStack result = super.finishUsingItem(stack, worldIn, living).copy();
		if (!(living instanceof PlayerEntity))
			return result;
		PlayerEntity player = (PlayerEntity) living;
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		if (ServerConfig.thirstSystem.get()) {
			extendedPlayer.addThirst(thirst);
		}
		if (ServerConfig.staminaSystem.get()) {
			extendedPlayer.addStamina(stamina);
		}

		player.removeEffect(Potions.THIRST.get());
		if ((original.getDamageValue() + 1 >= original.getMaxDamage()) && this.hasContainerItem(result)) {
			ItemStack itemStack = this.getContainerItem(original).copy();
			if (!worldIn.isClientSide()) {
				if (!player.addItem(itemStack)) {
					player.drop(itemStack, false);
				}
			}
		}
		return result;
	}

	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.DRINK;
	}
}
