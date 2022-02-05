package nuparu.sevendaystomine.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
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
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.electricity.IBattery;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.EnumQuality;
import nuparu.sevendaystomine.util.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.UUID;

public abstract class VehicleEntity extends LivingEntity implements INamedContainerProvider {
    public final static UUID SPEED_MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");
    public static final float MAX_FUEL = 5000;

    protected static final DataParameter<Integer> CHASSIS_QUALITY = EntityDataManager
            .<Integer>defineId(VehicleEntity.class, DataSerializers.INT);
    protected static final DataParameter<Integer> CALCULATED_QUALITY = EntityDataManager
            .<Integer>defineId(VehicleEntity.class, DataSerializers.INT);
    protected static final DataParameter<Boolean> CHARGED = EntityDataManager.<Boolean>defineId(VehicleEntity.class,
            DataSerializers.BOOLEAN);
    protected static final DataParameter<Float> FUEL = EntityDataManager.<Float>defineId(VehicleEntity.class,
            DataSerializers.FLOAT);
    protected static final DataParameter<Float> TURNING = EntityDataManager.<Float>defineId(VehicleEntity.class,
            DataSerializers.FLOAT);
    protected static final DataParameter<Float> TURNING_PREV = EntityDataManager.<Float>defineId(VehicleEntity.class,
            DataSerializers.FLOAT);
    protected static final DataParameter<Float> FRONT_ROTATION = EntityDataManager.<Float>defineId(VehicleEntity.class,
            DataSerializers.FLOAT);
    protected static final DataParameter<Float> FRONT_ROTATION_PREV = EntityDataManager.<Float>defineId(VehicleEntity.class,
            DataSerializers.FLOAT);
    protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);
    public float wheelAngle;
    public float wheelAnglePrev;
    public long nextIdleSound = 0;

    //Only for reading outside of this class, syncing is not ensured
    public double kmh;

    public VehicleEntity(EntityType<? extends VehicleEntity> type, World world) {
        super(type, world);
        this.maxUpStep = 1.5f;
    }

    protected ExtendedInventory initInventory() {
        final VehicleEntity minibike = this;
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
        this.getEntityData().define(CHARGED, false);
        this.getEntityData().define(CHASSIS_QUALITY, 1);
        this.getEntityData().define(CALCULATED_QUALITY, -1);
        this.getEntityData().define(FUEL, 0f);
        this.getEntityData().define(TURNING, 0f);
        this.getEntityData().define(TURNING_PREV, 0f);
        this.getEntityData().define(FRONT_ROTATION, 0f);
        this.getEntityData().define(FRONT_ROTATION_PREV, 0f);
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
        compound.putInt("chassis_quality", getChassisQuality());
        compound.putInt("calculated_quality", getCalculatedQuality());
        compound.putBoolean("charged", getCharged());
        compound.putFloat("fuel", getFuel());
        compound.putFloat("turning", getTurning());
        compound.putFloat("turning_prev", getTurningPrev());
        compound.putFloat("front_rotation", getFrontRotation());
        compound.putFloat("front_rotation_prev", getFrontRotationPrev());

        ExtendedInventory inv = this.getInventory();
        if(inv == null) return;
        compound.put("ItemHandler", inv.serializeNBT());

    }

    public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
        ExtendedInventory inv = this.getInventory();
        if(inv == null) return false;
        inv.setStackInSlot(inventorySlot, itemStackIn);
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

    public abstract boolean isComplete();

    public void onInventoryChanged(ExtendedInventory inv) {
        this.updateInventory();
    }

    public Vector2f getPitchYawServerSide() {
        return new Vector2f(this.xRot, this.yRot);
    }

    public Vector3d getForwardServerSide() {
        return getViewVector(1);
    }

    public Vector3d getLookAngle() {
        return super.getLookAngle();
    }

    @Override
    public float getViewXRot(float p_195050_1_) {
        if(!level.isClientSide()) return this.xRot;
        return p_195050_1_ == 1.0F ? this.xRot : MathHelper.lerp(p_195050_1_, this.xRotO, this.xRot);
    }
    @Override
    public float getViewYRot(float p_195046_1_) {
        if(!level.isClientSide()) return this.yRot;
        return p_195046_1_ == 1.0F ? this.yRot : MathHelper.lerp(p_195046_1_, this.yRotO, this.yRot);
    }


    public abstract void updateInventory();

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
        super.tick();
    }

    @Nullable
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : (Entity) this.getPassengers().get(0);
    }

    public boolean canBeDriven() {
        return this.canBeSteered() && this.isComplete() && getCharged();
    }

    public double getAcceleration(){
        return (1 + (double) this.getCalculatedQuality() / ServerConfig.maxQuality.get()) * 0.05;
    }

    @Override
    public void travel(Vector3d vec) {

        double acceleration = getAcceleration();
        double strafe = vec.x;
        double forward = vec.y;
        double vertical = vec.z;
        if (this.getControllingPassenger() != null && canBeDriven() && this.getFuel() > 0) {
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
                    double yP = yRot;
                    this.yRot -= strafe * 4;
                    this.yBodyRot = this.yRot;
                    System.out.println(level.isClientSide + " " + (yRot-yP));
                }
                if (yRot > 180) {
                    yRot -= 360;
                }
                if (!level.isClientSide()) {
                    ExtendedInventory inv = this.getInventory();
                    if(inv == null) return;
                    ItemStack engine = inv.getStackInSlot(4);
                    if (engine != null && engine.hasTag()) {
                        int quality = engine.getTag().getInt("Quality");
                        this.setFuel(getFuel() - (1.5f - (quality / ServerConfig.maxQuality.get())));
                    }

                    ItemStack battery = inv.getStackInSlot(3);
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
