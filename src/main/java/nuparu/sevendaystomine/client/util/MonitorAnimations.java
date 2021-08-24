package nuparu.sevendaystomine.client.util;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.elements.MonitorAnimation;

@OnlyIn(Dist.CLIENT)
public class MonitorAnimations {
	public static final MonitorAnimation WIN10_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win10/frame_9.png") });

	public static final MonitorAnimation WIN7_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win7/frame_1.png") });
	
	public static final MonitorAnimation WINXP_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_9.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_10.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_11.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/winxp/frame_12.png")});
	
	public static final MonitorAnimation WIN8_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/win8/frame_0.png")});
	
	public static final MonitorAnimation MAC_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_0.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_1.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_2.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_3.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_4.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_5.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_6.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_7.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_8.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_9.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_10.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_11.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_12.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_13.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_14.png"),
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/mac/frame_15.png")});
	
	public static final MonitorAnimation LINUX_LOADING = new MonitorAnimation(new ResourceLocation[] {
			new ResourceLocation(SevenDaysToMine.MODID, "textures/animations/loading/linux/frame_0.png")});
}
