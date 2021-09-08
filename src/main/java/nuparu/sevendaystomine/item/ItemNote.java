package nuparu.sevendaystomine.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.item.guide.BookDataManager;
import nuparu.sevendaystomine.util.book.BookData;

import javax.annotation.Nullable;
import java.util.List;

public class ItemNote extends Item {

	public ItemNote(Properties properties) {
		super(properties);
	}
	
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		SevenDaysToMine.proxy.openClientOnlyGui(4, stack);
		return ActionResult.success(stack);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip,
			ITooltipFlag flagIn) {
		ResourceLocation resourceLocation = this.getData(stack);


		String title = "Missing Data";
		String desc = "Missing Data";

		if(!resourceLocation.toString().equals("minecraft:empty")){
			BookData data = BookDataManager.instance.get(resourceLocation);
			if(data != null){
				if(data.getTitle() != null){
					title =data.getTitle();
				}
				if(data.getDesc() != null){
					desc =data.getDesc();
				}
			}
		}

		tooltip.add(new StringTextComponent(title));
		tooltip.add(new StringTextComponent(desc));
	}

	public static void setData(ItemStack stack, ResourceLocation data) {
		CompoundNBT nbt = stack.getOrCreateTag();
		nbt.putString("data", data.toString());
	}

	public ResourceLocation getData(ItemStack stack){
		CompoundNBT nbt = stack.getOrCreateTag();
		if(!nbt.contains("data", Constants.NBT.TAG_STRING)){
			return new ResourceLocation("minecraft:empty");
		}
		return new ResourceLocation(nbt.getString("data"));
	}
}
