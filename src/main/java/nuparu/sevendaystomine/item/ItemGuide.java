package nuparu.sevendaystomine.item;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

public class ItemGuide extends Item {
	
	public ResourceLocation data;
	public ItemGuide(ResourceLocation data, Item.Properties properties) {
		super(properties);
		this.data = data;
	}
	
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		//playerIn.openGui(SevenDaysToMine.instance, 22, worldIn, (int)playerIn.getX(), (int)playerIn.getY(), (int)playerIn.getZ());
		SevenDaysToMine.proxy.openClientOnlyGui(3, stack);
		return ActionResult.success(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		TranslationTextComponent title = new TranslationTextComponent(
				this.getRegistryName().getPath() + ".title");
		TranslationTextComponent desc = new TranslationTextComponent(
				this.getRegistryName().getPath() + ".desc");
		tooltip.add(title);
		tooltip.add(desc);
	}
}
