package nuparu.sevendaystomine.item;

import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemClothingLegs extends ItemClothing {

	public ItemClothingLegs(boolean isDyeable, boolean hasOverlay, String fileName) {
		super(ModArmorMaterial.CLOTHING,EquipmentSlotType.LEGS, new Item.Properties().stacksTo(1));
		this.isDyeable = isDyeable;
		this.hasOverlay = hasOverlay;
		this.texture = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +".png");
		if(hasOverlay) {
			this.overlay = new ResourceLocation(SevenDaysToMine.MODID,"textures/models/clothing/" + fileName +"_overlay.png");
		}
	}
	
	
	@OnlyIn(Dist.CLIENT)
	public PlayerModel getModel(PlayerEntity player, ItemStack stack) {
		if(model == null) {
			model = new PlayerModel(0.1f,false);
		}
		return model;
	}

}
