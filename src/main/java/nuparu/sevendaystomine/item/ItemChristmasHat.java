package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemChristmasHat extends ArmorItem {

	public ItemChristmasHat(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn,
			Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties);
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		if (!stack.isEmpty() && stack.getItem() instanceof ItemChristmasHat) {
			return SevenDaysToMine.MODID + ":textures/models/armor/christmas_hat.png";
		}
		return null;
	}
/*
	@Override
	@OnlyIn(Dist.CLIENT)
	public BipedModel getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlotType armorSlot,
			BipedModel original) {
		ModelChristmasHat model = new ModelChristmasHat();
		if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemChristmasHat) {
			model.young = original.young;
			model.crouching = original.crouching;
			model.riding = original.riding;
			model.rightArmPose = original.rightArmPose;
			model.leftArmPose = original.leftArmPose;
		}

		return model;
	}*/
}
