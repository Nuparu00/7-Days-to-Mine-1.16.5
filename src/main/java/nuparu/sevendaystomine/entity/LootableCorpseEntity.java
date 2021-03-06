package nuparu.sevendaystomine.entity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import nuparu.sevendaystomine.capability.ExtendedInventory;
import nuparu.sevendaystomine.capability.ExtendedInventoryProvider;
import nuparu.sevendaystomine.config.ServerConfig;
import nuparu.sevendaystomine.init.ModEntities;
import nuparu.sevendaystomine.inventory.entity.ContainerLootableCorpse;
import nuparu.sevendaystomine.item.ItemFuelTool;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.SpawnBloodMessage;
import nuparu.sevendaystomine.util.Utils;
import nuparu.sevendaystomine.util.MathUtils;

public class LootableCorpseEntity extends Entity implements INamedContainerProvider {

	protected final LazyOptional<ExtendedInventory> inventory = LazyOptional.of(this::initInventory);

	public int health = 40;
	public long age = 0;
	private boolean onEntity = false;
	private static final DataParameter<CompoundNBT> ORIGINAL_NBT = EntityDataManager
			.<CompoundNBT>defineId(LootableCorpseEntity.class, DataSerializers.COMPOUND_TAG);

	Entity originalCached = null;

	public LootableCorpseEntity(EntityType<LootableCorpseEntity> type, World world) {
		super(type, world);
	}

	public LootableCorpseEntity(World world) {
		this(ModEntities.LOOTABLE_CORPSE.get(), world);
	}

	public void setOriginal(Entity entity) {
		if (entity instanceof LivingEntity) {
			LivingEntity living = ((LivingEntity) entity);
			living.hurtTime = 0;
			living.swingTime = 0;
			living.deathTime = 0;
		}

		this.xRot = entity.xRot;
		this.xRotO = entity.xRotO;
		this.yRot = entity.yRot;
		this.yRotO = entity.yRotO;
		this.setYHeadRot(entity.getYHeadRot());

		entity.setDeltaMovement(new Vector3d(0, 0, 0));
		entity.setPose(Pose.STANDING);
		entity.clearFire();
		entity.setYHeadRot(0);
		entity.xRot = 0;
		entity.yRot = 0;

		CompoundNBT nbt = new CompoundNBT();

		if (entity != null) {
			CompoundNBT entityNBT = entity.saveWithoutId(new CompoundNBT());
			entityNBT.putString("id", entity.getEncodeId());
			nbt.put("entity", entityNBT);
			nbt.putString("resourceLocation", EntityType.getKey(entity.getType()).toString());
		}
		setOriginalNBT(nbt);
	}

	public Entity getOriginal() {
		if (originalCached == null) {
			CompoundNBT nbt = getOriginalNBT();
			originalCached = Utils.getEntityByNBTAndResource(new ResourceLocation(nbt.getString("resourceLocation")),
					nbt.getCompound("entity"), level);
		}
		return originalCached;
	}

	public void setOriginalNBT(CompoundNBT nbt) {
		this.getEntityData().set(ORIGINAL_NBT, nbt);
	}

	public CompoundNBT getOriginalNBT() {
		return this.getEntityData().get(ORIGINAL_NBT);
	}

	@Override
	protected void defineSynchedData() {
		this.getEntityData().define(ORIGINAL_NBT, new CompoundNBT());
	}

	@Override
	protected void readAdditionalSaveData(CompoundNBT compound) {

		if (compound.contains("originalEntity")) {
			setOriginalNBT(compound.getCompound("originalEntity"));
		}
		onEntity = compound.getBoolean("onEntity");
		age = compound.getLong("age");
		health = compound.getInt("health");
		if (getInventory() != null && compound.contains("ItemHandler")) {
			getInventory().deserializeNBT(compound.getCompound("ItemHandler"));
		}
	}

	@Override
	protected void addAdditionalSaveData(CompoundNBT compound) {

		compound.put("originalEntity", getOriginalNBT());
		compound.putBoolean("onEntity", onEntity);
		compound.putLong("age", age);
		compound.putInt("health", health);
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
		if(!playerEntity.isCrouching() && hand == Hand.MAIN_HAND) {
			if(playerEntity instanceof ServerPlayerEntity) {
				ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
				NetworkHooks.openGui(serverPlayerEntity, this, (packetBuffer) -> packetBuffer.writeInt(this.getId()));
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
			if (this.age >= ServerConfig.corpseLifespan.get()) {
				this.kill();
				return;
			}
		}

		double motionX = this.getDeltaMovement().x;
		double motionY = this.getDeltaMovement().y;
		double motionZ = this.getDeltaMovement().z;

		if (!onGround && !onEntity) {
			motionY -= 0.03999999910593033D;
		} else {
			motionY = 0;
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
		if (this.age < 20)
			return super.hurt(source, amount);
		if (this.level.isClientSide()) {
			level.playLocalSound(this.getX(), this.getY(), this.getZ(),
					SoundEvents.GENERIC_HURT, SoundCategory.HOSTILE, 1.0F, 1.0F, false);
			/*for (int i = 0; i < (int) Math
					.round(MathUtils.getDoubleInRange(1, 5) * SevenDaysToMine.proxy.getParticleLevel()); i++) {
				double x = this.getX() + MathUtils.getDoubleInRange(-1, 1) * this.getBbWidth();
				double y = this.getY() + MathUtils.getDoubleInRange(0, 1) * this.getBbHeight();
				double z = this.getZ() + MathUtils.getDoubleInRange(-1, 1) * this.getBbWidth();
				SevenDaysToMine.proxy.addParticle(this.level, EnumModParticleType.BLOOD, x, y, z,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d, MathUtils.getDoubleInRange(-0.5d, 1d) / 7d,
						MathUtils.getDoubleInRange(-1d, 1d) / 7d);
			}*/
		}
		else {
			for (int i = 0; i < MathUtils.getIntInRange(level.random, 20, 35); i++) {
				PacketManager.sendToTrackingEntity(PacketManager.spawnBlood, new SpawnBloodMessage(position().x, getY() + getBbHeight() * MathUtils.getFloatInRange(0.4f, 0.75f), position().z, MathUtils.getFloatInRange(-0.1f, 0.1f), MathUtils.getFloatInRange(0.1f, 0.22f), MathUtils.getFloatInRange(-0.1f, 0.1f)), () -> this);
			}
		}
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
		this.health -= amount;
		if (this.health <= 0) {
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
		return ContainerLootableCorpse.createContainerServerSide(windowiD,playerInventory,this);
	}

	@Override
	public ITextComponent getDisplayName() {
		if (this.getOriginal() != null){
			return getOriginal().getDisplayName();
		}
		return super.getDisplayName();
	}
}
