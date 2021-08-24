package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.block.BlockAirplaneRotor;
import nuparu.sevendaystomine.electricity.ElectricConnection;
import nuparu.sevendaystomine.electricity.EnumDeviceType;
import nuparu.sevendaystomine.electricity.IVoltage;
import nuparu.sevendaystomine.init.ModTileEntities;
import nuparu.sevendaystomine.util.DamageSources;
import nuparu.sevendaystomine.util.MathUtils;
import nuparu.sevendaystomine.util.ModConstants;

public class TileEntityAirplaneRotor extends TileEntity implements ITickableTileEntity, IVoltage {
	private List<ElectricConnection> inputs = new ArrayList<ElectricConnection>();
	private List<ElectricConnection> outputs = new ArrayList<ElectricConnection>();
	private long capacity = 1000l;
	private long voltage = 0;

	public float speed;
	public double voltPre;
	public boolean on;
	public float range = 8f;
	public float thrust = 0f;
	public float angle;
	public float anglePrev;
	private long nextSound = (long) 0;
	private AxisAlignedBB windBB;

	Direction facing;

	public TileEntityAirplaneRotor() {
		super(ModTileEntities.AIRPLANE_ROTOR.get());
	}

	@Override
	public void tick() {

		boolean dirty = false;

		anglePrev = angle;

		if (facing == null) {
			this.facing = level.getBlockState(worldPosition).getValue(BlockAirplaneRotor.FACING).getOpposite();
			dirty = true;
		}

		if (thrust < 0f) {
			thrust = 0f;
			dirty = true;
		}

		if (on) {
			if (voltage >= getRequiredPower()) {
				float thrustOld = thrust;
				if (thrust < 1f * (voltage / getRequiredPower())) {
					thrust += 0.01f;
				} else {
					thrust = 1f * (voltage / getRequiredPower());
				}
				if (Math.abs(thrust - thrustOld) >= 0.001) {
					dirty = true;
				}
				voltage -= getRequiredPower();
			}
		}
		if (!on || voltage < getRequiredPower()) {
			if (thrust > 0.004) {
				thrust -= 0.1f;
				dirty = true;
			} else {
				thrust = 0;
				dirty = true;
			}

		}

		if (thrust != 0) {

			final double maxForce = thrust * 0.004;
			range = (float) Math.PI * thrust * 0.4f;
			speed = (float) (3.6 * (thrust));
			angle += 45;
			dirty = true;

			List<Entity> entities = level.getEntitiesOfClass(Entity.class, getEntitySearchBox());

			if (!entities.isEmpty()) {

				double angle = Math.toRadians(getBlowAngle() - 90);
				final Vector3d blockPos = MathUtils.getConeApex(worldPosition, angle);
				final Vector3d basePos = MathUtils.getConeBaseCenter(worldPosition, angle, range);
				final Vector3d coneAxis = new Vector3d(basePos.x - blockPos.x, basePos.y - blockPos.y,
						basePos.z - blockPos.z);

				for (Entity entity : entities) {

					if (!(entity instanceof PlayerEntity) || !((PlayerEntity) entity).isFallFlying()) {

						Vector3d directionVec = new Vector3d(entity.getX() - blockPos.x, entity.getY() - blockPos.y,
								entity.getZ() - blockPos.z);

						if (MathUtils.isInCone(coneAxis, directionVec, 0.6)) {
							final double distToOrigin = directionVec.length();
							final double force = (1.0 - distToOrigin / range) * maxForce;
							if (force <= 0)
								continue;
							Vector3d normal = directionVec.normalize();
							Vector3d motion = entity.getDeltaMovement();

							entity.setDeltaMovement(entity.getDeltaMovement().add(motion.x + force * normal.x, 0,
									motion.z + force * normal.z));
						}
					}
				}
				BlockPos front = worldPosition.relative(facing);
				List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class,
						new AxisAlignedBB((double) front.getX(), (double) front.getY(), (double) front.getZ(),
								(double) (front.getX() + 1), (double) (front.getY() + 1), (double) (front.getZ() + 1)));
				for (int i = 0; i < list.size(); i++) {
					LivingEntity entity = list.get(i);

					entity.hurt(DamageSources.blade, 3);

				}
			}

		}
		if (dirty) {
			markForUpdate();
		}

	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);

		this.anglePrev = compound.getFloat("anglePrev");
		this.angle = compound.getFloat("angle");
		this.speed = compound.getFloat("speed");
		this.range = compound.getFloat("range");
		this.thrust = compound.getFloat("thrust");
		this.on = compound.getBoolean("on");
		if (compound.contains("facing", 3)) {
			this.facing = Direction.values()[(compound.getInt("facing"))];
		}

		this.voltage = compound.getLong("power");
		if (compound.contains("inputs", Constants.NBT.TAG_COMPOUND)) {
			inputs.clear();
			CompoundNBT in = compound.getCompound("inputs");
			int size = in.getInt("size");
			for (int i = 0; i < size; i++) {
				CompoundNBT nbt = in.getCompound("input_" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				inputs.add(connection);
			}
		}

		if (compound.contains("outputs", Constants.NBT.TAG_COMPOUND)) {
			outputs.clear();
			CompoundNBT in = compound.getCompound("outputs");
			int size = in.getInt("size");
			for (int i = 0; i < size; i++) {
				CompoundNBT nbt = in.getCompound("output" + i);
				ElectricConnection connection = new ElectricConnection();
				connection.readNBT(nbt);
				outputs.add(connection);
			}
		}

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);

		compound.putFloat("anglePrev", this.anglePrev);
		compound.putFloat("angle", this.angle);
		compound.putFloat("speed", this.speed);
		compound.putFloat("range", this.range);
		compound.putFloat("thrust", this.thrust);
		compound.putBoolean("on", this.on);
		if (facing != null) {
			compound.putInt("facing", this.facing.ordinal());
		}

		compound.putLong("power", voltage);

		CompoundNBT in = new CompoundNBT();

		in.putInt("size", getInputs().size());
		for (int i = 0; i < inputs.size(); i++) {
			ElectricConnection connection = inputs.get(i);
			in.put("input_" + i, connection.writeNBT(new CompoundNBT()));
		}

		CompoundNBT out = new CompoundNBT();

		out.putInt("size", getOutputs().size());
		for (int i = 0; i < outputs.size(); i++) {
			ElectricConnection connection = outputs.get(i);
			out.put("output" + i, connection.writeNBT(new CompoundNBT()));
		}

		compound.put("inputs", in);
		compound.put("outputs", out);

		return compound;
	}

	public void switchState() {
		this.on = !this.on;
		this.setChanged();
	}

	private AxisAlignedBB getEntitySearchBox() {
		windBB = null;
		if (windBB != null) {
			return windBB;
		}
		BlockPos posStart = worldPosition.relative(facing).relative(facing.getCounterClockWise(), 1).below(1);
		BlockPos posEnd = worldPosition.relative(facing, (int) range).relative(facing.getClockWise(), 1).above(1);
		windBB = new AxisAlignedBB(posStart.getX(), posStart.getY(), posStart.getZ(), posEnd.getX() + 1,
				posEnd.getY() + 1, posEnd.getZ() + 1);

		return windBB;
	}

	public double getBlowAngle() {
		switch (facing) {
		case NORTH:
			return 0d;
		case WEST:
			return 270d;
		case SOUTH:
			return 180d;
		case EAST:
			return 90d;
		default:
			return 0d;

		}

	}

	@Override
	public EnumDeviceType getDeviceType() {
		return EnumDeviceType.CONSUMER;
	}

	@Override
	public int getMaximalInputs() {
		return 4;
	}

	@Override
	public int getMaximalOutputs() {
		return 0;
	}

	@Override
	public List<ElectricConnection> getInputs() {
		return inputs;
	}

	@Override
	public List<ElectricConnection> getOutputs() {
		return outputs;
	}

	@Override
	public long getOutput() {
		return 0;
	}

	@Override
	public long getMaxOutput() {
		return 0;
	}

	@Override
	public long getOutputForConnection(ElectricConnection connection) {
		return 0;
	}

	@Override
	public boolean tryToConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(worldPosition)) {
			type = 0;
		} else if (connection.getTo().equals(worldPosition)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			this.outputs.add(connection);
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			this.inputs.add(connection);
			return true;
		}

		return false;
	}

	@Override
	public boolean canConnect(ElectricConnection connection) {
		short type = -1;
		if (connection.getFrom().equals(worldPosition)) {
			type = 0;
		} else if (connection.getTo().equals(worldPosition)) {
			type = 1;
		}

		if (type == 0 && getOutputs().size() < getMaximalOutputs()) {
			return true;
		}

		if (type == 1 && getInputs().size() < getMaximalInputs()) {
			return true;
		}

		return false;
	}

	@Override
	public long getRequiredPower() {
		return 60;
	}

	@Override
	public long getCapacity() {
		return capacity;
	}

	@Override
	public long getVoltageStored() {
		return voltage;
	}

	@Override
	public void storePower(long power) {
		this.voltage += power;
		if (this.voltage > this.getCapacity()) {
			this.voltage = this.getCapacity();
		}
		if (this.voltage < 0) {
			this.voltage = 0;
		}
	}

	@Override
	public long tryToSendPower(long power, ElectricConnection connection) {
		long canBeAdded = capacity - voltage;
		long delta = Math.min(canBeAdded, power);
		long lost = 0;
		if (connection != null) {
			lost = (long) Math.round(delta * ModConstants.DROP_PER_BLOCK * connection.getDistance());
		}
		long realDelta = delta - lost;
		this.voltage += realDelta;

		return delta;
	}

	private static final Vector3d offset = new Vector3d(0.5, 0.5, 0.5);

	@Override
	public Vector3d getWireOffset() {
		return offset;
	}

	@Override
	public boolean isPassive() {
		return false;
	}

	@Override
	public boolean disconnect(IVoltage voltage) {
		for (ElectricConnection input : getInputs()) {
			if (input.getFrom().equals(voltage.getPos())) {
				this.inputs.remove(input);
				markForUpdate();
				return true;
			}
		}

		for (ElectricConnection output : getOutputs()) {
			if (output.getTo().equals(voltage.getPos())) {

				this.outputs.remove(output);
				markForUpdate();
				return true;
			}
		}
		return false;
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
		CompoundNBT nbt = save(new CompoundNBT());
		return nbt;
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

	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {
		long toAdd = Math.min(this.capacity - this.voltage, maxReceive);
		if (!simulate) {
			this.voltage += toAdd;
		}
		return (int) toAdd;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		long toExtract = Math.min(this.voltage, maxExtract);
		if (!simulate) {
			this.voltage -= toExtract;
		}
		return (int) toExtract;
	}

	@Override
	public int getEnergyStored() {
		return (int) this.voltage;
	}

	@Override
	public int getMaxEnergyStored() {
		return (int) this.capacity;
	}

	@Override
	public boolean canExtract() {
		return this.capacity > 0;
	}

	@Override
	public boolean canReceive() {
		return this.voltage < this.capacity;
	}

	@Override
	public BlockPos getPos() {
		return worldPosition;
	}

}
