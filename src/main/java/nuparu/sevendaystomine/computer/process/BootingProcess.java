package nuparu.sevendaystomine.computer.process;

import nuparu.sevendaystomine.client.util.MonitorAnimations;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.ResourceLocation;
import nuparu.sevendaystomine.client.gui.monitor.elements.MonitorAnimation;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.tileentity.TileEntityComputer;

public class BootingProcess extends TickingProcess {

	private static final long BOOT_TIME = 300;

	public BootingProcess() {
		super();
	}

	@Override
	public void tick() {
		super.tick();
		if (existedFor >= getBootTime()) {
			computerTE.onBootFinished();
			computerTE.killProcess(this);
		}
		computerTE.setChanged();
	}

	@Override
	public void render(MatrixStack matrix, float partialTicks) {

		MonitorAnimation anim = getLoadingAnimation(computerTE);
		if (anim == null)
			return;
		ResourceLocation res = anim.getFrame((int) Math.round(getFrame()));
		if (res != null) {
			RenderSystem.pushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
		    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			RenderSystem.translated(0, 0, 2);
			RenderSystem.clearColor(1, 1, 1, 1);
			RenderUtils.drawTexturedRect(matrix,res, screen.getRelativeX(0.5) - 32, screen.getRelativeY(0.5) - 32, 0, 0, 64,
					64, 64, 64, 1, 2);
			RenderSystem.translated(0, 0, -2);
			GL11.glDisable(GL11.GL_BLEND);
			RenderSystem.popMatrix();
		}
	}

	public static MonitorAnimation getLoadingAnimation(TileEntityComputer computerTE) {
		switch (computerTE.getSystem()) {
		default:
		case NONE:
			return null;
		case WIN10:
			return MonitorAnimations.WIN10_LOADING;
		case WIN7:
			return MonitorAnimations.WIN7_LOADING;
		case WIN8:
			return MonitorAnimations.WIN8_LOADING;
		case WINXP:
			return MonitorAnimations.WINXP_LOADING;
		case MAC:
			return MonitorAnimations.MAC_LOADING;
		case LINUX:
			return MonitorAnimations.LINUX_LOADING;
		}
	}
	
	public long getBootTime() {
		return this.computerTE.getSystem() == TileEntityComputer.EnumSystem.MAC ? 320 : 300;
	}
	
	public float getFrame() {
		return existedFor / (this.computerTE.getSystem() == TileEntityComputer.EnumSystem.MAC ? 20 : 10);
	}

}
