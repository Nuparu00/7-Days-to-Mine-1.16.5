package nuparu.sevendaystomine.item;

import net.minecraft.item.Item;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemMinibikeChassis extends ItemQuality {

	public ItemMinibikeChassis() {
		super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
	}
/*
	@Override
	public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
		ItemStack itemstack = playerIn.getItemInHand(handIn);
		float f = 1.0F;
		float f1 = playerIn.prevRotationPitch + (playerIn.xRot - playerIn.prevRotationPitch) * 1.0F;
		float f2 = playerIn.prevRotationYaw + (playerIn.yRot - playerIn.prevRotationYaw) * 1.0F;
		double d0 = playerIn.xOld + (playerIn.getX() - playerIn.xOld) * 1.0D;
		double d1 = playerIn.yOld + (playerIn.getY() - playerIn.yOld) * 1.0D + (double) playerIn.getEyeHeight();
		double d2 = playerIn.zOld + (playerIn.getZ() - playerIn.zOld) * 1.0D;
		Vector3d vec3d = new Vector3d(d0, d1, d2);
		float f3 = MathHelper.cos(-f2 * 0.017453292F - (float) Math.PI);
		float f4 = MathHelper.sin(-f2 * 0.017453292F - (float) Math.PI);
		float f5 = -MathHelper.cos(-f1 * 0.017453292F);
		float f6 = MathHelper.sin(-f1 * 0.017453292F);
		float f7 = f4 * f5;
		float f8 = f3 * f5;
		double d3 = 5.0D;
		Vector3d vec3d1 = vec3d.addVector((double) f7 * 5.0D, (double) f6 * 5.0D, (double) f8 * 5.0D);
		RayTraceResult raytraceresult = worldIn.rayTraceBlocks(vec3d, vec3d1, true);

		if (raytraceresult == null) {
			return new ActionResult<ItemStack>(ActionResult.PASS, itemstack);
		} else {
			Vector3d vec3d2 = playerIn.getLook(1.0F);
			boolean flag = false;
			List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity(playerIn, playerIn.getBoundingBox()
					.expand(vec3d2.x * 5.0D, vec3d2.y * 5.0D, vec3d2.z * 5.0D).grow(1.0D));

			for (int i = 0; i < list.size(); ++i) {
				Entity entity = list.get(i);

				if (entity.canBeCollidedWith()) {
					AxisAlignedBB axisalignedbb = entity.getBoundingBox()
							.grow((double) entity.getCollisionBorderSize());

					if (axisalignedbb.contains(vec3d)) {
						flag = true;
					}
				}
			}

			if (flag) {
				return new ActionResult<ItemStack>(ActionResult.PASS, itemstack);
			} else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
				return new ActionResult<ItemStack>(ActionResult.PASS, itemstack);
			} else {
				Block block = worldIn.getBlockState(raytraceresult.getBlockPos()).getBlock();
				boolean flag1 = block == Blocks.WATER || block == Blocks.FLOWING_WATER;

				EntityMinibike minibike = new EntityMinibike(worldIn, raytraceresult.hitVec.x,
						flag1 ? raytraceresult.hitVec.y - 0.12D : raytraceresult.hitVec.y, raytraceresult.hitVec.z);
				minibike.yRot = playerIn.yRot;

				if (!worldIn.getCollisionBoxes(minibike, minibike.getBoundingBox().grow(-0.1D)).isEmpty()) {
					return new ActionResult<ItemStack>(ActionResult.FAIL, itemstack);
				} else {
					if (!worldIn.isClientSide()) {
						worldIn.addFreshEntity(minibike);
					}

					if (!playerIn.capabilities.isCreativeMode) {
						itemstack.shrink(1);
					}

					playerIn.addStat(StatList.getObjectUseStats(this));
					return new ActionResult<ItemStack>(ActionResult.SUCCESS, itemstack);
				}
			}
		}
	}*/
}
