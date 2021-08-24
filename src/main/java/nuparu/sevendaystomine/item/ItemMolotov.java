package nuparu.sevendaystomine.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

public class ItemMolotov extends Item {

	public ItemMolotov(Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public UseAction getUseAnimation(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);

		playerIn.startUsingItem(handIn);
		return ActionResult.success(itemstack);
	}

	@Override
	public void releaseUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		int i = this.getUseDuration(stack) - timeLeft;

		if (i < 0)
			return;

		float f = getVelocity(i);
		if (f < 0.1f)
			return;
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity player = (PlayerEntity) entityLiving;
			if (!player.isCreative()) {
				stack.shrink(1);
			}

			worldIn.playSound((PlayerEntity) null, player.getX(), player.getY(), player.getZ(),
					SoundEvents.SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F,
					0.4F / (random.nextFloat() * 0.4F + 0.8F));

			if (!worldIn.isClientSide()) {
				/*EntityMolotov grenade = new EntityMolotov(worldIn, player);
				grenade.shoot(player, player.xRot, player.yRot, 0.0F, 1.5f*f , 0.5f);
				worldIn.addFreshEntity(grenade);*/
			}

		}
	}

	public static float getVelocity(int charge) {
		float f = (float) charge / 20.0F;
		f = (f * f + f * 2.0F) / 3.0F;

		if (f > 1.0F) {
			f = 1.0F;
		}

		return f;
	}
}
