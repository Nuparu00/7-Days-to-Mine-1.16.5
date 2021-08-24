package nuparu.sevendaystomine.tileentity;

public class TileEntitySirene { /*extends TileEntity implements ITickable {

	public static final List<Channel> CHANNELS = new ArrayList<Channel>();
	public static final Channel DEFAULT_CHANNEL = new Channel("none", Integer.MAX_VALUE, null);

	static {
		CHANNELS.add(DEFAULT_CHANNEL);
		CHANNELS.add(new Channel("sirene", 1400, ModSounds.WHITE_NOISE));
		CHANNELS.add(new Channel("white_noise", 240, ModSounds.SIRENE));
	}

	private int time = Integer.MAX_VALUE;
	private Channel channel = DEFAULT_CHANNEL;

	public TileEntitySirene() {

	}

	@Override
	public void update() {
		if (this.channel != DEFAULT_CHANNEL && this.channel != null) {
			if (this.time > channel.duration) {
				this.time = channel.duration;
			} else if (this.time <= 0) {
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 10f, pos, SoundCategory.RECORDS);
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
			SevenDaysToMine.proxy.stopLoudSound(pos);
			return;
		}
		SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 10f, pos, SoundCategory.RECORDS);
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
		compound.setString("channel", this.channel.name);
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

	public void cycle() {
		for (int i = 0; i < CHANNELS.size(); i++) {
			if (CHANNELS.get(i) == channel && i < CHANNELS.size() - 1) {
				channel = CHANNELS.get(i + 1);
				this.time = channel.duration;
				SevenDaysToMine.proxy.playLoudSound(world,channel.sound, 10f, pos, SoundCategory.RECORDS);
				return;
			}
		}
		channel = DEFAULT_CHANNEL;
		this.time = Integer.MAX_VALUE;
		SevenDaysToMine.proxy.stopLoudSound(pos);
	}
*/
}
