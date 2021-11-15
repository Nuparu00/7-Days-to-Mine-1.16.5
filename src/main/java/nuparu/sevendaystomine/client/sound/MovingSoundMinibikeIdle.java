package nuparu.sevendaystomine.client.sound;

import net.minecraft.client.audio.TickableSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import nuparu.sevendaystomine.entity.MinibikeEntity;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.util.MathUtils;

@OnlyIn(Dist.CLIENT)
public class MovingSoundMinibikeIdle extends TickableSound {
	private final MinibikeEntity minibike;

	public MovingSoundMinibikeIdle(MinibikeEntity minibike) {
		super(ModSounds.MINIBIKE_IDLE.get(), SoundCategory.NEUTRAL);
		this.minibike = minibike;
		this.looping = false;
		this.delay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void tick() {
		if (minibike.getControllingPassenger() == null || !minibike.canBeDriven()) {
			this.stop();
			return;
		}
		this.pitch = (float)(1+ MathUtils.getSpeedKilometersPerHour(minibike)/100d);
		this.x = (float) this.minibike.getX();
		this.y = (float) this.minibike.getY();
		this.z = (float) this.minibike.getZ();
		this.volume = 2.0F;
	}
}