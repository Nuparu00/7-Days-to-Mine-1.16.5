package nuparu.sevendaystomine.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.inventory.entity.ContainerAirdrop;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.util.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class AirdropEntity extends Entity implements INamedContainerProvider {

    private static final DataParameter<Boolean> LANDED = EntityDataManager.<Boolean>defineId(AirdropEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> SMOKE_TIME = EntityDataManager.<Integer>defineId(AirdropEntity.class,
            DataSerializers.INT);
    private static final DataParameter<Integer> HEALTH = EntityDataManager.<Integer>defineId(AirdropEntity.class,
            DataSerializers.INT);
    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);
    public long age = 0;
    private boolean onEntity = false;

    public AirdropEntity(EntityType<AirdropEntity> type, World world) {
        super(type, world);
    }

    public AirdropEntity(World world) {
        this(ModEntities.AIRDROP.get(), world);
    }
    public AirdropEntity(World world, BlockPos pos) {
        this(world);
        this.setPos(pos.getX(),pos.getY(),pos.getZ());
    }


    @Override
    protected void defineSynchedData() {
        this.getEntityData().define(LANDED, false);
        this.getEntityData().define(SMOKE_TIME, 0);
        this.getEntityData().define(HEALTH, 50);
    }

    public boolean getLanded() {
        return this.getEntityData().get(LANDED);
    }

    public void setLanded(boolean landed) {
        this.getEntityData().set(LANDED, landed);
    }

    public int getSmokeTime() {
        return this.getEntityData().get(SMOKE_TIME);
    }

    public void setSmokeTime(int ticks) {
        this.getEntityData().set(SMOKE_TIME, ticks);
    }

    public int getHealth() {
        return this.getEntityData().get(HEALTH);
    }

    public void setHealth(int health) {
        this.getEntityData().set(HEALTH, health);
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {


        onEntity = compound.getBoolean("onEntity");
        age = compound.getLong("age");
        if (compound.contains("health")) {
            setHealth(compound.getInt("health"));
        }
        if (compound.contains("smokeTime")) {
            setSmokeTime(compound.getInt("smokeTime"));
        }
        if (compound.contains("landed")) {
            setLanded(compound.getBoolean("landed"));
        }
        if (getInventory() != null && compound.contains("ItemHandler")) {
            getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {

        compound.putBoolean("onEntity", onEntity);
        compound.putLong("age", age);
        compound.putInt("health", getHealth());
        compound.putInt("smokeTime", getSmokeTime());
        compound.putBoolean("landed", getLanded());
        if (getInventory() != null) {
            compound.put("ItemHandler", getInventory().serializeNBT());
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    @Override
    public ActionResultType interact(PlayerEntity playerEntity, Hand hand) {
        if (!playerEntity.isCrouching() && hand == Hand.MAIN_HAND) {
            if (playerEntity instanceof ServerPlayerEntity) {
                ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                NetworkHooks.openGui(serverPlayerEntity, this, (packetBuffer) -> {
                    packetBuffer.writeInt(this.getId());
                });
            }
            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }


    @Override
    public void tick() {
        super.tick();

        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();

        this.age++;
        if (!level.isClientSide()) {
            // ModConfig.world.corpseLifespan
            if (this.age >= 48000) {
                this.kill();
                return;
            }
        }


        if (getLanded() && getSmokeTime() > 0) {
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
            setSmokeTime(getSmokeTime() - 1);
        }

        double motionX = this.getDeltaMovement().x;
        double motionY = this.getDeltaMovement().y;
        double motionZ = this.getDeltaMovement().z;

        if (!onGround && !onEntity) {
            motionY = !getLanded() ? -0.0625 : -0.1911;
        } else {
            motionY = 0;
            if (!level.isClientSide()) {
                if (!getLanded()) {
                    setSmokeTime(1200);
                    setLanded(true);
                }
            }
        }
        if (this.onGround) {
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        }
        this.setDeltaMovement(new Vector3d(motionX, motionY, motionZ));
        this.move(MoverType.SELF, this.getDeltaMovement());

        if (this.getY() < -64.0D) {
            this.outOfWorld();
        }

        boolean flag = false;
        for (Entity entity : this.level.getEntities(this, getBoundingBox())) {
            if (entity instanceof PlayerEntity)
                continue;
            if (!this.hasPassenger(entity) && entity.canBeCollidedWith()) {
                flag = true;
            }
        }
        this.onEntity = flag;

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ServerPlayerEntity) {
            ServerPlayerEntity player = (ServerPlayerEntity) source.getDirectEntity();
            if (!player.isCreative()) {
                ItemStack s = player.getMainHandItem();
                if (s.getMaxDamage() > 0) {
                    s.hurt(1, this.random, player);
                    if (s.getDamageValue() >= s.getMaxDamage()) {
                        s.setCount(0);
                    }
                }
                if (s.getItem() instanceof ItemFuelTool) {
                    CompoundNBT nbt = s.getOrCreateTag();
                    if (nbt != null && nbt.contains("FuelCurrent") && nbt.getInt("FuelCurrent") > 0) {
                        nbt.putInt("FuelCurrent", Math.max(0, nbt.getInt("FuelCurrent") - 1));
                    }
                }
            }
        }
        int healthNew = (int) (getHealth() - amount);
        this.setHealth(healthNew);
        if (healthNew <= 0) {
            if (getInventory() != null) {
                for (int i = 0; i < getInventory().getSlots(); i++) {
                    ItemStack stack = getInventory().getStackInSlot(i);
                    InventoryHelper.dropItemStack(level, getX() + this.getBbWidth() / 2, getY(),
                            getZ() + this.getBbWidth() / 2, stack);
                }
            }
            this.kill();
        }
        return super.hurt(source, amount);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    public ExtendedInventory getInventory() {
        return this.inventory.orElse(null);
    }

    public int getInventorySize() {
        return 9;
    }

    protected ExtendedInventory initInventory() {
        return new ExtendedInventory(getInventorySize());
    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        this.getInventory().setStackInSlot(inventorySlot, itemStackIn);
        return true;
    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ExtendedInventoryProvider.EXTENDED_INV_CAP) {
            return inventory.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void kill() {
        this.inventory.invalidate();
        super.kill();
    }

    @Override
    public Container createMenu(int windowiD, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ContainerAirdrop.createContainerServerSide(windowiD,playerInventory,this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean shouldRenderAtSqrDistance(double p_70112_1_) {
        double d0 = this.getBoundingBox().getSize();
        if (Double.isNaN(d0)) {
            d0 = 1.0D;
        }

        d0 = d0 * 128.0D * getViewScale();
        return p_70112_1_ < d0 * d0;
    }
}
