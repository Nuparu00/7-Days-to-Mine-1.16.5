package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;
import nuparu.sevendaystomine.init.ModFood;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.potions.Potions;
import nuparu.sevendaystomine.util.PlayerUtils;

public class ItemAntibiotics extends Item{
	public ItemAntibiotics() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_MEDICINE).food(ModFood.ANTIBIOTICS));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		stack = super.finishUsingItem(stack, worldIn, living);
		if(living instanceof PlayerEntity)return stack;
		PlayerEntity player = (PlayerEntity)living;
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(player);
		int time = iep.getInfectionTime();
		int stage = PlayerUtils.getInfectionStage(time);
		if (player instanceof ServerPlayerEntity && (stage >= PlayerUtils.getNumberOfstages()-1)) {
			//ModTriggers.CURE.trigger((ServerPlayerEntity) player);
		}
		player.removeEffect(Potions.INFECTION.get());
		iep.setInfectionTime(-1);
		return stack;
	}
}
