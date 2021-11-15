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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.block.BlockBush;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.config.EnumQualityState;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModBlocks;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.entity.ContainerMinibike;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MinibikeEntity extends LivingEntity implements INamedContainerProvider {
    public final static UUID SPEED_MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");
    public static final float MAX_FUEL = 5000;
    private static final DataParameter<Boolean> ENGINE = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> BATTERY = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> WHEELS = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> SEAT = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> HANDLES = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> CHEST = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> CHASSIS_QUALITY = EntityDataManager
            .<Integer>defineId(MinibikeEntity.class, DataSerializers.INT);
    private static final DataParameter<Integer> CALCULATED_QUALITY = EntityDataManager
            .<Integer>defineId(MinibikeEntity.class, DataSerializers.INT);
    private static final DataParameter<Boolean> CHARGED = EntityDataManager.<Boolean>defineId(MinibikeEntity.class,
            DataSerializers.BOOLEAN);
    private static final DataParameter<Float> FUEL = EntityDataManager.<Float>defineId(MinibikeEntity.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> TURNING = EntityDataManager.<Float>defineId(MinibikeEntity.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> TURNING_PREV = EntityDataManager.<Float>defineId(MinibikeEntity.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> FRONT_ROTATION = EntityDataManager.<Float>defineId(MinibikeEntity.class,
            DataSerializers.FLOAT);
    private static final DataParameter<Float> FRONT_ROTATION_PREV = EntityDataManager.<Float>defineId(MinibikeEntity.class,
            DataSerializers.FLOAT);
    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);
    public float wheelAngle;
    public float wheelAnglePrev;
    public long nextIdleSound = 0;

    //Only for reading outside of this class, syncing is not ensured
    public double kmh;

    public MinibikeEntity(EntityType<MinibikeEntity> type, World world) {
        super(type, world);
        this.maxUpStep = 1.5f;
    }

    public MinibikeEntity(World world) {
        this(ModEntities.MINIBIKE.get(), world);
    }

    public static AttributeModifierMap createAttributes() {
        return MonsterEntity.createMonsterAttributes().add(Attributes.FOLLOW_RANGE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175F).add(Attributes.ATTACK_DAMAGE, 0.0D)
                .add(Attributes.ARMOR, 0.0D).add(Attributes.MAX_HEALTH, 60).build();
    }

    protected ExtendedInventory initInventory() {
        final MinibikeEntity minibike = this;
        return new ExtendedInventory(getInventorySize()) {
            @Override
            protected void onContentsChanged(int slot) {
                if (minibike != null) {
                    minibike.onInventoryChanged(this);
                }
            }
        };
    }

    public int getInventorySize() {
        return 16;
    }

    public ExtendedInventory getInventory() {
        return this.inventory.orElse(null);
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.getEntityData().define(ENGINE, false);
        this.getEntityData().define(BATTERY, false);
        this.getEntityData().define(WHEELS, false);
        this.getEntityData().define(SEAT, false);
        this.getEntityData().define(HANDLES, false);
        this.getEntityData().define(CHEST, false);
        this.getEntityData().define(CHARGED, false);
        this.getEntityData().define(CHASSIS_QUALITY, 1);
        this.getEntityData().define(CALCULATED_QUALITY, -1);
        this.getEntityData().define(FUEL, 100f);
        this.getEntityData().define(TURNING, 0f);
        this.getEntityData().define(TURNING_PREV, 0f);
        this.getEntityData().define(FRONT_ROTATION, 0f);
        this.getEntityData().define(FRONT_ROTATION_PREV, 0f);
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

    public boolean getWheels() {
        return this.getEntityData().get(WHEELS);
    }

    protected void setWheels(boolean state) {
        this.getEntityData().set(WHEELS, state);
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

    public boolean getChest() {
        return this.getEntityData().get(CHEST);
    }

    protected void setChest(boolean state) {
        this.getEntityData().set(CHEST, state);
    }

    public int getChassisQuality() {
        return this.getEntityData().get(CHASSIS_QUALITY);
    }

    protected void setChassisQuality(int quality) {
        this.getEntityData().set(CHASSIS_QUALITY, quality);
    }

    public int getCalculatedQuality() {
        return this.getEntityData().get(CALCULATED_QUALITY);
    }

    protected void setCalculatedQuality(int quality) {
        this.getEntityData().set(CALCULATED_QUALITY, quality);
    }

    public float getFuel() {
        return this.getEntityData().get(FUEL);
    }

    protected void setFuel(float fuel) {
        this.getEntityData().set(FUEL, MathUtils.clamp(fuel, 0, MAX_FUEL));
    }

    public boolean getCharged() {
        return this.getEntityData().get(CHARGED);
    }

    protected void setCharged(boolean state) {
        this.getEntityData().set(CHARGED, state);
    }

    public float getTurning() {
        return this.getEntityData().get(TURNING);
    }

    protected void setTurning(float turning) {
        this.getEntityData().set(TURNING, turning);
    }

    public float getTurningPrev() {
        return this.getEntityData().get(TURNING_PREV);
    }

    protected void setTurningPrev(float turning) {
        this.getEntityData().set(TURNING_PREV, turning);
    }

    public float getFrontRotation() {
        return this.getEntityData().get(FRONT_ROTATION);
    }

    protected void setFrontRotation(float rotation) {
        this.getEntityData().set(FRONT_ROTATION, rotation);
    }

    public float getFrontRotationPrev() {
        return this.getEntityData().get(FRONT_ROTATION_PREV);
    }

    protected void setFrontRotationPrev(float rotationPrev) {
        this.getEntityData().set(FRONT_ROTATION_PREV, rotationPrev);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {

        if (compound.contains("engine", Constants.NBT.TAG_BYTE)) {
            this.setEngine(compound.getBoolean("engine"));
        }
        if (compound.contains("battery", Constants.NBT.TAG_BYTE)) {
            this.setBattery(compound.getBoolean("battery"));
        }
        if (compound.contains("wheels", Constants.NBT.TAG_BYTE)) {
            this.setWheels(compound.getBoolean("wheels"));
        }
        if (compound.contains("seat", Constants.NBT.TAG_BYTE)) {
            this.setSeat(compound.getBoolean("seat"));
        }
        if (compound.contains("handles", Constants.NBT.TAG_BYTE)) {
            this.setHandles(compound.getBoolean("handles"));
        }
        if (compound.contains("chest", Constants.NBT.TAG_BYTE)) {
            this.setChest(compound.getBoolean("chest"));
        }
        if (compound.contains("chassis_quality", Constants.NBT.TAG_INT)) {
            this.setChassisQuality(compound.getInt("chassis_quality"));
        }

        if (compound.contains("calculated_quality", Constants.NBT.TAG_INT)) {
            this.setCalculatedQuality(compound.getInt("calculated_quality"));
        }

        if (compound.contains("charged", Constants.NBT.TAG_BYTE)) {
            this.setCharged(compound.getBoolean("charged"));
        }

        if (compound.contains("fuel", Constants.NBT.TAG_FLOAT)) {
            this.setFuel(compound.getFloat("fuel"));
        }

        if (compound.contains("turning", Constants.NBT.TAG_FLOAT)) {
            this.setTurning(compound.getFloat("turning"));
        }

        if (compound.contains("turning_prev", Constants.NBT.TAG_FLOAT)) {
            this.setTurningPrev(compound.getFloat("turning_prev"));
        }

        if (compound.contains("front_rotation", Constants.NBT.TAG_FLOAT)) {
            this.setFrontRotation(compound.getFloat("front_rotation"));
        }

        if (compound.contains("front_rotation_prev", Constants.NBT.TAG_FLOAT)) {
            this.setFrontRotationPrev(compound.getFloat("front_rotation_prev"));
        }

        if (getInventory() != null && compound.contains("ItemHandler")) {
            getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {


        compound.putBoolean("engine", getEngine());
        compound.putBoolean("battery", getBattery());
        compound.putBoolean("wheels", getWheels());
        compound.putBoolean("seat", getSeat());
        compound.putBoolean("handles", getHandles());
        compound.putBoolean("chest", getChest());
        compound.putInt("chassis_quality", getChassisQuality());
        compound.putInt("calculated_quality", getCalculatedQuality());
        compound.putBoolean("charged", getCharged());
        compound.putFloat("fuel", getFuel());
        compound.putFloat("turning", getTurning());
        compound.putFloat("turning_prev", getTurningPrev());
        compound.putFloat("front_rotation", getFrontRotation());
        compound.putFloat("front_rotation_prev", getFrontRotationPrev());

        if (getInventory() != null) {
            compound.put("ItemHandler", getInventory().serializeNBT());
        }
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
    public Iterable<ItemStack> getArmorSlots() {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType p_184582_1_) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType p_184201_1_, ItemStack p_184201_2_) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return getChest() ? this.getPassengers().size() < 1 : this.getPassengers().size() < 2;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public double getPassengersRidingOffset() {
        return (double) this.getDimensions(Pose.STANDING).height * 0.62D;
    }

    public boolean canBeSteered() {
        return this.getControllingPassenger() instanceof LivingEntity;
    }

    public boolean isComplete() {
        return this.getBattery() && this.getEngine() && this.getHandles() && this.getSeat() && this.getWheels();
    }

    public void onInventoryChanged(ExtendedInventory inv) {
        this.updateInventory();
    }

    public boolean isBateryCharged() {
        ItemStack battery = getInventory().getStackInSlot(3);
        if (battery.isEmpty())
            return false;
        Item item = battery.getItem();
        if (!(item instanceof IBattery))
            return false;
        IBattery bat = (IBattery) item;
        return bat.getVoltage(battery, level) > 0;
    }

    public Vector2f getPitchYawServerSide() {
        return new Vector2f(this.xRot, this.yRot);
    }

    public Vector3d getForwardServerSide() {
        return Vector3d.directionFromRotation(this.getPitchYawServerSide());
    }

    public void updateInventory() {
        this.setHandles(getInventory().getStackInSlot(0).getItem() == ModItems.MINIBIKE_HANDLES.get());
        this.setWheels(getInventory().getStackInSlot(1).getItem() == ModBlocks.WHEELS.get().asItem());
        this.setSeat(getInventory().getStackInSlot(2).getItem() == ModItems.MINIBIKE_SEAT.get());
        this.setBattery(getInventory().getStackInSlot(3).getItem() instanceof IBattery);
        this.setEngine(getInventory().getStackInSlot(4).getItem() == ModItems.SMALL_ENGINE.get());
        this.setChest(getInventory().getStackInSlot(5).getItem() == Blocks.CHEST.asItem());

        int quality = -1;

        if (this.getHandles() && this.getBattery() && this.getWheels() && this.getSeat() && this.getEngine()) {
            if (EnumQualityState.isQualitySystemOn()) {
                quality = 0;
                int items = 0;
                for (int i = 0; i < 5; i++) {
                    ItemStack stack = this.getInventory().getStackInSlot(i);
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

        /*if (this.isComplete()) {
            AttributeModifier speedModifier = new AttributeModifier(SPEED_MODIFIER_UUID, "Speed Modifier",
                    1+(quality / 3333f), AttributeModifier.Operation.MULTIPLY_TOTAL);
            if (this.getAttribute(Attributes.MOVEMENT_SPEED)
                    .getModifier(SPEED_MODIFIER_UUID) != null) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(this
                        .getAttribute(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_UUID));
            }
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            this.getAttribute(Attributes.MOVEMENT_SPEED).addTransientModifier(speedModifier);
        } else {
            if (this.getAttribute(Attributes.MOVEMENT_SPEED)
                    .getModifier(SPEED_MODIFIER_UUID) != null) {
                this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(this
                        .getAttribute(Attributes.MOVEMENT_SPEED).getModifier(SPEED_MODIFIER_UUID));
            }
        }*/
    }

    @Override
    public ActionResultType interact(PlayerEntity playerEntity, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            ItemStack stack = playerEntity.getItemInHand(hand);
            if (playerEntity.isCrouching()) {
                if (playerEntity instanceof ServerPlayerEntity) {
                    if (stack.getItem() == ModItems.GAS_CANISTER.get()) {
                        if (this.getFuel() < MAX_FUEL) {
                            this.setFuel(this.getFuel() + 250);
                            stack.shrink(1);
                            level.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.BOTTLE_EMPTY,
                                    SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
                                    MathUtils.getFloatInRange(0.9f, 1f));
                        }
                    }
                    else if(stack.getItem() == ModItems.WRENCH.get()){
                        if (this.getHealth() < this.getMaxHealth()) {
                            ItemStack toConsume = new ItemStack(Items.IRON_INGOT, 1);
                            if (Utils.hasItemStack(playerEntity, toConsume)) {
                                Utils.removeItemStack(playerEntity.inventory, toConsume);
                                stack.hurt(1, random, (ServerPlayerEntity) playerEntity);
                                this.heal(this.getMaxHealth() / 5f);
                                level.playSound(null, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), SoundEvents.ANVIL_USE,
                                        SoundCategory.BLOCKS, MathUtils.getFloatInRange(0.5f, 0.75f),
                                        MathUtils.getFloatInRange(0.9f, 1f));
                            }
                            else {
                                playerEntity.sendMessage(new TranslationTextComponent("repair.missing",toConsume.getDisplayName(),this.getTypeName()),Util.NIL_UUID);
                            }
                        }
                        else{
                            playerEntity.sendMessage(new TranslationTextComponent("repair.repaired",this.getTypeName()),Util.NIL_UUID);
                        }
                    }
                    else {
                        ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
                        NetworkHooks.openGui(serverPlayerEntity, this, (packetBuffer) -> packetBuffer.writeInt(this.getId()));
                    }
                }

                return ActionResultType.SUCCESS;
            } else {
                return playerEntity.startRiding(this) ? ActionResultType.CONSUME : ActionResultType.PASS;
            }
        }
        return ActionResultType.PASS;
    }

    @Override
    public void tick() {
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
            this.setDeltaMovement(this.getDeltaMovement().add(0, -0.8, 0));
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
                        this.setFuel((this.getFuel() - (10F / ItemUtils.getQuality(getInventory().getStackInSlot(4)))));
                    }
                }
                if (state.isFaceSturdy(level, pos, Direction.UP)) {
                    for (int j = 0; j < 10; j++) {
                        level.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state).setPos(pos), getX() + random.nextDouble() * 0.2,
                                getY() + random.nextDouble() * 0.2, getZ() + random.nextDouble() * 0.2, getDeltaMovement().x, getDeltaMovement().y,
                                getDeltaMovement().z);
                    }
                }
            }
            if (kmh > 5) {

                for (int k = 0; (double) k < 1.0D + kmh / 8; ++k) {
                    double d5 = (double) (this.random.nextFloat() * 2.0F - 1.0F);
                    double d6 = (double) (this.random.nextInt(2) * 2 - 1) * 0.7D;

                    double d7 = this.getX();
                    double d8 = this.getZ();

                    level.addParticle(ParticleTypes.SMOKE, d7, this.getY() + 0.12, d8, this.getDeltaMovement().x,
                            this.getDeltaMovement().y, this.getDeltaMovement().z);

                }
            }
        }
        if (this.getHealth() <= this.getMaxHealth() * 0.2) {
            if (this.getHealth() <= this.getMaxHealth() * 0.1) {
                for (int k = 0; k < random.nextInt(2); k++) {
                    level.addParticle(ParticleTypes.FLAME, getX() + random.nextDouble() * 0.3 - 0.15,
                            getY() + 0.25, getZ() + random.nextDouble() * 0.3 - 0.15, this.getDeltaMovement().x,
                            this.getDeltaMovement().y + 0.02, this.getDeltaMovement().z);
                }
            }
            for (int k = 0; k < random.nextInt(3); k++) {
                level.addParticle(ParticleTypes.SMOKE, getX() + random.nextDouble() * 0.3 - 0.15,
                        getY() + 0.25, getZ() + random.nextDouble() * 0.3 - 0.15, this.getDeltaMovement().x,
                        this.getDeltaMovement().y + 0.07, this.getDeltaMovement().z);
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

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    public boolean canBeDriven() {
        return true /*this.canBeSteered() && this.isComplete() && getCharged()*/;
    }

    public double getAcceleration(){
        return (1 + (double) this.getCalculatedQuality() / CommonConfig.maxQuality.get()) * 0.05;
    }

    @Override
    public void travel(Vector3d vec) {
        double acceleration = getAcceleration();
        double strafe = vec.x;
        double forward = vec.y;
        double vertical = vec.z;
        if (this.getControllingPassenger() != null /* && canBeDriven() && this.getFuel() > 0*/) {
            LivingEntity entitylivingbase = (LivingEntity) this.getControllingPassenger();
            strafe = entitylivingbase.xxa;
            forward = entitylivingbase.zza;
            //vertical = entitylivingbase.zza;

            if (forward < 0) {
                forward *= 0.4;
                strafe = -strafe;
            }

            if (!this.verticalCollision)
                acceleration *= 0.05;

            Vector3d vector3d = this.getForwardServerSide();
            this.setDeltaMovement(this.getDeltaMovement().add(forward * vector3d.x * acceleration, 0, forward * vector3d.z * acceleration));


            if (forward != 0) {
                if (strafe != 0) {
                    this.setTurning((float) (getTurning() + strafe * 2f));
                    this.yRot -= strafe * 4;
                    this.yBodyRot = this.yRot;
                }
                if (yRot > 180) {
                    yRot -= 360;
                }
                if (!level.isClientSide()) {
                    ItemStack engine = this.getInventory().getStackInSlot(4);
                    if (engine != null && engine.hasTag()) {
                        int quality = engine.getTag().getInt("Quality");
                        this.setFuel(getFuel() - (1.5f - (quality / CommonConfig.maxQuality.get())));
                    }

                    ItemStack battery = getInventory().getStackInSlot(3);
                    if (!battery.isEmpty() && level.random.nextInt(1) == 0 && battery.getItem() instanceof IBattery) {
                        IBattery bat = (IBattery) battery.getItem();
                        bat.drainVoltage(battery, level, 1);
                    }

                }
            }

        }

        if (!level.isClientSide()) {
            //System.out.println(strafe);
            float prevFront = this.getFrontRotation() * 0.9f;
            if (Math.abs(prevFront) <= 0.0001) {
                prevFront = 0;
            }

            prevFront += strafe * 0.05f * (forward < 0 ? 1 : -1);

            prevFront = MathUtils.clamp(prevFront, -1, 1);
            setFrontRotationPrev(getFrontRotation());
            setFrontRotation((float) prevFront);
        }
    }

    @Override
    public void positionRider(Entity p_226266_1_) {
        this.positionRider(p_226266_1_, Entity::setPos);
    }


    private void positionRider(Entity passenger, Entity.IMoveCallback posY) {
        if (this.hasPassenger(passenger)) {
            float f = 0.0F;
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
            }

        }
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setYBodyRot(this.yRot);
        float f = MathHelper.wrapDegrees(entityToUpdate.yRot - this.yRot);
        float f1 = MathHelper.clamp(f, -90F, 90F);
        entityToUpdate.yRotO += f1 - f;
        entityToUpdate.yRot += f1 - f;
        entityToUpdate.setYHeadRot(entityToUpdate.yRot);
    }

    @Override
    public Container createMenu(int windowiD, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return ContainerMinibike.createContainerServerSide(windowiD, playerInventory, this);
    }

    @Override
    public ITextComponent getName() {
        ITextComponent itextcomponent = this.getCustomName();
        if(itextcomponent != null) return super.getName();

        int quality = this.getCalculatedQuality();

        if(quality > 0){
            EnumQuality tier = EnumQuality.getFromQuality(quality);
            IFormattableTextComponent qualityTitle = new TranslationTextComponent("stat.quality." + tier.name().toLowerCase());
            Style style = qualityTitle.getStyle().withColor(tier.getColor());

            ITextComponent type = getTypeName();
            if(type instanceof TranslationTextComponent){
                ((TranslationTextComponent )type).setStyle(style);
                qualityTitle.setStyle(style);
            }
            return qualityTitle.append(" ").append(type);
        }
        return new TranslationTextComponent("stat.unfinished").append(" ").append(this.getTypeName().getString());
    }
}
