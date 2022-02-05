package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.potions.Potions;

public class ItemCannedSoup extends ItemCannedFood {

	int thirst;
	int stamina;

	public ItemCannedSoup(Item.Properties properties, int thirst, int stamina) {
		super(properties);
		this.thirst = thirst;
		this.stamina = stamina;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if (!(living instanceof PlayerEntity))
			return stack;
		PlayerEntity player = (PlayerEntity) living;
		IExtendedPlayer extendedPlayer = CapabilityHelper.getExtendedPlayer(player);
		if (ServerConfig.thirstSystem.get()) {
			extendedPlayer.addThirst(thirst);
		}
		if (ServerConfig.staminaSystem.get()) {
			extendedPlayer.addStamina(stamina);
		}
		player.removeEffect(Potions.THIRST.get());
		return stack;
	}

}
