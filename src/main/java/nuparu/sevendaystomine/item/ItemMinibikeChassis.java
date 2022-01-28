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
import nuparu.sevendaystomine.entity.MinibikeEntity;
import nuparu.sevendaystomine.init.ModItemGroups;

public class ItemMinibikeChassis extends ItemQuality {

    public ItemMinibikeChassis() {
        super(new Item.Properties().stacksTo(1).tab(ModItemGroups.TAB_ELECTRICITY));
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

            MinibikeEntity minibikeEntity = new MinibikeEntity(world);
            minibikeEntity.setPos(blockpos1.getX(), blockpos1.getY(), blockpos1.getZ());

            world.addFreshEntity(minibikeEntity);
            minibikeEntity.yRot = p_195939_1_.getPlayer().yRot;
            minibikeEntity.yRotO = p_195939_1_.getPlayer().yRot;
            minibikeEntity.yBodyRot = p_195939_1_.getPlayer().yRot;
            minibikeEntity.yBodyRotO = p_195939_1_.getPlayer().yRot;
            minibikeEntity.yHeadRot = p_195939_1_.getPlayer().yRot;
            minibikeEntity.yHeadRotO = p_195939_1_.getPlayer().yRot;

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

                MinibikeEntity minibikeEntity = new MinibikeEntity(p_77659_1_);
                minibikeEntity.setPos(blockpos.getX(), blockpos.getY(), blockpos.getZ());


                p_77659_1_.addFreshEntity(minibikeEntity);
                minibikeEntity.yRot = p_77659_2_.yRot;
                minibikeEntity.yRotO = p_77659_2_.yRot;
                minibikeEntity.yBodyRot = p_77659_2_.yRot;
                minibikeEntity.yBodyRotO = p_77659_2_.yRot;
                minibikeEntity.yHeadRot = p_77659_2_.yRot;
                minibikeEntity.yHeadRotO = p_77659_2_.yRot;

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
