package nuparu.sevendaystomine.item;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemNightVisionDevice extends ArmorItem {

	public ItemNightVisionDevice(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn,
			Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (!stack.isEmpty() && stack.getItem() instanceof ItemNightVisionDevice) {
			return SevenDaysToMine.MODID + ":textures/models/armor/night_vision_device.png";
		}
		return null;
	}
/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot,
			BipedModel original) {
		ModelNightVisionDevice model = new ModelNightVisionDevice();
		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemNightVisionDevice) {
			model.young = original.young;
			model.crouching = original.crouching;
			model.riding = original.riding;
			model.rightArmPose = original.rightArmPose;
			model.leftArmPose = original.leftArmPose;
		}

		return model;
	}
*/
	@Override
	public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
		EffectInstance effect_new = new EffectInstance(Effects.NIGHT_VISION, 240, 1, false, false);
		effect_new.setCurativeItems(new ArrayList<ItemStack>());
		player.addEffect(effect_new);
	}

}
