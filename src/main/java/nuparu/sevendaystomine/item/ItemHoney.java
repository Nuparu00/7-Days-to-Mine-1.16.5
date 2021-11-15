package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;

@Deprecated
public class ItemHoney extends ItemDrink {

	public ItemHoney(Item.Properties properties, int thirst, int stamina) {
		super(properties,thirst,stamina);
	}
/*
	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, PlayerEntity player) {
		super.onFoodEaten(stack, worldIn, player);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		if (player instanceof ServerPlayerEntity && PlayerUtils.getInfectionStage(time) == 0) {
			player.removeEffect(Potions.INFECTION.get());
			iep.setInfectionTime(-1);
		}
	}*/
}
