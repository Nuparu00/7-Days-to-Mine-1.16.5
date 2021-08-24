package nuparu.sevendaystomine.client.gui.monitor.elements;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MonitorAnimation {
	private final ResourceLocation[] frames;

	public MonitorAnimation(ResourceLocation[] frames) {
		this.frames = frames;
	}

	public ResourceLocation getFrame(int index) {
		while (index > frames.length - 1) {
			index -= frames.length;
		}

		return frames[index];
	}

	public int getFramesCount() {
		return frames.length;
	}
}
