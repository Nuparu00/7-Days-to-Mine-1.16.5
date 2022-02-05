package nuparu.sevendaystomine.entity.ai;

import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.item.BowItem;
import net.minecraft.util.math.MathHelper;
import nuparu.sevendaystomine.entity.ZombiePolicemanEntity;

import java.util.EnumSet;

public class RangedVomitAttackGoal extends Goal {
    private final ZombiePolicemanEntity mob;
    private final IRangedAttackMob rangedAttackMob;
    private LivingEntity target;
    private int attackTime = -1;
    private final double speedModifier;
    private int seeTime;
    private final int attackIntervalMin;
    private final int attackIntervalMax;
    private final float attackRadius;
    private final float attackRadiusSqr;

    public RangedVomitAttackGoal(IRangedAttackMob p_i1649_1_, double p_i1649_2_, int p_i1649_4_, float p_i1649_5_) {
        this(p_i1649_1_, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
    }

    public RangedVomitAttackGoal(IRangedAttackMob p_i1650_1_, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
        if (!(p_i1650_1_ instanceof ZombiePolicemanEntity)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires ZombiePolicemanEntity implements RangedAttackMob");
        } else {
            this.rangedAttackMob = p_i1650_1_;
            this.mob = (ZombiePolicemanEntity)p_i1650_1_;
            this.speedModifier = p_i1650_2_;
            this.attackIntervalMin = p_i1650_4_;
            this.attackIntervalMax = p_i1650_5_;
            this.attackRadius = p_i1650_6_;
            this.attackRadiusSqr = p_i1650_6_ * p_i1650_6_;
        }
    }

    public boolean canUse() {
        if(mob.getVomitTimer() != 0) return false;
        LivingEntity livingentity = this.mob.getTarget();
        if (livingentity != null && livingentity.isAlive()) {
            this.target = livingentity;
            if(this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ()) <= 4){
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean canContinueToUse() {
        return this.canUse();
    }

    public void stop() {
        this.target = null;
        this.seeTime = 0;
        this.attackTime = -1;
    }

    public void tick() {
        double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        boolean flag = this.mob.getSensing().canSee(this.target);
        if (flag) {
            ++this.seeTime;
        } else {
            this.seeTime = 0;
        }

        if (!(d0 > (double)this.attackRadiusSqr) && this.seeTime >= 5) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
        }

        this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
        if (--this.attackTime == 0) {
            if (!flag) {
                return;
            }

            float f = MathHelper.sqrt(d0) / this.attackRadius;
            float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, lvt_5_1_);
            this.attackTime = MathHelper.floor(f * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        } else if (this.attackTime < 0) {
            float f2 = MathHelper.sqrt(d0) / this.attackRadius;
            this.attackTime = MathHelper.floor(f2 * (float)(this.attackIntervalMax - this.attackIntervalMin) + (float)this.attackIntervalMin);
        }

    }
}