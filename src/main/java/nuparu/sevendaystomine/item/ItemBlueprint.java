package nuparu.sevendaystomine.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.capability.CapabilityHelper;

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
		if (!Screen.hasShiftDown()) return;
		if (Minecraft.getInstance().player == null) return;
		boolean known = CapabilityHelper.getExtendedPlayer(Minecraft.getInstance().player).hasRecipe(recipe);
		boolean read = isRead(stack);
		TranslationTextComponent knownText = new TranslationTextComponent(known ? "stat.known" : "stat.unknown");
		TranslationTextComponent readText = new TranslationTextComponent(read ? "stat.used" : "stat.unused");
		Style knownStyle = knownText.getStyle().withColor(known ? TextFormatting.GREEN : TextFormatting.RED);
		knownText.setStyle(knownStyle);

		Style readStyle = knownText.getStyle().withColor(read ? TextFormatting.GREEN : TextFormatting.RED);
		readText.setStyle(readStyle);

		tooltip.add(knownText);
		tooltip.add(readText);
	}
}
