package nuparu.sevendaystomine.entity;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SChangeGameStatePacket;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.math.*;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.block.BlockTurretBase;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.BulletImpactMessage;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.Utils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public class ShotEntity extends ProjectileEntity {
    private static final Predicate<Entity> TARGETS = EntityPredicates.NO_SPECTATORS.and(EntityPredicates.ENTITY_STILL_ALIVE).and(Entity::canBeCollidedWith)::test;


    private static final DataParameter<Byte> PIERCE_LEVEL = EntityDataManager.defineId(ShotEntity.class, DataSerializers.BYTE);
    private static final float BLOCK_DAMAGE_BASE = 0.003125f;
    protected boolean inGround;
    protected int inGroundTime;
    private boolean explosive;
    private boolean sparking;
    private boolean turret;
    private int life;
    private double baseDamage = 2.0D;
    private int knockback;
    private IntOpenHashSet piercingIgnoreEntityIds;
    private List<Entity> piercedAndKilledEntities;

    public ShotEntity(EntityType<ShotEntity> type, World p_i48546_2_) {
        super(type, p_i48546_2_);
    }

    public ShotEntity(World p_i48546_2_) {
        super(ModEntities.SHOT.get(), p_i48546_2_);
    }

    public ShotEntity(double p_i48547_2_, double p_i48547_4_, double p_i48547_6_, World p_i48547_8_) {
        this(ModEntities.SHOT.get(), p_i48547_8_);
        this.setPos(p_i48547_2_, p_i48547_4_, p_i48547_6_);
    }

    public ShotEntity(LivingEntity p_i48548_2_, World p_i48548_3_) {
        this(p_i48548_2_.getX(), p_i48548_2_.getEyeY() - (double) 0.1F, p_i48548_2_.getZ(), p_i48548_3_);
        this.setOwner(p_i48548_2_);

    }

    public void shoot(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        Vector3d vector3d = (new Vector3d(p_70186_1_, p_70186_3_, p_70186_5_)).normalize().add(this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_, this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_, this.random.nextGaussian() * (double) 0.0075F * (double) p_70186_8_).scale(p_70186_7_);
        this.setDeltaMovement(vector3d);
        float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
        this.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
        this.xRot = (float) (MathHelper.atan2(vector3d.y, f) * (double) (180F / (float) Math.PI));
        this.yRotO = this.yRot;
        this.xRotO = this.xRot;
    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -MathHelper.sin(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        float f1 = -MathHelper.sin((p_234612_2_ + p_234612_4_) * ((float) Math.PI / 180F));
        float f2 = MathHelper.cos(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
        Vector3d vector3d = p_234612_1_.getDeltaMovement();
        this.setDeltaMovement(this.getDeltaMovement().add(vector3d.x, p_234612_1_.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
    }

    public void shootFromRotation(float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = -MathHelper.sin(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        float f1 = -MathHelper.sin((p_234612_2_ + p_234612_4_) * ((float) Math.PI / 180F));
        float f2 = MathHelper.cos(p_234612_3_ * ((float) Math.PI / 180F)) * MathHelper.cos(p_234612_2_ * ((float) Math.PI / 180F));
        this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
    }

    protected void defineSynchedData() {
        this.entityData.define(PIERCE_LEVEL, (byte) 0);
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpTo(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.setPos(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRot(p_180426_7_, p_180426_8_);
    }

    @OnlyIn(Dist.CLIENT)
    public void lerpMotion(double p_70016_1_, double p_70016_3_, double p_70016_5_) {
        super.lerpMotion(p_70016_1_, p_70016_3_, p_70016_5_);
        this.life = 0;
    }

    public void tick() {
        super.tick();
        boolean flag = this.isNoPhysics();
        Vector3d vector3d = this.getDeltaMovement();
        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            float f = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
            this.yRot = (float) (MathHelper.atan2(vector3d.x, vector3d.z) * (double) (180F / (float) Math.PI));
            this.xRot = (float) (MathHelper.atan2(vector3d.y, f) * (double) (180F / (float) Math.PI));
            this.yRotO = this.yRot;
            this.xRotO = this.xRot;
        }

        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level.getBlockState(blockpos);
        if (!blockstate.isAir(this.level, blockpos) && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level, blockpos);
            if (!voxelshape.isEmpty()) {
                Vector3d vector3d1 = this.position();

                for (AxisAlignedBB axisalignedbb : voxelshape.toAabbs()) {
                    if (axisalignedbb.move(blockpos).contains(vector3d1)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.isInWaterOrRain()) {
            this.clearFire();
        }

        if (this.inGround && !flag) {
            if (!this.level.isClientSide) {
                this.tickDespawn();
            }

            ++this.inGroundTime;
        } else {
            this.inGroundTime = 0;
            Vector3d vector3d2 = this.position();
            Vector3d vector3d3 = vector3d2.add(vector3d);
            RayTraceResult raytraceresult = this.level.clip(new RayTraceContext(vector3d2, vector3d3, RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
            if (raytraceresult.getType() != RayTraceResult.Type.MISS) {
                vector3d3 = raytraceresult.getLocation();
            }

            while (!this.removed) {
                EntityRayTraceResult entityraytraceresult = this.findHitEntity(vector3d2, vector3d3);
                if (entityraytraceresult != null) {
                    raytraceresult = entityraytraceresult;
                }

                if (raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
                    Entity entity = ((EntityRayTraceResult) raytraceresult).getEntity();
                    Entity entity1 = this.getOwner();
                    if (entity instanceof PlayerEntity && entity1 instanceof PlayerEntity && !((PlayerEntity) entity1).canHarmPlayer((PlayerEntity) entity)) {
                        raytraceresult = null;
                        entityraytraceresult = null;
                    }
                }

                if (raytraceresult != null && raytraceresult.getType() != RayTraceResult.Type.MISS && !flag && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                    this.onHit(raytraceresult);
                    this.hasImpulse = true;
                }

                if (entityraytraceresult == null || this.getPierceLevel() <= 0) {
                    break;
                }

                raytraceresult = null;
            }

            vector3d = this.getDeltaMovement();
            double d3 = vector3d.x;
            double d4 = vector3d.y;
            double d0 = vector3d.z;

            double d5 = this.getX() + d3;
            double d1 = this.getY() + d4;
            double d2 = this.getZ() + d0;
            float f1 = MathHelper.sqrt(getHorizontalDistanceSqr(vector3d));
            if (flag) {
                this.yRot = (float) (MathHelper.atan2(-d3, -d0) * (double) (180F / (float) Math.PI));
            } else {
                this.yRot = (float) (MathHelper.atan2(d3, d0) * (double) (180F / (float) Math.PI));
            }

            this.xRot = (float) (MathHelper.atan2(d4, f1) * (double) (180F / (float) Math.PI));
            this.xRot = lerpRotation(this.xRotO, this.xRot);
            this.yRot = lerpRotation(this.yRotO, this.yRot);
            float f2 = 0.99F;
            float f3 = 0.05F;
            if (this.isInWater()) {
                for (int j = 0; j < 4; ++j) {
                    float f4 = 0.25F;
                    this.level.addParticle(ParticleTypes.BUBBLE, d5 - d3 * 0.25D, d1 - d4 * 0.25D, d2 - d0 * 0.25D, d3, d4, d0);
                }

                f2 = this.getWaterInertia();
            }

            this.setDeltaMovement(vector3d.scale(f2));
            if (!this.isNoGravity() && !flag) {
                Vector3d vector3d4 = this.getDeltaMovement();
                this.setDeltaMovement(vector3d4.x, vector3d4.y - (double) 0.05F, vector3d4.z);
            }

            this.setPos(d5, d1, d2);
            this.checkInsideBlocks();
        }
    }

    private boolean shouldFall() {
        return this.inGround && this.level.noCollision((new AxisAlignedBB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vector3d vector3d = this.getDeltaMovement();
        this.setDeltaMovement(vector3d.multiply(this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F, this.random.nextFloat() * 0.2F));
        this.life = 0;
    }

    public void move(MoverType p_213315_1_, Vector3d p_213315_2_) {
        super.move(p_213315_1_, p_213315_2_);
        if (p_213315_1_ != MoverType.SELF && this.shouldFall()) {
            this.startFalling();
        }

    }

    protected void tickDespawn() {
        ++this.life;
        if (this.life >= 1200) {
            this.remove();
        }

    }

    private void resetPiercedEntities() {
        if (this.piercedAndKilledEntities != null) {
            this.piercedAndKilledEntities.clear();
        }

        if (this.piercingIgnoreEntityIds != null) {
            this.piercingIgnoreEntityIds.clear();
        }

    }

    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
        super.onHitEntity(p_213868_1_);
        Entity entity = p_213868_1_.getEntity();
        float f = (float) this.getDeltaMovement().length();
        int i = MathHelper.ceil(MathHelper.clamp((double) this.baseDamage, 0.0D, 2.147483647E9D));
        if (this.getPierceLevel() > 0) {
            if (this.piercingIgnoreEntityIds == null) {
                this.piercingIgnoreEntityIds = new IntOpenHashSet(5);
            }

            if (this.piercedAndKilledEntities == null) {
                this.piercedAndKilledEntities = Lists.newArrayListWithCapacity(5);
            }

            if (this.piercingIgnoreEntityIds.size() >= this.getPierceLevel() + 1) {
                this.remove();
                return;
            }

            this.piercingIgnoreEntityIds.add(entity.getId());
        }

        Entity entity1 = this.getOwner();
        DamageSource damagesource;
        if (entity1 == null) {
            damagesource = DamageSources.causeShotDamage(this, this);
        } else {
            damagesource = DamageSources.causeShotDamage(this, entity1);
            if (entity1 instanceof LivingEntity) {
                ((LivingEntity) entity1).setLastHurtMob(entity);
            }
        }

        boolean flag = entity.getType() == EntityType.ENDERMAN;
        int k = entity.getRemainingFireTicks();
        if ((this.isOnFire() || sparking) && !flag) {
            entity.setSecondsOnFire(10);
        }

        if (explosive && !level.isClientSide()) {
            level.explode(this, entity.getX(), entity.getY(), entity.getZ(), 1.2f, sparking, Explosion.Mode.DESTROY);
        }

        if (entity.hurt(damagesource, (float) i)) {
            if (flag) {
                return;
            }

            if (entity instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entity;

                if (this.knockback > 0) {
                    Vector3d vector3d = this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D).normalize().scale((double) this.knockback * 0.6D);
                    if (vector3d.lengthSqr() > 0.0D) {
                        livingentity.push(vector3d.x, 0.1D, vector3d.z);
                    }
                }

                if (!this.level.isClientSide && entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity);
                }

                this.doPostHurtEffects(livingentity);
                if (entity1 != null && livingentity != entity1 && livingentity instanceof PlayerEntity && entity1 instanceof ServerPlayerEntity && !this.isSilent()) {
                    ((ServerPlayerEntity) entity1).connection.send(new SChangeGameStatePacket(SChangeGameStatePacket.ARROW_HIT_PLAYER, 0.0F));
                }

                if (!entity.isAlive() && this.piercedAndKilledEntities != null) {
                    this.piercedAndKilledEntities.add(livingentity);
                }

                if (!this.level.isClientSide && entity1 instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) entity1;
                }
            }

            if (this.getPierceLevel() <= 0) {
                this.remove();
            }
        } else {
            entity.setRemainingFireTicks(k);
            this.setDeltaMovement(this.getDeltaMovement().scale(-0.1D));
            this.yRot += 180.0F;
            this.yRotO += 180.0F;
            if (!this.level.isClientSide && this.getDeltaMovement().lengthSqr() < 1.0E-7D) {
                this.remove();
            }
        }

    }

    @Override
    protected void onHit(RayTraceResult p_70227_1_) {
        RayTraceResult.Type raytraceresult$type = p_70227_1_.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            Entity entity = ((EntityRayTraceResult) p_70227_1_).getEntity();
            int invulnerableTimeOld = entity.invulnerableTime;
            entity.invulnerableTime = 0;
            this.onHitEntity((EntityRayTraceResult) p_70227_1_);
            entity.invulnerableTime = invulnerableTimeOld;
        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            this.onHitBlock((BlockRayTraceResult) p_70227_1_);
        }

    }

    protected void onHitBlock(BlockRayTraceResult rayTraceResult) {
        BlockPos pos = rayTraceResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (block instanceof BlockTurretBase) {
            return;
        }

        super.onHitBlock(rayTraceResult);
        Vector3d vector3d = rayTraceResult.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vector3d);
        Vector3d vector3d1 = vector3d.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vector3d1.x, this.getY() - vector3d1.y, this.getZ() - vector3d1.z);
        this.inGround = true;
        this.setPierceLevel((byte) 0);
        this.resetPiercedEntities();


        if (ServerConfig.bulletsBreakBlocks.get() && !level.isClientSide()) {
            if (state.getMaterial() == Material.GLASS) {
                this.level.destroyBlock(pos, false);
            } else if (!Utils.damageBlock((ServerWorld) level, pos,
                    (float) (baseDamage / state.getDestroySpeed(level, pos)) * BLOCK_DAMAGE_BASE,
                    true)) {
                Chunk chunk = level.getChunkAt(pos);
                if (chunk != null) {
                    PacketManager.sendToChunk(PacketManager.bulletImpact,
                            new BulletImpactMessage(getX(), getY(), getZ(), this.getDeltaMovement().x, this.getDeltaMovement().y, this.getDeltaMovement().z, pos), () -> chunk);
                }
            }
        }
        if (explosive && !level.isClientSide()) {
            level.explode(this, pos.getX(), pos.getY(), pos.getZ(), 1.2f, sparking, Explosion.Mode.DESTROY);
        }
    }

    protected void doPostHurtEffects(LivingEntity p_184548_1_) {
    }

    @Nullable
    protected EntityRayTraceResult findHitEntity(Vector3d p_213866_1_, Vector3d p_213866_2_) {
        return ProjectileHelper.getEntityHitResult(this.level, this, p_213866_1_, p_213866_2_, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1.0D), this::canHitEntity);
    }

    protected boolean canHitEntity(Entity p_230298_1_) {
        return super.canHitEntity(p_230298_1_) && (this.piercingIgnoreEntityIds == null || !this.piercingIgnoreEntityIds.contains(p_230298_1_.getId()));
    }

    public void addAdditionalSaveData(CompoundNBT nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putShort("life", (short) this.life);

        nbt.putBoolean("inGround", this.inGround);
        nbt.putDouble("damage", this.baseDamage);
        nbt.putByte("PierceLevel", this.getPierceLevel());
        nbt.putBoolean("explosive", this.explosive);
        nbt.putBoolean("sparking", this.sparking);
    }

    public void readAdditionalSaveData(CompoundNBT nbt) {
        super.readAdditionalSaveData(nbt);
        this.life = nbt.getShort("life");

        this.inGround = nbt.getBoolean("inGround");
        if (nbt.contains("damage", 99)) {
            this.baseDamage = nbt.getDouble("damage");
        }

        this.setPierceLevel(nbt.getByte("PierceLevel"));

        this.explosive = nbt.getBoolean("explosive");
        this.sparking = nbt.getBoolean("sparking");
    }

    public void setOwner(@Nullable Entity p_212361_1_) {
        super.setOwner(p_212361_1_);
    }


    protected boolean isMovementNoisy() {
        return false;
    }

    public double getBaseDamage() {
        return this.baseDamage;
    }

    public void setBaseDamage(double p_70239_1_) {
        this.baseDamage = p_70239_1_;
    }

    public void setKnockback(int p_70240_1_) {
        this.knockback = p_70240_1_;
    }

    public boolean isAttackable() {
        return false;
    }

    protected float getEyeHeight(Pose p_213316_1_, EntitySize p_213316_2_) {
        return 0.13F;
    }

    public byte getPierceLevel() {
        return this.entityData.get(PIERCE_LEVEL);
    }

    public void setPierceLevel(byte p_213872_1_) {
        this.entityData.set(PIERCE_LEVEL, p_213872_1_);
    }

    public void setEnchantmentEffectsFromEntity(LivingEntity p_190547_1_, float p_190547_2_) {
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER_ARROWS, p_190547_1_);
        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH_ARROWS, p_190547_1_);
        this.setBaseDamage((double) (p_190547_2_ * 2.0F) + this.random.nextGaussian() * 0.25D + (double) ((float) this.level.getDifficulty().getId() * 0.11F));
        if (i > 0) {
            this.setBaseDamage(this.getBaseDamage() + (double) i * 0.5D + 0.5D);
        }

        if (j > 0) {
            this.setKnockback(j);
        }

        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAMING_ARROWS, p_190547_1_) > 0) {
            this.setSecondsOnFire(100);
        }

    }

    protected float getWaterInertia() {
        return 0.6F;
    }

    public boolean isNoPhysics() {
        return this.noPhysics;
    }

    public void setNoPhysics(boolean p_203045_1_) {
        this.noPhysics = p_203045_1_;
    }

    public IPacket<?> getAddEntityPacket() {
        Entity entity = this.getOwner();
        return new SSpawnObjectPacket(this, entity == null ? 0 : entity.getId());
    }

    public boolean getExplosive() {
        return explosive;
    }

    public void setExplosive(boolean explosive) {
        this.explosive = explosive;
    }

    public boolean getSparking() {
        return sparking;
    }

    public void setSparking(boolean sparking) {
        this.sparking = sparking;
    }

    public boolean getTurret() {
        return turret;
    }

    public void setTurret(boolean turret) {
        this.turret = turret;
    }

    public double getDamage() {
        return this.baseDamage;
    }

    public void setDamage(double damageIn) {
        this.baseDamage = damageIn;
    }
}
