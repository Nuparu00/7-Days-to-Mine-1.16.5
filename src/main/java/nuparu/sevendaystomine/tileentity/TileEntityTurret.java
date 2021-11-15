package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.block.BlockHorizontalBase;
import nuparu.sevendaystomine.block.BlockTurretBase;
import nuparu.sevendaystomine.entity.ShotEntity;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.inventory.itemhandler.ItemHandlerNameable;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.TurretShotMessage;

public abstract class TileEntityTurret extends TileEntityItemHandler<ItemHandlerNameable>
		implements ITickableTileEntity, INetwork {

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();

	public static final double VIEW_DISTANCE = 16;
	public static final ITextComponent DEFAULT_NAME = new TranslationTextComponent("container.turret");

	private Direction facing;

	public float headRotation = 0f;
	public float headRotationPrev = 0f;
	public int headRotationMaximumReached = 0; // 0 == LEFT ;1== RIGHT
	public int maxMemory = 400;// In ticks
	public int memory = 0;
	public int maxDelay = 5;
	public int delay = 0;
	private boolean on = false;

	public Entity target;
	public AITurretTarget targetAI;
	public AITurretShoot shootAI;

	public List<String> whitelistedTypes = new ArrayList<String>();

	public TileEntityTurret(TileEntityType<?> type) {
		super(type);
		targetAI = new AITurretTarget(this);
		shootAI = new AITurretShoot(this);
	}

	@Override
	protected ItemHandlerNameable createInventory() {
		return new ItemHandlerNameable(9, DEFAULT_NAME);
	}

	@Override
	public void onContainerOpened(PlayerEntity player) {

	}

	@Override
	public void onContainerClosed(PlayerEntity player) {

	}

	@Override
	public boolean isUsableByPlayer(PlayerEntity player) {
		return this.level.getBlockEntity(this.worldPosition) == this
				&& player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5,
						this.worldPosition.getZ() + 0.5) <= 64;
	}

	@Override
	public ResourceLocation getLootTable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.headRotation = compound.getFloat("headRoation");
		this.headRotationPrev = compound.getFloat("headRoationPrev");
		this.headRotationMaximumReached = compound.getInt("headRotationReached");
		if (compound.contains("target", Constants.NBT.TAG_STRING)) {

			if (this.level != null && this.level instanceof ServerWorld) {
				this.target = ((ServerWorld) this.level).getEntity(UUID.fromString(compound.getString("target")));
			}
		} else {
			this.target = null;
		}
		this.maxMemory = compound.getInt("maxMemory");
		this.memory = compound.getInt("memory");

		this.maxDelay = compound.getInt("maxDelay");
		this.delay = compound.getInt("delay");
		this.on = compound.getBoolean("on");

		if (level != null) {
			network.clear();
			ListNBT list = compound.getList("network", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.size(); ++i) {
				LongNBT nbt = (LongNBT) list.get(i);
				BlockPos blockPos = BlockPos.of(nbt.getAsLong());
				network.add(blockPos);
			}
		}

		whitelistedTypes.clear();
		ListNBT list = compound.getList("whitelistedTypes", Constants.NBT.TAG_STRING);
		for (int i = 0; i < list.size(); ++i) {
			StringNBT nbt = (StringNBT) list.get(i);
			whitelistedTypes.add(nbt.getAsString());
		}
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putFloat("headRoation", headRotation);
		compound.putFloat("headRoationPrev", headRotationPrev);
		compound.putInt("headRotationReached", headRotationMaximumReached);
		if (target != null) {
			compound.putString("target", target.getUUID().toString());
		}
		compound.putInt("maxMemory", maxMemory);
		compound.putInt("memory", memory);

		compound.putInt("maxDelay", maxDelay);
		compound.putInt("delay", delay);
		compound.putBoolean("on", on);
		ListNBT list = new ListNBT();
		for (BlockPos net : getConnections()) {
			list.add(LongNBT.valueOf(net.asLong()));
		}
		compound.put("network", list);

		ListNBT targets = new ListNBT();
		for (String s : whitelistedTypes) {
			targets.add(StringNBT.valueOf(s));
		}
		compound.put("whitelistedTypes", targets);

		return compound;
	}

	@Override
	public void tick() {
		if (level.isClientSide()) {
			return;
		}
		this.headRotationPrev = this.headRotation;
		if (delay < maxDelay) {
			delay++;
		}
		if (on) {
			if (this.level.getBlockState(this.worldPosition).getBlock() instanceof BlockTurretBase) {
				facing = this.level.getBlockState(this.worldPosition).getValue(BlockHorizontalBase.FACING);
			}
			if (target == null) {
				if (headRotationMaximumReached == 1) {
					headRotation += 1;
				}
				if (headRotationMaximumReached == 0) {
					headRotation -= 1;
				}
				if (headRotation >= 180) {
					headRotationMaximumReached = 0;
				}
				if (headRotation <= -180) {
					headRotationMaximumReached = 1;
				}
			} else {

				if (!target.isAlive()) {
					target = null;
				} else {
					rotateTowards();
					if (delay == maxDelay && !level.isClientSide()) {
						shootAI.updateAITask();
						delay = 0;
					}
				}
			}

			EntityRayTraceResult ray = rayTrace(this.level, VIEW_DISTANCE);

			if (ray != null && ray.getEntity() != null) {
				targetAI.setTarget(ray.getEntity());
			}
			/*
			 * else { level.setBlockState(new
			 * BlockPos(ray.hitVec.x,ray.hitVec.y-2,ray.hitVec.z),
			 * Blocks.BEDROCK.defaultBlockState()); }
			 */
			targetAI.updateAITask();
			markForUpdate();
		}
	}

	public void rotateTowards() {
		double dX = worldPosition.getX() - target.getX();
		double dY = worldPosition.getY() - target.getY();
		double dZ = worldPosition.getZ() - target.getZ();

		double yaw = Math.atan2(dZ, dX);
		double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;

		Vector3d position = getHeadPosition(0.25);
		Vector3d rotation = getHeadRotation();
		Vector3d entityPosition = new Vector3d(target.getX(), target.getY(), target.getZ());

		Vector3d neededRotation = entityPosition.subtract(position);
		float currentYaw = getYaw(rotation);
		float neededYaw = getYaw(neededRotation);

		float difference = currentYaw - neededYaw;

		if (Math.abs(difference) % 360 == 0) {
			difference = 0;
		}
		if (difference > 0) {
			if (difference >= 1) {
				headRotation -= 1;
			} else {
				headRotation -= difference;
			}
		}
		if (difference < 0) {
			if (difference <= -1) {
				headRotation += 1;
			} else {
				headRotation += difference;
			}

		}
	}

	public static float getYaw(Vector3d vec) {
		double deltaX = vec.x;
		double deltaZ = vec.z;
		double yaw = 0;
		if (deltaX != 0) {
			if (deltaX < 0) {
				yaw = 1.5 * Math.PI;
			} else {
				yaw = 0.5 * Math.PI;
			}
			yaw -= Math.atan(deltaZ / deltaX);
		} else if (deltaZ < 0) {
			yaw = Math.PI;
		}
		return (float) (-yaw * 180 / Math.PI - 90);
	}

	public Vector3d getHeadPosition(double dst) {
		Vector3d rot = getHeadRotation();
		return new Vector3d(worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5)
				.add(new Vector3d(rot.x * dst, rot.y * 0.7, rot.z * dst));
	}

	public Vector3d getHeadRotation() {
		return getVectorFromYawPitch(headRotation
				+ getAngle(level.getBlockState(this.worldPosition).getValue(BlockHorizontalBase.FACING)),
				0);
	}

	public EntityRayTraceResult rayTrace(World worldIn, double distance) {

		Vector3d rotation = getHeadRotation();
		Vector3d position = getHeadPosition(0.25);
		Vector3d rayEnd = position
				.add(new Vector3d(rotation.x * distance, rotation.y * distance, rotation.z * distance));
		Vector3d finalVec = null;

		RayTraceResult mop = worldIn.clip(new RayTraceContext(position, rayEnd, RayTraceContext.BlockMode.OUTLINE,
				RayTraceContext.FluidMode.NONE, null));
		EntityRayTraceResult result = null;
		Entity pointedEntity = null;
		float f = 1.0F;
		List<Entity> list = worldIn.getEntitiesOfClass(Entity.class,
				new AxisAlignedBB(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
						worldPosition.getX() + 1, worldPosition.getY() + 1, worldPosition.getZ() + 1)
								.inflate(distance,  distance, distance),
				EntityPredicates.NO_SPECTATORS);
		double d2 = distance;
		for (int j = 0; j < list.size(); ++j) {
			Entity entity1 = list.get(j);
			if (entity1 instanceof LivingEntity) {
				AxisAlignedBB axisalignedbb = entity1.getBoundingBox();
				Optional<Vector3d> movingobjectposition = axisalignedbb.clip(position, rayEnd);

				if (axisalignedbb.contains(position)) {
					if (d2 >= 0.0D) {
						pointedEntity = entity1;
						finalVec = !movingobjectposition.isPresent() ? position : movingobjectposition.get();
						d2 = 0.0D;
					}
				} else if (movingobjectposition.isPresent()) {
					double d3 = position.distanceTo(movingobjectposition.get());

					if (d3 < d2 || d2 == 0.0D) {
						pointedEntity = entity1;
						finalVec = movingobjectposition.get();
						d2 = d3;

					}
				}
			}
		}

		if (pointedEntity != null && pointedEntity instanceof LivingEntity) {

			double distToEntity = Math.abs(position
					.distanceTo(new Vector3d(pointedEntity.getX(), pointedEntity.getY(), pointedEntity.getZ())));
			BlockRayTraceResult rayFinal = worldIn.clip(new RayTraceContext(position,
					position.add(new Vector3d(rotation.x * distToEntity, rotation.y * distToEntity,
							rotation.z * distToEntity)),
					RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, null));
			if (rayFinal == null || worldIn.getBlockState(rayFinal.getBlockPos()).getMaterial() == Material.AIR) {
				result = new EntityRayTraceResult(pointedEntity, finalVec);
			}
		}
		if (result == null) {
			return null;
		}

		return result;
	}

	public static Vector3d getVectorFromYawPitch(float yaw, float pitch) {
		float f = MathHelper.cos(-yaw * 0.017453292F - (float) Math.PI);
		float f1 = MathHelper.sin(-yaw * 0.017453292F - (float) Math.PI);
		float f2 = -MathHelper.cos(-pitch * 0.017453292F);
		float f3 = MathHelper.sin(-pitch * 0.017453292F);
		return new Vector3d(f1 * f2, f3, f * f2).normalize();
	}

	public boolean canEntityBeSeen(Entity entity) {
		return level.clip(new RayTraceContext(
				new Vector3d(worldPosition.getX() + 0.5, worldPosition.getY() + 1.1, worldPosition.getZ() + 0.5),
				new Vector3d(entity.getX(), entity.getY() + (double) entity.getEyeHeight(), entity.getZ()),
				RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, null)) == null;
	}

	public float getAngle(Direction facing) {
		switch (facing) {
		default:
		case NORTH: {
			return 0;
		}
		case SOUTH: {
			return 180;
		}

		case WEST: {
			return 270;
		}

		case EAST: {
			return 90;
		}

		}

	}

	public static class AITurretBase {
		TileEntityTurret te = null;

		public AITurretBase(TileEntityTurret te) {
			this.te = te;
		}

		public void updateAITask() {

		}

		public double distanceSqrtTo(Entity entity) {
			double d0 = te.getBlockPos().getX() - entity.getX();
			double d1 = te.getBlockPos().getY() - entity.getY();
			double d2 = te.getBlockPos().getZ() - entity.getZ();
			return (d0 * d0 + d1 * d1 + d2 * d2);
		}
	}

	public class AITurretTarget extends AITurretBase {
		public AITurretTarget(TileEntityTurret te) {
			super(te);
		}

		public void updateAITask() {

			Entity target = te.target;
			if (target != null) {
				if (distanceSqrtTo(target) > 256) {
					te.target = null;
				}
				if (Math.abs(target.getY() - te.getBlockPos().getY()) > 1) {
					te.target = null;
				}

			}
		}

		public void setTarget(Entity seenEntity) {
			if (seenEntity == te.target) {
				return;
			}
			if (seenEntity instanceof PlayerEntity
					&& (((PlayerEntity) seenEntity).isCreative() || seenEntity.isSpectator())) {
				return;
			}
			EntityType<?> entitytype = seenEntity.getType();
			ResourceLocation key = EntityType.getKey(entitytype);
			if (key != null && te.whitelistedTypes.contains(key.toString()))
				return;

			if (te.target == null || distanceSqrtTo(seenEntity) < distanceSqrtTo(te.target)) {
				te.target = seenEntity;
			}
		}
	}

	public class AITurretShoot extends AITurretBase {
		public AITurretShoot(TileEntityTurret te) {
			super(te);
		}

		public void updateAITask() {

			Entity target = te.target;
			BlockPos pos = te.getBlockPos();
			if (target != null && te.hasAmmo()) {

				double dX = worldPosition.getX() - target.getX();
				double dY = worldPosition.getY() - target.getY();
				double dZ = worldPosition.getZ() - target.getZ();

				double yaw = Math.atan2(dZ, dX);
				double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;

				Vector3d position = getHeadPosition(0.25);
				Vector3d rotation = getHeadRotation();
				Vector3d entityPosition = new Vector3d(target.getX(), target.getY(), target.getZ());

				Vector3d neededRotation = entityPosition.subtract(position);
				float currentYaw = TileEntityTurret.getYaw(rotation);
				float neededYaw = TileEntityTurret.getYaw(neededRotation);

				float difference = currentYaw - neededYaw;
				float absDiff = Math.abs(difference);

				if (absDiff <= 16) {
					shoot();
				}

			}
		}

		public void shoot() {
			if (te.hasAmmo() && te.delay == te.maxDelay) {
				float currentYaw = TileEntityTurret.getYaw(getHeadRotation());
				shoot(getHeadPosition(0.25), currentYaw + 90, 0f);
				te.consumeAmmo(1);
				te.delay = 0;
			}
		}

		public void shoot(Vector3d pos, float yaw, float pitch) {
			ShotEntity shot = new ShotEntity(pos.x,pos.y,pos.z,level);
			shot.setDamage(1);
			shot.setTurret(true);
			shot.shootFromRotation(0, yaw, 0.0F, 2f,4f);
			/*EntityShot shot = new EntityShot(te.level, pos, yaw, pitch, 1.5f, 0.25f);
			shot.setDamage(20f);
*/
			if (!level.isClientSide()) {
				level.addFreshEntity(shot);
			}
			level.playSound(null, te.worldPosition, ModSounds.AK47_SHOT.get(), SoundCategory.BLOCKS, 0.25f,
					1.0F / (te.level.random.nextFloat() * 0.4F + 1.2F) + 1f * 0.5F);
			Chunk chunk = level.getChunkAt(worldPosition);
			Vector3d pos2 = getHeadPosition(1.2);
			if(chunk != null) {
				PacketManager.sendToChunk(PacketManager.turretShot, new TurretShotMessage(pos2.x, pos2.y+0.3, pos2.z), () -> chunk);
			}

		}
	}

	public boolean hasAmmo() {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.SEVEN_MM_BULLET.get())
				return true;
		}
		return false;
	}

	public void consumeAmmo(int amount) {
		for (int i = 0; i < 9; i++) {
			ItemStack stack = this.getInventory().getStackInSlot(i);
			if (!stack.isEmpty() && stack.getItem() == ModItems.SEVEN_MM_BULLET.get()) {
				if (stack.getCount() >= amount) {
					stack.shrink(amount);
					if (stack.getCount() <= 0) {
						this.getInventory().setStackInSlot(i, ItemStack.EMPTY);
					}
					break;
				} else {
					amount -= stack.getCount();
					this.getInventory().setStackInSlot(i, ItemStack.EMPTY);
				}
			}
		}
	}

	public void setDisplayName(String displayName) {
	}

	@Override
	@Nullable
	public ITextComponent getDisplayName() {
		return DEFAULT_NAME;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<BlockPos> getConnections() {
		return (List<BlockPos>) network.clone();
	}

	@Override
	public void connectTo(INetwork toConnect) {
		if (!isConnectedTo(toConnect)) {
			network.add(toConnect.getPosition());
			toConnect.connectTo(this);
			setChanged();
		}
	}

	@Override
	public void disconnect(INetwork toDisconnect) {
		if (isConnectedTo(toDisconnect)) {
			network.remove(toDisconnect.getPosition());
			toDisconnect.disconnect(this);
			setChanged();
		}
	}

	@Override
	public boolean isConnectedTo(INetwork net) {
		return network.contains(net.getPosition());
	}

	@Override
	public void disconnectAll() {
		for (BlockPos pos : getConnections()) {
			TileEntity te = level.getBlockEntity(pos);
			if (te instanceof INetwork) {
				((INetwork) te).disconnect(this);
			}
		}
	}

	@Override
	public BlockPos getPosition() {
		return this.getBlockPos();
	}

	public void setOn(boolean on) {
		this.on = on;
		markForUpdate();
	}

	public void markForUpdate() {
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 3);
		setChanged();
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket() {
		CompoundNBT nbtTag = new CompoundNBT();
		this.save(nbtTag);
		return new SUpdateTileEntityPacket(this.worldPosition, 0, nbtTag);
	}

	@Override
	public CompoundNBT getUpdateTag() {
		return save(new CompoundNBT());
	}

	@Override
	public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
		CompoundNBT tag = pkt.getTag();
		load(level.getBlockState(pkt.getPos()), tag);
		if (hasLevel()) {
			level.sendBlockUpdated(pkt.getPos(), level.getBlockState(this.worldPosition),
					level.getBlockState(pkt.getPos()), 2);
		}
	}

	public boolean isOn() {
		return on;
	}

	public boolean switchOn() {
		setOn(!isOn());
		return on;
	}

	@Override
	public void sendPacket(String packet, INetwork from, PlayerEntity playerFrom) {
		switch (packet) {
		case "switch":
			switchOn();
			break;
		case "shoot":
			this.shootAI.shoot();
			break;
		case "reset":
			this.whitelistedTypes.clear();
			setChanged();
			break;
		}
	}

}
