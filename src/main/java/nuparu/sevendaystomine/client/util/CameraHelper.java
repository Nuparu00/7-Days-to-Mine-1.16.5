package nuparu.sevendaystomine.client.util;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.config.CommonConfig;
import nuparu.sevendaystomine.init.ModItems;
import nuparu.sevendaystomine.item.ItemAnalogCamera;
import nuparu.sevendaystomine.network.PacketManager;
import nuparu.sevendaystomine.network.packets.PhotoToServerMessage;
import nuparu.sevendaystomine.util.Utils;

@OnlyIn(Dist.CLIENT)
public class CameraHelper {
	private IntBuffer pixelBuffer;
	private int[] pixelValues;
	public static final CameraHelper INSTANCE = new CameraHelper();

	public void saveScreenshot(int width, int height, Framebuffer buffer, PlayerEntity playerIn) {
		if (CommonConfig.allowPhotos.get()) {
			saveScreenshot((String) null, width, height, buffer);
		}
	}

	public void saveScreenshot(String screenshotName, int width, int height, Framebuffer buffer) {
		Minecraft mc = Minecraft.getInstance();
		PlayerEntity player = mc.player;
		if (player == null)
			return;
		ItemStack stack = player.getItemInHand(Hand.MAIN_HAND);
		if (!stack.isEmpty() && stack.getItem() == ModItems.ANALOG_CAMERA.get()) {
			double dW = 1 - ItemAnalogCamera.getWidth(stack, player);
			double dH = 1 - ItemAnalogCamera.getHeight(stack, player);

			saveScreenshot(screenshotName, (int) (width * dW / 2d), (int) (height * dH / 2d),
					(int) (width - width * dW / 2d), (int) (height - height * dH / 2d), width, height, buffer);
		}
	}

	public void saveScreenshot(String screenshotName, int x, int y, int xx, int yy, int screenWidth, int screenHeight,
			Framebuffer buffer) {

		int width = xx - x;
		int height = yy - y;
		try {
			screenWidth = buffer.width;
			screenHeight = buffer.height;
			int i = screenWidth * screenHeight;

			NativeImage nativeimage = new NativeImage(screenWidth, screenHeight, false);

			RenderSystem.bindTexture(buffer.getColorTextureId());
			nativeimage.downloadTexture(0, true);
			nativeimage.flipY();

			NativeImage res = new NativeImage(width, height, false);
			nativeimage.resizeSubRectTo(x,y,width,height,res);

			sendFile(res);

		} catch (Exception exception) {
			exception.printStackTrace();

		}

	}

	public void sendFile(NativeImage img) throws IOException {
		File tempFile = File.createTempFile("sdtm-", "-tmpimg");
		img.writeToFile(tempFile);
		tempFile.deleteOnExit();
		
		byte[] bytes = FileUtils.readFileToByteArray(tempFile);
		List<byte[]> chunks = Utils.divideArray(bytes, 30000);
		int parts = chunks.size();
		String ID = UUID.randomUUID().toString();
		for (int i = 0; i < parts; i++) {
			PacketManager.photoToServer.sendToServer(new PhotoToServerMessage(chunks.get(i), i, parts, ID));
		}
	}

}
