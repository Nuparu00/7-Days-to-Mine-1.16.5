package nuparu.sevendaystomine.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.client.util.ResourcesHelper;
import nuparu.sevendaystomine.client.util.ResourcesHelper.Image;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoRequestMessage;

@OnlyIn(Dist.CLIENT)
public class GuiPhoto extends Screen {

	private String path;
	private Image image = null;

	private long nextUpdate = 0;

	public GuiPhoto(String path) {
		super(new StringTextComponent("screen.photo"));
		this.path = path;
		System.out.println("CONSTRUCTOR + " + path);
		image = ResourcesHelper.INSTANCE.getImage(path);
		if (image == null) {
			PacketManager.photoRequest.sendToServer(new PhotoRequestMessage(path));
			image = ResourcesHelper.INSTANCE.getImage(path);
		}
	}

	@Override
	public void tick() {
		if (image == null) {
			if (System.currentTimeMillis() >= nextUpdate) {
				System.out.println("PATH " + path);
				image = ResourcesHelper.INSTANCE.tryToGetImage(path);
				nextUpdate = System.currentTimeMillis() + 1000;
			}
		}
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		super.render(matrix, mouseX, mouseY, partialTicks);
		if (image != null) {
			// -1 == (width > height) ; 0 == (width == height) ; 1 == (width < height)
			int shape = Integer.compare(image.height, image.width);

			int w = width;
			int h = height;

			if (shape == -1) {
				w = (int) Math.floor(w * 0.75f);
				h = (int) Math.floor(((float) image.height / (float) image.width) * w);
			} else if (shape == 0) {
				h = (int) Math.floor(h * 0.75f);
				w = h;
			} else if (shape == 1) {
				h = (int) Math.floor(h * 0.75f);
				w = (int) Math.floor(((float) image.width / (float) image.height) * h);
			}
			if (image.res != null) {
				Minecraft.getInstance().getTextureManager().bind(image.res);
				AbstractGui.blit(matrix, (width/2)-(w/2), (height/2)-(h/2),w, h,0,0, w,h,w,h);
				//RenderUtils.drawTexturedRect(image.res, (width / 2) - (w / 2), (height / 2) - (h / 2), w, h, w, h, w, h, 1, 1);
			}
			else {
				System.out.println("RES IS NULL");
			}
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

}
