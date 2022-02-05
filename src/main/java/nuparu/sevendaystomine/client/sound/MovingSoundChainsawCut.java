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
import nuparu.sevendaystomine.events.ClientEventHandler;
import nuparu.sevendaystomine.events.PlayerEventHandler;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.init.ModSounds;
import nuparu.sevendaystomine.item.ItemAuger;
import nuparu.sevendaystomine.item.ItemChainsaw;

@OnlyIn(Dist.CLIENT)
public class MovingSoundChainsawCut extends TickableSound {
	private final PlayerEntity player;

	public MovingSoundChainsawCut(PlayerEntity player) {
		super(ModSounds.CHAINSAW_CUT.get(), SoundCategory.NEUTRAL);
		this.player = player;
		this.looping = false;
		this.delay = 0;
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	public void tick() {
		ItemStack stack = this.player.getItemInHand(Hand.MAIN_HAND);
		CompoundNBT nbt = stack.getTag();
		if (!this.player.isAlive() || System.currentTimeMillis()- ClientEventHandler.getLastTimeHittingBlock() > 500 || stack.isEmpty() || (!(stack.getItem() instanceof ItemChainsaw) && !(stack.getItem() instanceof ItemAuger))) {
			this.stop();
			ClientEventHandler.nextChainsawCutSound = System.currentTimeMillis();
		}
		if (nbt == null || !nbt.contains("FuelCurrent",Constants.NBT.TAG_INT) || nbt.getInt("FuelCurrent") == 0) {
			this.stop();
			ClientEventHandler.nextChainsawCutSound = System.currentTimeMillis();
			return;
		}

		else {
			this.x = (float) this.player.getX();
			this.y = (float) this.player.getY();
			this.z = (float) this.player.getZ();
			this.volume = 1.0F;
		}
	}
}