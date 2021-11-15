package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.potions.Potions;

public class ItemAlcoholDrink extends ItemDrink {

	public ItemAlcoholDrink(Item.Properties properties, int thirst, int stamina) {
		super(properties, thirst, stamina);
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity living) {
		ItemStack result = super.finishUsingItem(stack, worldIn, living);
		if (!(living instanceof PlayerEntity))
			return result;
		PlayerEntity player = (PlayerEntity) living;

		if (worldIn.isClientSide())
			return result;

		EffectInstance effect = new EffectInstance(Potions.ALCOHOL_BUZZ.get(), 6000, 0, false, false);
		if(!player.hasEffect(Potions.ALCOHOL_POISON.get())){
			if(!player.hasEffect(Potions.DRUNK.get())){
				if(player.hasEffect(Potions.ALCOHOL_BUZZ.get())){
					EffectInstance buzzEffect = player.getEffect(Potions.ALCOHOL_BUZZ.get());
					if(random.nextInt(Math.max(1,6-(buzzEffect.getDuration()/10))) == 0){
						effect = new EffectInstance(Potions.DRUNK.get(), 3000, 0, false, false);
					}
				}
			}
			else {
				EffectInstance drunkEffect = player.getEffect(Potions.DRUNK.get());
				effect = drunkEffect;
				if(random.nextInt(Math.max(1,60-(drunkEffect.getDuration()/5))) == 0){
					effect = new EffectInstance(Potions.ALCOHOL_POISON.get(), 3000, 0, false, false);
				}
			}
		}
		effect.setCurativeItems(new ArrayList<ItemStack>());
		player.addEffect(effect);

		return result;
	}

}
