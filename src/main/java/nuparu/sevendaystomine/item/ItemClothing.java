package nuparu.sevendaystomine.item;

import javax.annotation.Nullable;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.util.ColorDetector;

public abstract class ItemClothing extends DyeableArmorItem implements IScrapable {

	public ResourceLocation texture;
	public ResourceLocation overlay;
	public float scale = 0f;
	public boolean isDyeable = false;
	public boolean hasOverlay = false;
	public int defaultColor = 16777215;

	private EnumMaterial material = EnumMaterial.CLOTH;
	private int weight = 2;

	public ItemClothing(IArmorMaterial materialIn, EquipmentSlotType equipmentSlotIn, Item.Properties properties) {
		super(materialIn, equipmentSlotIn, properties.tab(ModItemGroups.TAB_CLOTHING));
	}

	@Nullable
	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
		return SevenDaysToMine.MODID + ":textures/misc/empty.png";
	}

	@Override
	public boolean isValidRepairItem(ItemStack armor, ItemStack stack) {
		return stack.getItem() == ModItems.CLOTH.get();
	}

	@Override
	public int getColor(ItemStack p_200886_1_) {
		CompoundNBT compoundnbt = p_200886_1_.getTagElement("display");
		return compoundnbt != null && compoundnbt.contains("color", 99) ? compoundnbt.getInt("color") : 0xffffff;
	}

	@Override
	public ITextComponent getName(ItemStack itemstack) {
		ITextComponent textComponent = super.getName(itemstack);
		if (isDyeable) {
			int c = getColor(itemstack);
			float r = (float) (c >> 16 & 255);
			float g = (float) (c >> 8 & 255);
			float b = (float) (c & 255);
			System.out.println(r + " " + g + " " + b);
			String colorName = ColorDetector.INSTANCE.getColorMatch(r, g, b);
			if (colorName != null) {
				TranslationTextComponent stringTextComponent = new TranslationTextComponent(
						SevenDaysToMine.proxy.localize(colorName));
				return stringTextComponent.append(new StringTextComponent(" ")).append(textComponent);
			}

		}
		return textComponent;
	}

	@OnlyIn(Dist.CLIENT)
	protected PlayerModel<?> model;

	@SuppressWarnings("unchecked")
	@OnlyIn(Dist.CLIENT)
	public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack itemStack,
			EquipmentSlotType armorSlot, A _default) {
		if (model == null) {
			model = new PlayerModel(0.35f, false);
		}
		return (A) model;
	}

	public void setMaterial(EnumMaterial mat) {
		material = mat;
	}

	public EnumMaterial getItemMaterial() {
		return material;
	}

	public void setWeight(int newWeight) {
		weight = newWeight;
	}

	public int getWeight() {
		return weight;
	}

	public boolean canBeScraped() {
		return true;
	}

}
