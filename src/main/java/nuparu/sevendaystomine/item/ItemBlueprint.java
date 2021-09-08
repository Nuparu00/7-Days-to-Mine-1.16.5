package nuparu.sevendaystomine.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlueprint extends ItemRecipeBook {

	public ItemBlueprint(ResourceLocation data, String recipe, Item.Properties properties) {
		super(data, recipe,properties);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
								ITooltipFlag flagIn) {
		TranslationTextComponent title = new TranslationTextComponent(
				this.getRegistryName().getPath() + ".title");
		tooltip.add(title);
	}
}
