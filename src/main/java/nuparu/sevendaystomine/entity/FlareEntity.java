package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModLootTables;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;

public class FlareEntity extends ProjectileItemEntity {


    public long age = 0;
    public boolean called = false;

    public FlareEntity(EntityType<FlareEntity> fragmentationGrenadeEntityEntityType, World world) {
        super(fragmentationGrenadeEntityEntityType, world);
    }

    public FlareEntity(World p_i50155_2_) {
        super(ModEntities.FLARE.get(), p_i50155_2_);
        setItem(new ItemStack(ModItems.FLARE.get()));
    }

    public FlareEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
        super(ModEntities.FLARE.get(), p_i1774_2_, p_i1774_1_);
        setItem(new ItemStack(ModItems.FLARE.get()));
    }

    public FlareEntity(World p_i1775_1_, double p_i1775_2_, double p_i1775_4_, double p_i1775_6_) {
        super(ModEntities.FLARE.get(), p_i1775_2_, p_i1775_4_, p_i1775_6_, p_i1775_1_);
        setItem(new ItemStack(ModItems.FLARE.get()));
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.FLARE.get();
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData getParticle() {
        ItemStack itemstack = new ItemStack(ModItems.FLARE.get());
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    public void handleEntityEvent(byte p_70103_1_) {
        if (p_70103_1_ == 3) {
            IParticleData iparticledata = this.getParticle();

            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    @Override
    protected void onHitEntity(EntityRayTraceResult p_213868_1_) {

    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        BlockState blockstate = this.level.getBlockState(result.getBlockPos());
        blockstate.onProjectileHit(this.level, blockstate, result, this);
        Vector3i normali = result.getDirection().getNormal();
        Vector3d direction = this.getDeltaMovement();
        Vector3d normal = new Vector3d(normali.getX(),normali.getY(),normali.getZ());

        double dot =  direction.normalize().dot(normal);
        Vector3d res = direction.subtract(normal.multiply(2*dot,2*dot,2*dot));

        this.setDeltaMovement(res.x*direction.length()*0.3f, res.y*direction.length()*0.3f, res.z*direction.length()*0.3f);
    }

    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        age = compound.getLong("age");
        called = compound.getBoolean("called");
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putLong("age", age);
        compound.putBoolean("called", called);
    }

    @Override
    public void tick() {
        super.tick();

        this.age++;
        if (!level.isClientSide()) {
            if(this.age >= 240 && !called && level instanceof ServerWorld){
                ServerWorld serverLevel = (ServerWorld)level;

                AirdropEntity e = new AirdropEntity(level, serverLevel.getSharedSpawnPos().above(255));
                LootTable loottable = level.getServer().getLootTables().get(ModLootTables.AIRDROP);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder(serverLevel)).withParameter(LootParameters.ORIGIN, Vector3d.atCenterOf(blockPosition()));

                ItemUtils.fill(loottable,e.getInventory(), lootcontext$builder.create(LootParameterSets.CHEST));
                serverLevel.addFreshEntity(e);
                e.setPos(getX()+MathUtils.getIntInRange(-8,8), 255, getZ()+MathUtils.getIntInRange(-8,8));

                called = true;
            }
            if (this.age >= 360) {
                this.kill();
                return;
            }
        }

        if(this.age >= 60){
            double height = this.getDimensions(this.getPose()).height;
            for (int i = 0; i < random.nextInt(3) + 1; i++) {
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, this.position().x, this.position().y + height
                        , this.position().z,
                        MathUtils.getFloatInRange(-0.02f, 0.02f), MathUtils.getFloatInRange(0.2f, 0.5f),
                        MathUtils.getFloatInRange(-0.02f, 0.02f));
                level.addParticle(ParticleTypes.CLOUD, this.position().x, this.position().y + height, this.position().z,
                        MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.2f, 0.5f),
                        MathUtils.getFloatInRange(-0.1f, 0.1f));
            }
        }
    }
}
