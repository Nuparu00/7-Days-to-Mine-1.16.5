package nuparu.sevendaystomine.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import nuparu.sevendaystomine.entity.CarEntity;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemCarChassis extends Item {

	public ItemCarChassis() {
		super(new Item.Properties().tab(ModItemGroups.TAB_MATERIALS));
	}
	public ActionResultType useOn(ItemUseContext p_195939_1_) {
		World world = p_195939_1_.getLevel();
		if (!(world instanceof ServerWorld)) {
			return ActionResultType.SUCCESS;
		} else {
			ItemStack itemstack = p_195939_1_.getItemInHand();
			BlockPos blockpos = p_195939_1_.getClickedPos();
			Direction direction = p_195939_1_.getClickedFace();
			BlockState blockstate = world.getBlockState(blockpos);

			BlockPos blockpos1;
			if (blockstate.getCollisionShape(world, blockpos).isEmpty()) {
				blockpos1 = blockpos;
			} else {
				blockpos1 = blockpos.relative(direction);
			}

            /*EntityType<?> entitytype = ModEntities.MINIBIKE.get();
            if (entitytype.spawn((ServerWorld)world, itemstack, p_195939_1_.getPlayer(), blockpos1, SpawnReason.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP) != null) {
                itemstack.shrink(1);
            }*/

			CarEntity carEntity = new CarEntity(world);
			carEntity.setPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

			world.addFreshEntity(carEntity);
			carEntity.yRot = p_195939_1_.getPlayer().yRot;
			carEntity.yRotO = p_195939_1_.getPlayer().yRot;
			carEntity.yBodyRot = p_195939_1_.getPlayer().yRot;
			carEntity.yBodyRotO = p_195939_1_.getPlayer().yRot;
			carEntity.yHeadRot = p_195939_1_.getPlayer().yRot;
			carEntity.yHeadRotO = p_195939_1_.getPlayer().yRot;

			return ActionResultType.CONSUME;
		}
	}

	public ActionResult<ItemStack> use(World p_77659_1_, PlayerEntity p_77659_2_, Hand p_77659_3_) {
		ItemStack itemstack = p_77659_2_.getItemInHand(p_77659_3_);
		RayTraceResult raytraceresult = getPlayerPOVHitResult(p_77659_1_, p_77659_2_, RayTraceContext.FluidMode.SOURCE_ONLY);
		if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
			return ActionResult.pass(itemstack);
		} else if (!(p_77659_1_ instanceof ServerWorld)) {
			return ActionResult.success(itemstack);
		} else {
			BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult) raytraceresult;
			BlockPos blockpos = blockraytraceresult.getBlockPos();
			if (!(p_77659_1_.getBlockState(blockpos).getBlock() instanceof FlowingFluidBlock)) {
				return ActionResult.pass(itemstack);
			} else if (p_77659_1_.mayInteract(p_77659_2_, blockpos) && p_77659_2_.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
                /*EntityType<?> entitytype = ModEntities.MINIBIKE.get();
                if (entitytype.spawn((ServerWorld) p_77659_1_, itemstack, p_77659_2_, blockpos, SpawnReason.SPAWN_EGG, false, false) == null) {
                    return ActionResult.pass(itemstack);
                } else {*/

				CarEntity carEntity = new CarEntity(p_77659_1_);
				carEntity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());


				p_77659_1_.addFreshEntity(carEntity);
				carEntity.yRot = p_77659_2_.yRot;
				carEntity.yRotO = p_77659_2_.yRot;
				carEntity.yBodyRot = p_77659_2_.yRot;
				carEntity.yBodyRotO = p_77659_2_.yRot;
				carEntity.yHeadRot = p_77659_2_.yRot;
				carEntity.yHeadRotO = p_77659_2_.yRot;

				if (!p_77659_2_.abilities.instabuild) {
					itemstack.shrink(1);
				}

				p_77659_2_.awardStat(Stats.ITEM_USED.get(this));
				return ActionResult.consume(itemstack);
				//}
			} else {
				return ActionResult.fail(itemstack);
			}
		}
	}
}
