package nuparu.sevendaystomine.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.init.ModTileEntities;

public class TileEntityRadio extends TileEntity implements ITickableTileEntity {

	public static final List<Channel> CHANNELS = new ArrayList<Channel>();
	public static final Channel DEFAULT_CHANNEL = new Channel("none", Integer.MAX_VALUE, null);

	static {
		CHANNELS.add(DEFAULT_CHANNEL);
		CHANNELS.add(new Channel("white_noise", 240, ModSounds.WHITE_NOISE.get()));
		CHANNELS.add(new Channel("eas", 1400, ModSounds.EAS.get()));
	}

	private int time = Integer.MAX_VALUE;
	private Channel channel = DEFAULT_CHANNEL;

	public TileEntityRadio() {
		super(ModTileEntities.RADIO.get());
	}

	@Override
	public void tick() {
		if (this.channel != DEFAULT_CHANNEL && this.channel != null) {
			if (this.time > channel.duration) {
				this.time = channel.duration;
			} else if (this.time <= 0) {
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(level,channel.sound, 1.2f, worldPosition, SoundCategory.RECORDS);
			}
			--this.time;

			markNoise();
		} else {
			this.time = Integer.MAX_VALUE;
		}
	}

	public void setChannel(String name) {
		channel = Channel.getByName(name);
		this.time = channel.duration;
		if (channel == DEFAULT_CHANNEL || channel == null) {
			this.time = Integer.MAX_VALUE;
			SevenDaysToMine.proxy.stopLoudSound(worldPosition);
			return;
		}
		SevenDaysToMine.proxy.playLoudSound(level,channel.sound, 1.2f, worldPosition, SoundCategory.RECORDS);
	}

	@Override
	public void load(BlockState state, CompoundNBT compound) {
		super.load(state, compound);
		this.time = compound.getInt("time");
		this.channel = Channel.getByName(compound.getString("channel"));

	}

	@Override
	public CompoundNBT save(CompoundNBT compound) {
		super.save(compound);
		compound.putInt("time", this.time);
		compound.putString("channel", channel == null ? DEFAULT_CHANNEL.name : this.channel.name);
		return compound;
	}

	public static class Channel {
		String name;
		SoundEvent sound;
		int duration;

		public Channel(String name, int duration, SoundEvent sound) {
			this.name = name;
			this.duration = duration;
			this.sound = sound;
		}

		public static Channel getByName(String name) {
			for (Channel ch : CHANNELS) {
				if (ch.name.equals(name)) {
					return ch;
				}
			}
			return null;
		}
	}

	public void markNoise() {

	}

	public void cycleRadio() {
		for (int i = 0; i < CHANNELS.size(); i++) {
			if (CHANNELS.get(i) == channel && i < CHANNELS.size() - 1) {
				channel = CHANNELS.get(i + 1);
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(level,channel.sound, 1.2f, worldPosition, SoundCategory.RECORDS);
				return;
			}
		}
		channel = DEFAULT_CHANNEL;
		this.time = Integer.MAX_VALUE;
		SevenDaysToMine.proxy.stopLoudSound(worldPosition);
	}

}
