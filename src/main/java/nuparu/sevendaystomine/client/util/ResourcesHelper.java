package nuparu.sevendaystomine.client.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Maps;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ResourcesHelper {
	private HashMap<String, File> files = Maps.newHashMap();
	private HashMap<String, Image> photos = Maps.newHashMap();

	public static final ResourcesHelper INSTANCE = new ResourcesHelper();

	public Image getImage(String name) {
		Iterator<Entry<String, Image>> itr = photos.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Image> entry = itr.next();
			if (name.equals(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	public void putImage(Image img, String name) {
		photos.put(name, img);
	}

	public Image addResourceFromFile(File file, String path) throws IOException {
		BufferedImage bimg = ImageIO.read(file);
		System.out.println(file.getAbsolutePath());
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write( bimg, "png", baos );
		baos.flush();
		baos.close();

		/*NativeImage n = new NativeImage(bimg.getWidth(),bimg.getHeight(),false);
		
		for(int x = 0; x < bimg.getWidth(); x++) {
			for(int y = 0; y < bimg.getHeight(); y++) {
				int color = bimg.getRGB(x, y);
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;
				
				int argb = new Color(red, green, blue, 1).getRGB();
				n.setPixelRGBA(x, y, argb);
			}
		}*/
		NativeImage n = NativeImage.read(new FileInputStream(file));

		
		System.out.println("DOES FILE EXIST " + file.exists() + " " + file.getAbsolutePath());
		
		//NativeImage
		try {
		DynamicTexture tex = new DynamicTexture(n);
		ResourceLocation res = Minecraft.getInstance().getTextureManager()
				.register(FilenameUtils.getName(path), tex);
		if (res == null)
			return null;
		Image img = new Image(res, n.getWidth(), n.getHeight(),path);
		putImage(img, path);
		return img;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @param path path that is saved in a CompoundNBT
	 * @return
	 */
	public Image tryToGetImage(String path) {
		if (photos.containsKey(path)) {
			return photos.get(path);
		} else if (files.containsKey(path)) {
			try {
				return addResourceFromFile(files.get(path), path);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void addFile(File file, String path) {
		files.put(path, file);
	}

	public void clear() {
		photos.clear();
	}

	public class Image {
		public ResourceLocation res;
		public int width;
		public int height;
		public String path;

		public Image(ResourceLocation res, int width, int height, String path) {
			this.res = res;
			this.width = width;
			this.height = height;
			this.path = path;
		}
	}
}
