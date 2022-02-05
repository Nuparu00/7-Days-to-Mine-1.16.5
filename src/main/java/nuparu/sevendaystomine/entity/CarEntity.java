package nuparu.sevendaystomine.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.DyeColor;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockBush;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.entity.ContainerCar;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.ItemUtils;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.PlayerUtils;

import javax.annotation.Nullable;
import java.util.List;

public class CarEntity extends VehicleEntity {

    private static final DataParameter<Boolean> ENGINE = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BATTERY = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WHEELS_FRONT = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WHEELS_BACK = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SEAT = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HANDLES = EntityDataManager.<Boolean>defineId(CarEntity.class,
            DataSerializers.BOOLEAN);

    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(CarEntity.class, DataSerializers.INT);

    public int honkCooldown = 0;

    public CarEntity(EntityType<CarEntity> type, World world) {
        super(type, world);
        this.maxUpStep = 1.5f;
    }

    public CarEntity(World world) {
        this(ModEntities.CAR.get(), world);
    }

    public static AttributeModifierMap createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 60).build();
    }

    public int getInventorySize() {
        return 16;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ENGINE, false);
        this.getEntityData().define(BATTERY, false);
        this.getEntityData().define(WHEELS_FRONT, false);
        this.getEntityData().define(WHEELS_BACK, false);
        this.getEntityData().define(SEAT, false);
        this.getEntityData().define(HANDLES, false);
        this.entityData.define(COLOR, DyeColor.RED.getId());
    }

    @Override
    public ActionResultType interact(PlayerEntity playerEntity, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            ItemStack itemstack = playerEntity.getItemInHand(hand);
            Item item = itemstack.getItem();
            if(item instanceof DyeItem){
                DyeColor dyecolor = ((DyeItem)item).getDyeColor();
                if(dyecolor != getColor()){
                    setColor(dyecolor);
                    if (!playerEntity.abilities.instabuild) {
                        itemstack.shrink(1);
                    }
                }
                return ActionResultType.SUCCESS;
            }
        }

        return super.interact(playerEntity, hand);
    }

    public boolean getEngine() {
        return this.getEntityData().get(ENGINE);
    }

    protected void setEngine(boolean state) {
        this.getEntityData().set(ENGINE, state);
    }

    public boolean getBattery() {
        return this.getEntityData().get(BATTERY);
    }

    protected void setBattery(boolean state) {
        this.getEntityData().set(BATTERY, state);
    }

    public boolean getWheelsFront() {
        return this.getEntityData().get(WHEELS_FRONT);
    }

    protected void setWheelsFront(boolean state) {
        this.getEntityData().set(WHEELS_FRONT, state);
    }

    public boolean getWheelsBack() {
        return this.getEntityData().get(WHEELS_BACK);
    }

    protected void setWheelsBack(boolean state) {
        this.getEntityData().set(WHEELS_BACK, state);
    }

    public boolean getSeat() {
        return this.getEntityData().get(SEAT);
    }

    protected void setSeat(boolean state) {
        this.getEntityData().set(SEAT, state);
    }

    public boolean getHandles() {
        return this.getEntityData().get(HANDLES);
    }

    protected void setHandles(boolean state) {
        this.getEntityData().set(HANDLES, state);
    }

    public DyeColor getColor() {
        return DyeColor.byId(this.entityData.get(COLOR));
    }

    public void setColor(DyeColor p_175547_1_) {
        this.entityData.set(COLOR, p_175547_1_.getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {

        super.readAdditionalSaveData(compound);
        if (compound.contains("engine", Constants.NBT.TAG_BYTE)) {
            this.setEngine(compound.getBoolean("engine"));
        }
        if (compound.contains("battery", Constants.NBT.TAG_BYTE)) {
            this.setBattery(compound.getBoolean("battery"));
        }
        if (compound.contains("wheels_front", Constants.NBT.TAG_BYTE)) {
            this.setWheelsFront(compound.getBoolean("wheels_front"));
        }
        if (compound.contains("wheels_back", Constants.NBT.TAG_BYTE)) {
            this.setWheelsBack(compound.getBoolean("wheels_back"));
        }
        if (compound.contains("seat", Constants.NBT.TAG_BYTE)) {
            this.setSeat(compound.getBoolean("seat"));
        }
        if (compound.contains("handles", Constants.NBT.TAG_BYTE)) {
            this.setHandles(compound.getBoolean("handles"));
        }
        if (compound.contains("color", Constants.NBT.TAG_BYTE)) {
            this.setColor(DyeColor.byId(compound.getInt("color")));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {


        super.addAdditionalSaveData(compound);
        compound.putBoolean("engine", getEngine());
        compound.putBoolean("battery", getBattery());
        compound.putBoolean("wheels_front", getWheelsFront());
        compound.putBoolean("wheels_back", getWheelsBack());
        compound.putBoolean("seat", getSeat());
        compound.putBoolean("handles", getHandles());
        compound.putByte("color", (byte)this.getColor().getId());

    }


    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengers().size() < 4;
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.02D;
    }

    @Override
    public boolean isComplete() {
        return this.getBattery() && this.getEngine() && this.getHandles() && this.getSeat() && this.getWheelsFront() && this.getWheelsBack();
    }

    public boolean isBateryCharged() {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return false;
        ItemStack battery = inv.getStackInSlot(3);
        if (battery.isEmpty())
            return false;
        Item item = battery.getItem();
        if (!(item instanceof IBattery))
            return false;
        IBattery bat = (IBattery) item;
        return bat.getVoltage(battery, level) > 0;
    }

    @Override
    public void updateInventory() {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;

        this.setHandles(inv.getStackInSlot(0).getItem() == ModItems.MINIBIKE_HANDLES.get());
        this.setWheelsFront(inv.getStackInSlot(1).getItem() == ModBlocks.WHEELS.get().asItem());
        this.setWheelsBack(inv.getStackInSlot(2).getItem() == ModBlocks.WHEELS.get().asItem());
        this.setSeat(inv.getStackInSlot(3).getItem() == ModItems.MINIBIKE_SEAT.get());
        this.setBattery(inv.getStackInSlot(4).getItem() instanceof IBattery);
        this.setEngine(inv.getStackInSlot(5).getItem() == ModItems.SMALL_ENGINE.get());

        int quality = -1;

        if (this.getHandles() && this.getBattery() && this.getWheelsFront() && this.getWheelsBack() && this.getSeat() && this.getEngine()) {
            if (EnumQualityState.isQualitySystemOn()) {
                quality = 0;
                int items = 0;
                for (int i = 0; i < 6; i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (!stack.isEmpty() && PlayerUtils.isQualityItem(stack)) {
                        quality += ItemUtils.getQuality(stack);
                        items++;
                    }
                }
                if (items > 0) {
                    quality = (int) Math.floor(quality / items);
                }
            } else {
                quality = 600;
            }
        }

        this.setCalculatedQuality(quality);
    }

    @Override
    public void tick() {
        if(honkCooldown > 0){
            honkCooldown--;
        }


        yBodyRot = yRot;
        yBodyRotO = yRotO;

        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;

        //System.out.println(this.lerpX);
        kmh = Math.sqrt(this.getDeltaMovement().x * this.getDeltaMovement().x + this.getDeltaMovement().z * this.getDeltaMovement().z) * 20;

        //System.out.println(this.getCalculatedQuality() + " " + kmh);
        if (level.isClientSide()) {
            if (System.currentTimeMillis() - nextIdleSound >= (4750 / (1d + kmh / 40d))
                    && this.getControllingPassenger() != null && this.canBeDriven()) {
                SevenDaysToMine.proxy.playMovingSound(0, this);
                this.nextIdleSound = System.currentTimeMillis();
            }
        }

        super.tick();

        Vector3d forward = this.getForwardServerSide();
        double drag = this.verticalCollision ? 0.88 : 0.99;

        this.setDeltaMovement(this.getDeltaMovement().multiply(drag, drag, drag));

        if (!level.isClientSide()) {
            float turning = this.getTurning();
            this.setTurningPrev(turning);
            if (turning != 0) {
                this.setTurning(turning * (float) drag);
            }
        }
        if (this.horizontalCollision && this.isInWater()) {
            this.setDeltaMovement(this.getDeltaMovement().x, 0.30000001192092896D, this.getDeltaMovement().z);
        }

        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.1, 0));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!level.isClientSide()) {
            this.setCharged(this.isBateryCharged());
        }
        this.wheelAnglePrev = this.wheelAngle;
        this.wheelAngle += forward.x * this.getDeltaMovement().x + forward.z * this.getDeltaMovement().z;
        if (wheelAngle > 360) {
            wheelAngle -= 360;
        } else if (wheelAngle < 0) {
            wheelAngle += 360;
        }

        if (this.getFuel() < 0) {
            this.setFuel(0);
        }

        double d2 = Math.cos((double) this.yRot * Math.PI / 180.0D);
        double d4 = Math.sin((double) this.yRot * Math.PI / 180.0D);

        //Replacing grass with dirt
        if (kmh > 3) {
            if (this.getControllingPassenger() != null && this.getControllingPassenger() instanceof LivingEntity) {
                BlockPos pos = new BlockPos(this.xOld, this.yOld - 1, this.zOld);
                BlockState state = this.level.getBlockState(pos);

                if (!level.isClientSide()) {
                    if (state.getBlock() == Blocks.GRASS_BLOCK) {
                        level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState());
                    } else if (level.getBlockState(pos.above()).getBlock() instanceof BlockBush) {
                        level.setBlockAndUpdate(pos.above(), Blocks.AIR.defaultBlockState());
                    }

                    if (level.random.nextInt(15) == 0) {
                        this.setFuel((this.getFuel() - (10F / ItemUtils.getQuality(inv.getStackInSlot(4)))));
                    }
                }
                if (state.isFaceSturdy(level, pos, Direction.UP)) {

                    for (int j = 0; j < 4; j++) {
                        double f = ((j == 0 || j == 3) ? 1.2D : -1.2D);
                        double g = ((j == 0 || j == 1) ? -0.75D : 0.75);
                        Vector3d vec3d = (new Vector3d(f, 0.0D, g))
                                .yRot(-this.yRot * 0.017453292F - ((float) Math.PI / 2F));
                        for (int k = 0; k < 5; k++) {
                            level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state),
                                    getX() + vec3d.x + (random.nextDouble() * 0.1 - 0.05),
                                    getY() + vec3d.y + (random.nextDouble() * 0.1 - 0.05),
                                    getZ() + vec3d.z + (random.nextDouble() * 0.1 - 0.05), getDeltaMovement().x, getDeltaMovement().y,
                                    getDeltaMovement().z);
                        }
                    }
                    Vector3d vec3d = (new Vector3d(-2.6, 0.15D + (random.nextDouble() * 0.1 - 0.05),
                            0.4 + (random.nextDouble() * 0.1 - 0.05)))
                            .yRot(-this.yRot * 0.017453292F - ((float) Math.PI / 2F));
                    for (int k = 0; k < 4; k++) {

                        level.addParticle(ParticleTypes.SMOKE, getX() + vec3d.x, getY() + vec3d.y,
                                getZ() + vec3d.z, -getDeltaMovement().x / 20, -getDeltaMovement().y / 20, -getDeltaMovement().z / 2);
                    }


                    /*for (int j = 0; j < 10; j++) {
                        level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state).setPos(pos), getX() + random.nextDouble() * 0.2,
                                getY() + random.nextDouble() * 0.2, getZ() + random.nextDouble() * 0.2, getDeltaMovement().x, getDeltaMovement().y,
                                getDeltaMovement().z);
                    }*/
                }
            }
            /*if (kmh > 5) {

                for (int k = 0; (double) k < 1.0D + kmh / 8; ++k) {
                    double d5 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                    double d6 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;

                    double d7 = this.getX();
                    double d8 = this.getZ();

                    level.addParticle(ParticleTypes.SMOKE, d7, this.getY() + 0.12, d8, this.getDeltaMovement().x,
                            this.getDeltaMovement().y, this.getDeltaMovement().z);

                }
            }*/
        }


        if (this.getHealth() <= this.getMaxHealth() * 0.2) {
            if (this.getHealth() <= this.getMaxHealth() * 0.1) {

                Vector3d vec3d = (new Vector3d(1.8 + (random.nextDouble() * 0.25 - 0.125), 0.5,
                        0 + (random.nextDouble() * 0.25 - 0.125)))
                        .yRot(-this.yRot * 0.017453292F - ((float) Math.PI / 2F));

                for (int k = 0; k < random.nextInt(2); k++) {
                    level.addParticle(ParticleTypes.FLAME, getX() + vec3d.x,
                            getY() + vec3d.y, getZ() + vec3d.z, this.getDeltaMovement().x,
                            this.getDeltaMovement().y + MathUtils.getDoubleInRange(0.02,0.035), this.getDeltaMovement().z);
                }
            }
            for (int k = 0; k < random.nextInt(3); k++) {
                Vector3d vec3d = (new Vector3d(1.8 + (random.nextDouble() * 0.5 - 0.25), 0.75,
                        0 + (random.nextDouble() * 0.5 - 0.25)))
                        .yRot(-this.yRot * 0.017453292F - ((float) Math.PI / 2F));

                level.addParticle(ParticleTypes.SMOKE, getX() + vec3d.x,
                        getY() + vec3d.y, getZ() + vec3d.z, this.getDeltaMovement().x,
                        this.getDeltaMovement().y + MathUtils.getDoubleInRange(0.015,0.035), this.getDeltaMovement().z);
            }
        }


        //System.out.println(kmh);
        //Entity collision handling
        if (kmh >= 3 && !level.isClientSide()) {
            List<Entity> list = this.level.getEntities(this, this.getBoundingBox().inflate((double) 0.2F, (double) -0.01F, (double) 0.2F), EntityPredicates.pushableBy(this));
            if (!list.isEmpty()) {

                for (int j = 0; j < list.size(); ++j) {
                    Entity entity = list.get(j);
                    if (entity instanceof LivingEntity) {
                        if (!entity.hasPassenger(this) && !this.hasPassenger(entity)) {
                            double force = kmh / 6d;
                            entity.push((double) (-MathHelper.sin(this.yRot * ((float) Math.PI / 180F)) * (float) force * 0.5F), 0.1D, (double) (MathHelper.cos(this.yRot * ((float) Math.PI / 180F)) * (float) force * 0.5F));
                            entity.hurt(DamageSources.causeVehicleDamage(this, entity), (float) Math.floor(1.5 * kmh));
                            hurt(DamageSource.mobAttack((LivingEntity) entity), (float) Math.floor(0.25 * kmh));
                            //this.push(entity,90);
                        }
                    }
                }
            }
        }
    }

    @Override
    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    @Override
    public boolean canBeDriven() {
        return true /*this.canBeSteered() && this.isComplete() && getCharged()*/;
    }

    @Override
    public double getAcceleration(){
        return (1 + (double) this.getCalculatedQuality() / ServerConfig.maxQuality.get()) * 0.05;
    }

    @Override
    public void positionRider(Entity p_226266_1_) {
        this.positionRider(p_226266_1_, Entity::setPos);
    }


    private void positionRider(Entity passenger, IMoveCallback posY) {
        if (this.hasPassenger(passenger)) {
            /*float f = 0.0F;
            float f1 = (float) ((this.removed ? (double) 0.01F : this.getPassengersRidingOffset()) + passenger.getMyRidingOffset());
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(passenger);
                if (i == 0) {
                    f = 0.2F;
                } else {
                    f = -0.6F;
                }

                if (passenger instanceof AnimalEntity) {
                    f = (float) ((double) f + 0.2D);
                }
            }

            Vector3d vector3d = (new Vector3d((double) f, 0.0D, 0.0D)).yRot(-this.yRot * ((float) Math.PI / 180F) - ((float) Math.PI / 2F));
            posY.accept(passenger, this.getX() + vector3d.x, this.getY() + (double) f1, this.getZ() + vector3d.z);
            passenger.yRot += this.yRot - this.yRotO;
            passenger.setYHeadRot(passenger.getYHeadRot() + this.yRot - this.yRotO);
            this.applyYawToEntity(passenger);

            if (passenger instanceof AnimalEntity && this.getPassengers().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(((AnimalEntity) passenger).yBodyRot + (float) j);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
            }*/


            float f = 0.18F;
            float g = -0.4F;
            float f1 = (float) ((this.removed ? 0.009999999776482582D : this.getPassengersRidingOffset())
                    + passenger.getMyRidingOffset());

            int i = this.getPassengers().indexOf(passenger);

            if (i > 1) {
                f = -0.6f;
            }

            if (i % 2 == 1) {
                g = 0.4F;
            }

            if (passenger instanceof AnimalEntity) {
                f = (float) ((double) f + 0.2D);
            }

            Vector3d vec3d = (new Vector3d((double) f, 0.0D, g))
                    .yRot(-this.yRot * 0.017453292F - ((float) Math.PI / 2F));
            passenger.setPos(this.getX() + vec3d.x, this.getY() + (double) f1, this.getZ() + vec3d.z);
            passenger.yRot += this.yRot - this.yRotO;
            passenger.setYHeadRot(passenger.getYHeadRot() + this.yRot - this.yRotO);
            this.applyYawToEntity(passenger);

            if (passenger instanceof AnimalEntity && this.getPassengers().size() > 1) {
                int j = passenger.getId() % 2 == 0 ? 90 : 270;
                passenger.setYBodyRot(((AnimalEntity) passenger).yBodyRot + (float) j);
                passenger.setYHeadRot(passenger.getYHeadRot() + (float) j);
            }

        }
    }

    @Override
    public Container createMenu(int windowiD, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ContainerCar.createContainerServerSide(windowiD, playerInventory, this);
    }
}