package nuparu.sevendaystomine.item;

import net.minecraft.block.material.Material;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.world.World;
import nuparu.sevendaystomine.init.ModItemGroups;
import nuparu.sevendaystomine.init.ModItems;

public class ItemEmptyCan extends Item {

	public ItemEmptyCan() {
		super(new Item.Properties().stacksTo(64).tab(ModItemGroups.TAB_MATERIALS));
	}

	public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {

		ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);

		RayTraceResult raytraceresult = getPlayerPOVHitResult(p_77659_1_, p_77659_2_,
				RayTraceContext.FluidMode.SOURCE_ONLY);
		if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
			return ActionResult.pass(itemstack);
		} else {
			if (raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
				BlockPos blockpos = ((BlockRayTraceResult) raytraceresult).getBlockPos();
				if (!p_77659_1_.mayInteract(p_77659_2_, blockpos)) {
					return ActionResult.pass(itemstack);
				}

				if (p_77659_1_.getFluidState(blockpos).is(FluidTags.WATER)) {
					p_77659_1_.playSound(p_77659_2_, p_77659_2_.getX(), p_77659_2_.getY(), p_77659_2_.getZ(),
							SoundEvents.BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
					return ActionResult.sidedSuccess(
							this.turnBottleIntoItem(itemstack, p_77659_2_, new ItemStack(ModItems.CANNED_MURKY_WATER.get())),
							p_77659_1_.isClientSide());
				}
			}

			return ActionResult.pass(itemstack);
		}

	}

	//Possible issue in here!!!
	protected ItemStack turnBottleIntoItem(ItemStack p_185061_1_, PlayerEntity p_185061_2_, ItemStack p_185061_3_) {
		p_185061_2_.awardStat(Stats.ITEM_USED.get(this));
		return p_185061_3_;
	}

}
