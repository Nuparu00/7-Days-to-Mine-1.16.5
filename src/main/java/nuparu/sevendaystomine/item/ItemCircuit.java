package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemCircuit extends Item {

	public ItemCircuit() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		String data = SevenDaysToMine.proxy.localize("stat.data.null");
		if(stack.getOrCreateTag() != null &&  stack.getOrCreateTag().contains("data")) {
			data = stack.getOrCreateTag().getString("data");
		}
		tooltip.add(new StringTextComponent(SevenDaysToMine.proxy.localize("stat.data",data)));
	}
}
