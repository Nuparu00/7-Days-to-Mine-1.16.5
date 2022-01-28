package nuparu.sevendaystomine.item;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

import nuparu.sevendaystomine.capability.CapabilityHelper;
import nuparu.sevendaystomine.capability.IExtendedPlayer;

public class ItemRecipeBook extends ItemGuide {
	
	public static final ArrayList<String> RECIPES = new ArrayList<String>();
	
	protected String recipe;

	public ItemRecipeBook(ResourceLocation data, String recipe, Item.Properties properties) {
		super(data,properties);
		this.recipe = recipe;
		RECIPES.add(recipe);
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		IExtendedPlayer iep = CapabilityHelper.getExtendedPlayer(playerIn);
		if (playerIn.isCrouching())
			return ActionResult.pass(stack);

		if (!iep.hasRecipe(recipe) && !isRead(stack)) {
			if (worldIn.isClientSide()) {
				SevenDaysToMine.proxy.openClientOnlyGui(2, stack);
			}
			iep.unlockRecipe(recipe);
			setRead(stack, true);
			
			if(!worldIn.isClientSide() && iep.getRecipes().containsAll(RECIPES)) {
				//ModTriggers.KNOW_IT_ALL.trigger((ServerPlayerEntity) playerIn);
			}
			
			return ActionResult.success(stack);
		} else {
			return super.use(worldIn, playerIn, handIn);
		}
	}

	public boolean isRead(ItemStack stack) {
		CompoundNBT nbt = stack.getOrCreateTag();
		if (nbt != null) {
			if (nbt.contains("read")) {
				return nbt.getBoolean("read");
			}
		}
		return false;
	}

	public void setRead(ItemStack stack, boolean read) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putBoolean("read", read);
	}

	public String getRecipe() {
		return this.recipe;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		if(!Screen.hasShiftDown()) return;
		if(Minecraft.getInstance().player == null) return;
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
