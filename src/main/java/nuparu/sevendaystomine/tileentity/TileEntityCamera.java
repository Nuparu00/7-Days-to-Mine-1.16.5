package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.electricity.network.INetwork;
import nuparu.sevendaystomine.entity.EntityCameraView;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityCamera extends TileEntity implements ITickableTileEntity, INetwork {

	private ArrayList<BlockPos> network = new ArrayList<BlockPos>();
	private ArrayList<PlayerEntity> viewers = new ArrayList<PlayerEntity>();
	private String customName;
	private boolean on = true;
	private boolean rotating = false;

	private EntityCameraView cameraView;

	public TileEntityCamera() {
		super(ModTileEntities.CAMERA.get());
	}

	@Override
	public void tick() {
		/*if (cameraView == null && ModConfig.players.allowCameras && level != null) {
			cameraView = new EntityCameraView(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), this);
			if (!level.isClientSide()) {
				level.addFreshEntity(cameraView);
			}
		}*/
	}

	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	public String getCustomName() {
		return this.customName;
	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		if (hasCustomName()) {
			compound.putString("custom_name", customName);
		}
		ListNBT list = new ListNBT();
		for (BlockPos net : getConnections()) {
			list.add(LongNBT.valueOf(net.asLong()));
		}
		compound.put("network", list);
		compound.putBoolean("on", on);
		compound.putBoolean("rotating", rotating);

		return compound;
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		if (compound.contains("custom_name", 8)) {
			customName = compound.getString("custom_name");
		}
		if (level != null) {
			network.clear();
			ListNBT list = compound.getList("network", Constants.NBT.TAG_LONG);
			for (int i = 0; i < list.size(); ++i) {
				LongNBT nbt = (LongNBT) list.get(i);
				BlockPos blockPos = BlockPos.of(nbt.getAsLong());
				network.add(blockPos);
			}
		}
		this.on = compound.getBoolean("on");
		this.rotating = compound.getBoolean("rotating");
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

	public void closeView(PlayerEntity player) {
		viewers.remove(player);
	}

	public float getHeadRotation() {
		return (float) (90f * Math.sin(level.getGameTime() / 450d));
	}

	public float getHeadRotationPrev() {
		return (float) (90f * Math.sin(level.getGameTime() / 450d));
	}

	public EntityCameraView getCameraView(PlayerEntity player) {
		if (on && cameraView != null && CommonConfig.allowCameras.get()) {
			viewers.add(player);
			cameraView.yRotO = cameraView.yRot;
			cameraView.yRot = cameraView.initRotation + getHeadRotation() * cameraView.direction;
			return cameraView;
		}
		return null;
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
		return this.worldPosition;
	}
	
	public void setOn(boolean on) {
		this.on = on;
		level.sendBlockUpdated(worldPosition, level.getBlockState(this.worldPosition),
				level.getBlockState(worldPosition), 2);
		setChanged();
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
		switch(packet) {
		case "switch" : switchOn(); break;
		}
	}

}
