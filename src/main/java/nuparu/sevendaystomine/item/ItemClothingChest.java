package nuparu.sevendaystomine.item;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemClothingChest extends ItemClothing {

	public ItemClothingChest(boolean isDyeable, boolean hasOverlay, String fileName) {
		super(ModArmorMaterial.CLOTHING,EquipmentSlotType.CHEST, new Item.Properties().stacksTo(1));
		this.isDyeable = isDyeable;
		this.hasOverlay = hasOverlay;
		this.texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +".png");
		if(hasOverlay) {
			this.overlay = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +"_overlay.png");
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
			EquipmentSlotType armorSlot, A _default) {
		if (model == null) {
			model = new PlayerModel(0.3f, false);
		}
		return (A) model;
	}

}
