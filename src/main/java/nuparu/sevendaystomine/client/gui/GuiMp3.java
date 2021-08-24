package nuparu.sevendaystomine.client.gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.util.math.MathHelper;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.util.ColorRGBA;
import org.apache.commons.io.FilenameUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.ScrollPanel;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.util.MP3Helper;
import nuparu.sevendaystomine.client.util.MP3Helper.Audio;
import nuparu.sevendaystomine.client.util.MP3Helper.AudioPlayer;
import nuparu.sevendaystomine.client.util.MP3Helper.EnumAudioMode;
import net.minecraft.util.text.ITextProperties;

@OnlyIn(Dist.CLIENT)
public class GuiMp3 extends Screen implements IGuiEventListener, INestedGuiEventHandler {
	private final int imageHeight = 194;
	private final int imageWidth = 176;
	NumberFormat formatter = new DecimalFormat("00");
	int guiLeft = 0;
	int guiTop = 0;
	static final ResourceLocation TEXTURE = new ResourceLocation(SevenDaysToMine.MODID, "textures/gui/mp3player.png");
	private GuiButtonPause buttonPause;
	private GuiButtonStop buttonStop;
	private GuiButtonRepeat buttonRepeat;
	private GuiButtonForward buttonForward;
	private GuiButtonBackward buttonBackward;
	Slider slider = new Slider();
	VolumeSlider volumeSlider = new VolumeSlider();
	GuiScrollbar scrollbar;
	String textNormal;
	String textShifted;
	int i;
	int pages;
	int page = 1;
	boolean playCompleted;
	float volume = 0.0f;
	long lastPress = 0L;
	int move = 0;

	public GuiMp3() {
		super(new StringTextComponent("screen.mp3"));
		this.height = 194;
	}

	public void selectAudioIndex(int index) {

		if (index <= MP3Helper.files.size()) {
			long now = System.nanoTime();
			if (index == MP3Helper.selected) {
				if ((now - lastPress) / 1000000 < 500) {
					playMusic(MP3Helper.files.get(index));
					lastPress = 0L;
				} else {
					lastPress = now;
				}

			} else {
				MP3Helper.selected = index;
				lastPress = now;
			}
		} else {
			MP3Helper.selected = MP3Helper.files.size();
			lastPress = 0L;
		}

	}

	public boolean isAudioSelected(int index) {
		return index == MP3Helper.selected;
	}

	public void getFiles() {
		MP3Helper.def.mkdirs();
		Path dir = Paths.get("./resources/audio/");

		try {

			ArrayList<Audio> arrayList = new ArrayList<Audio>();
			DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.{wav}");
			/*
			 * int previousY = this.guiTop+42-20; i = 0;
			 */
			for (Path entry : stream) {
				Audio audio = new Audio();
				audio.setPath(entry);
				audio.setName(FilenameUtils.removeExtension(entry.getFileName().toString()));
				// audio.duration =
				AudioInputStream audioStream = null;
				try {
					File audioFile = new File(entry.toString());
					audioStream = AudioSystem.getAudioInputStream(audioFile);
					AudioFormat format = audioStream.getFormat();
					long frames = audioStream.getFrameLength();
					audio.setDuration((frames + 0.0) / format.getFrameRate());
				} catch (UnsupportedAudioFileException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (audioStream != null) {
						audioStream.close();
					}
				}
				arrayList.add(audio);
			}
			MP3Helper.files = arrayList;
			stream.close();

		} catch (IOException x) {
			throw new RuntimeException(String.format("error reading folder %s: %s", dir, x.getMessage()), x);
		}
	}

	Minecraft getMinecraftInstance() {
		return this.minecraft;
	}

	FontRenderer getFontRenderer() {
		return font;
	}

	@Override
	public void init() {
		getFiles();
		this.guiLeft = (this.width - 176) / 2;
		this.guiTop = (this.height - 166) / 2;
		this.buttons.clear();
		int x = (this.width) / 2;
		this.scrollbar = new GuiScrollbar(this, x - 75, this.guiTop + 45, MP3Helper.files, 150);
		buttonPause = new GuiButtonPause( x - 10, this.guiTop + 155, (button) -> {
			System.out.println("DDDDDDD");
			actionPerformed(button);
        });

		buttonBackward = new GuiButtonBackward(x - 35, this.guiTop + 155, (button) -> {
			System.out.println("DDDDDDD");
			actionPerformed(button);
        });

		buttonStop = new GuiButtonStop(x - 60, this.guiTop + 155, (button) -> {
			System.out.println("DDDDDDD");
			actionPerformed(button);
        });

		buttonRepeat = new GuiButtonRepeat(x - 85, this.guiTop + 155, (button) -> {
			System.out.println("DDDDDDD");
			actionPerformed(button);
        });

		buttonForward = new GuiButtonForward( x + 15, this.guiTop + 155, (button) -> {
			System.out.println("DDDDDDD");
			actionPerformed(button);
        });

		slider.x = x - 75;
		slider.y = this.guiTop + 20;
		slider.xSize = 150;
		slider.ySize = 10;
		slider.zLevel =  + 1;

		volumeSlider.x = x + 36;
		volumeSlider.y = this.guiTop + 160;
		volumeSlider.xSize = 50;
		volumeSlider.ySize = 10;
		volumeSlider.zLevel = 1;

		addButton(buttonPause);
		addButton(buttonStop);
		addButton(buttonRepeat);
		addButton(buttonBackward);
		addButton(buttonForward);

	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	@Override
	public void tick() {
		if (MP3Helper.getAudioPlayer().getAudioClip() != null) {
			double currentSecond = MP3Helper.getAudioPlayer().getAudioClip().getMicrosecondPosition() / 1000000;
			if (MP3Helper.getAudioPlayer().getDuration() != 0) {
				slider.setValue((double) (currentSecond / MP3Helper.getAudioPlayer().getDurationInSecs()));
			}
		}
		volumeSlider.setValue(MP3Helper.audioVolume);
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	@Override
	public void render(MatrixStack matrix, int parWidth, int parHeight, float p_73863_3_) {
		matrix.pushPose();
		this.minecraft.getTextureManager().bind(TEXTURE);
		int k = (this.width - this.imageWidth) / 2;
		int l = (this.height - this.imageHeight) / 2;
		this.blit(matrix,k, l, 0, 0, this.imageWidth, this.imageHeight);
		if (this.scrollbar != null) {
			this.scrollbar.render(matrix,parWidth, parHeight, p_73863_3_);
		}
		if (volumeSlider != null) {
			slider.render(matrix);
		}
		if (volumeSlider != null) {
			volumeSlider.render(matrix);
		}
		double currentSecond = 0d;
		double durationSecond = 0d;

		if (MP3Helper.files == null || MP3Helper.files.size() <= 0) {
			AbstractGui.drawCenteredString(matrix,font,
					new TranslationTextComponent("mp3.nofile.0").getString(), this.guiLeft + 88,
					this.guiTop + 50, 5921370);
			AbstractGui.drawCenteredString(matrix,font,
					new TranslationTextComponent("mp3.nofile.1").getString(), this.guiLeft + 88,
					this.guiTop + 62, 5921370);
			font.drawWordWrap(ITextProperties.of(MP3Helper.def.getAbsolutePath()), this.guiLeft + 18, this.guiTop + 74, 140,
					5921370);
		}

		if (MP3Helper.getAudioPlayer().getAudioClip() != null) {
			currentSecond = MP3Helper.getAudioPlayer().getAudioClip().getMicrosecondPosition() / 1000000;
			durationSecond = MP3Helper.getAudioPlayer().getDurationInSecs();
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {
					if (textNormal == null) {
						textNormal = MP3Helper.files.get(MP3Helper.selected).getName();
						textShifted = textNormal + " ";
					}
					if (!textNormal.equals(MP3Helper.files.get(MP3Helper.selected).getName())) {
						textNormal = MP3Helper.files.get(MP3Helper.selected).getName();
						textShifted = textNormal + " ";
					}
					if (textShifted == null) {
						textShifted = textNormal + " ";
					}
					if (textShifted != null) {
						if (move >= 20) {
							textShifted = cyclicLeftShift(textShifted, 1);
							move = 0;
						} else {
							move++;
						}
						AbstractGui.drawCenteredString(matrix,font,
								font.plainSubstrByWidth(
										textShifted.substring(0, Math.min(textShifted.length(), 50)), 150),
								this.guiLeft + 88, this.guiTop + 5, 5921370);
					}
				} else {
					textShifted = null;
				}
			}
		}
		String string = new StringBuilder("").append(formatter.format(Math.floor(currentSecond / 60))).append(":")
				.append(formatter.format(currentSecond % 60)).append("/")
				.append(formatter.format(Math.floor(durationSecond / 60))).append(":")
				.append(formatter.format(durationSecond % 60)).toString();
		this.font.draw(matrix,string, this.guiLeft + 13, this.guiTop + 32, 4210752);

		// zLevel = 100.0F;

		matrix.popPose();
		super.render(matrix,parWidth, parHeight, p_73863_3_);
	}

	public String cyclicLeftShift(String s, int k) {
		k = k % s.length();
		return s.substring(k) + s.substring(0, k);
	}

@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		scrollbar.mouseClicked(mouseX,mouseY,mouseButton);
		slider.mouseClicked(mouseX, mouseY, mouseButton);
		volumeSlider.mouseClicked(mouseX, mouseY, mouseButton);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}


	public void actionPerformed(Button parButton) {
		System.out.println("DDD");
		if (parButton == buttonPause) {
			if (!MP3Helper.isPlaying) {

				if (MP3Helper.selected < MP3Helper.files.size()) {
					textShifted = MP3Helper.files.get(MP3Helper.selected).getName();
					playMusic(MP3Helper.files.get(MP3Helper.selected));
				}
			} else {
				if (MP3Helper.getAudioPlayer().isPaused()) {
					MP3Helper.getAudioPlayer().resume();
				} else {
					MP3Helper.getAudioPlayer().pause();
				}

			}
		}
		if (parButton == buttonStop) {
			if (MP3Helper.isPlaying) {
				MP3Helper.getAudioPlayer().stop();
			}
		}
		if (parButton == buttonRepeat) {
			if (MP3Helper.mode == EnumAudioMode.PLAY_ONCE) {
				MP3Helper.mode = EnumAudioMode.LOOP;
			} else if (MP3Helper.mode == EnumAudioMode.LOOP) {
				MP3Helper.mode = EnumAudioMode.CYCLE;
			} else if (MP3Helper.mode == EnumAudioMode.CYCLE) {
				MP3Helper.mode = EnumAudioMode.PLAY_ONCE;
			}

		}
		if (parButton == buttonForward) {
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {

					MP3Helper.getAudioPlayer().stop();
				}
				if (MP3Helper.selected + 1 < MP3Helper.files.size()) {
					MP3Helper.selected++;
				} else {
					MP3Helper.selected = 0;
				}
				playMusic(MP3Helper.files.get(MP3Helper.selected));
			}
		}
		if (parButton == buttonBackward) {
			if (MP3Helper.selected < MP3Helper.files.size()) {
				if (MP3Helper.isPlaying) {

					MP3Helper.getAudioPlayer().stop();
				}
				if (MP3Helper.selected > 0) {
					MP3Helper.selected--;
				} else {
					MP3Helper.selected = MP3Helper.files.size() - 1;
				}
				playMusic(MP3Helper.files.get(MP3Helper.selected));
			}
		}
	}

	public static void playMusic(final Audio audio) {

		if (MP3Helper.isPlaying) {

			MP3Helper.getAudioPlayer().getAudioClip().setMicrosecondPosition(0L);
			MP3Helper.getAudioPlayer().stop();
		}

		MP3Helper.playbackThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					MP3Helper.getAudioPlayer().load(audio.getPath().toString());
					MP3Helper.getAudioPlayer().setAudio(audio);
					MP3Helper.getAudioPlayer().play();

				} catch (UnsupportedAudioFileException ex) {

					ex.printStackTrace();
				} catch (LineUnavailableException ex) {

					ex.printStackTrace();
				} catch (IOException ex) {

					ex.printStackTrace();
				} catch (Exception ex) {

					ex.printStackTrace();
				}

			}
		});

		MP3Helper.playbackThread.start();
	}

	public void setVolume(float volume) {
		MP3Helper.audioVolume = volume;
		MP3Helper.getAudioPlayer().setVolume(MP3Helper.audioVolume);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	class GuiButtonPause extends Button {
		public GuiButtonPause(int xPos, int yPos, Button.IPressable press) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press);
		}

		public GuiButtonPause(int xPos, int yPos, Button.IPressable press, Button.ITooltip tooltip) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press,tooltip);
		}
		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft mc = Minecraft.getInstance();
				if (!MP3Helper.isPlaying || MP3Helper.isPlaying && MP3Helper.getAudioPlayer().isPaused()) {
					mc.getTextureManager().bind(TEXTURE);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.blit(stack,this.x, this.y, 176, i, this.width, this.height);
				} else {
					mc.getTextureManager().bind(TEXTURE);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.blit(stack,this.x, this.y, 196, i, this.width, this.height);
				}
			}
		}
	}

	class GuiButtonForward extends Button {
		public GuiButtonForward(int xPos, int yPos, Button.IPressable press) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press);
		}

		public GuiButtonForward(int xPos, int yPos, Button.IPressable press, Button.ITooltip tooltip) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press,tooltip);
		}
		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft mc = Minecraft.getInstance();
				mc.getTextureManager().bind(TEXTURE);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 78;

				if (flag) {
					i += this.height;
				}

				this.blit(stack,this.x, this.y, 176, i, this.width, this.height);
			}
		}
	}

	class GuiButtonBackward extends Button {

		public GuiButtonBackward(int xPos, int yPos, Button.IPressable press) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press);
		}

		public GuiButtonBackward(int xPos, int yPos, Button.IPressable press, Button.ITooltip tooltip) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press,tooltip);
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft mc = Minecraft.getInstance();
				mc.getTextureManager().bind(TEXTURE);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 78;

				if (flag) {
					i += this.height;
				}

				this.blit(stack,this.x, this.y, 196, i, this.width, this.height);
			}
		}

		@Override
		public void onPress() {

			System.out.println("FFFFFFFFFFFFFFFFFFFFFFF");
			this.onPress.onPress(this);
		}

	}

	class GuiButtonRepeat extends Button {
		public GuiButtonRepeat(int xPos, int yPos, Button.IPressable press) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press);
		}

		public GuiButtonRepeat(int xPos, int yPos, Button.IPressable press, Button.ITooltip tooltip) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press,tooltip);
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft mc = Minecraft.getInstance();
				if (MP3Helper.mode == EnumAudioMode.PLAY_ONCE) {
					mc.getTextureManager().bind(TEXTURE);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 38;

					if (flag) {
						i += this.height;
					}

					this.blit(stack,this.x, this.y, 236, i, this.width, this.height);
				} else if (MP3Helper.mode == EnumAudioMode.LOOP) {
					mc.getTextureManager().bind(TEXTURE);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 78;

					if (flag) {
						i -= this.height;
					}

					this.blit(stack,this.x, this.y, 236, i, this.width, this.height);
				} else if (MP3Helper.mode == EnumAudioMode.CYCLE) {
					mc.getTextureManager().bind(TEXTURE);
					RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
					boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					int i = 98;

					if (flag) {
						i += this.height;
					}

					this.blit(stack,this.x, this.y, 236, i, this.width, this.height);
				}
			}
		}
	}

	class GuiButtonStop extends Button {

		public GuiButtonStop(int xPos, int yPos, Button.IPressable press) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press);
		}

		public GuiButtonStop(int xPos, int yPos, Button.IPressable press, Button.ITooltip tooltip) {
			super(xPos, yPos, 20, 20, new StringTextComponent(""),press,tooltip);
		}

		/**
		 * Draws this button to the screen.
		 */
		@Override
		public void renderButton(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
			if (this.visible) {
				Minecraft mc = Minecraft.getInstance();
				mc.getTextureManager().bind(TEXTURE);
				RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
				boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
						&& mouseY < this.y + this.height;
				int i = 38;

				if (flag) {
					i += this.height;
				}

				this.blit(stack,this.x, this.y, 216, i, this.width, this.height);

			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class GuiScrollbar extends ScrollPanel {
		private GuiMp3 parent;
		private ArrayList<Audio> audios;

		public GuiScrollbar(GuiMp3 parent, int x, int y, ArrayList<Audio> audios, int listWidth) {
			super(parent.getMinecraftInstance(), 150, 100, y, x);
			this.parent = parent;
			this.audios = audios;

		}

		@Override
		protected int getScrollAmount() {
			return parent.font.lineHeight * 3;
		}

		@Override
		protected boolean clickPanel(double mouseX, double mouseY, int button) {

			int clampedY = (int) Math.max(0,mouseY);
			int slot = (int) Math.floor(clampedY/25d);

			parent.selectAudioIndex(slot);

			System.out.println(mouseX + " " + mouseY + " " + button + " " + slot);
			return super.clickPanel(mouseX, mouseY, button);
		}
		
		@Override
		protected void drawBackground() {
			// this.parent.drawDefaultBackground();
		}

		@Override
		protected int getContentHeight() {
			return this.audios.size()*20;
		}

		@Override
		protected void drawPanel(MatrixStack stack, int entryRight, int relativeY, Tessellator tess, int mouseX,
				int mouseY) {
			stack.pushPose();
			FontRenderer font = this.parent.getFontRenderer();
			if(!audios.isEmpty()){
				for(int i = 0; i < audios.size(); i++){
					Audio audio = audios.get(i);
					if(audio != null){
						font.draw(stack,font.plainSubstrByWidth(audio.getName(), width - 10), this.left + 3, top + 2,
								MP3Helper.selected == i ? 16777120 : 0xFFFFFF);
						font.draw(stack,font.plainSubstrByWidth(
								new StringBuilder("").append(parent.formatter.format(Math.floor(audio.getDuration() / 60)))
										.append(":").append(parent.formatter.format(audio.getDuration() % 60)).toString(),
								width - 10), this.left + 3, top + 12, 0xCCCCCC);
						if(MP3Helper.selected == i){

						}
						stack.translate(0,25,0);
					}
				}
			}

			stack.popPose();
			/*
			font.draw(stack,font.plainSubstrByWidth(audios.get(entryRight).getName(), width - 10), this.left + 3, top + 2,
					0xFFFFFF);
			font.draw(stack,font.plainSubstrByWidth(
					new StringBuilder("").append(parent.formatter.format(Math.floor(audios.get(entryRight).getDuration() / 60)))
							.append(":").append(parent.formatter.format(audios.get(entryRight).getDuration() % 60)).toString(),
					width - 10), this.left + 3, top + 12, 0xCCCCCC);
*/
		}

			
		}

	@OnlyIn(Dist.CLIENT)
	public static class Slider {
		double x;
		double y;
		double xSize;
		double ySize;
		double zLevel;

		double value;

		ColorRGBA color = new ColorRGBA(0.65, 0.65, 0.65);
		ColorRGBA cursorColor = new ColorRGBA(0.2, 0.2, 0.2);

		public Slider() {
		}

		public Slider(double x, double y, double xSize, double ySize, double zLevel) {
			this.x = x;
			this.y = y;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zLevel = zLevel;
		}

		public void render(MatrixStack matrixStack) {
			RenderUtils.drawColoredRect(matrixStack,color, x, y, xSize, ySize, zLevel);
			drawCursor(matrixStack,x + (xSize * value), y, 1, ySize, zLevel + 1);
		}

		public void drawCursor(MatrixStack matrixStack, double x, double y, double width, double height, double zLevel) {
			matrixStack.translate(0, 0, zLevel);
			AbstractGui.fill(matrixStack, (int)x, (int)y, (int)(x+width), (int)(y+height), this.cursorColor.toHex());
			matrixStack.translate(0, 0, -zLevel);
		}



		public void setValue(double value) {
			this.value = value;
		}

		public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
			if (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize) {
				double part = (double) (mouseX - x) / xSize;
				AudioPlayer player = MP3Helper.getAudioPlayer();
				if (player == null || player.getAudioClip() == null)
					return true;
				player.getAudioClip().setMicrosecondPosition(
						(long) ((MP3Helper.getAudioPlayer().getDurationInSecs() * 1000000) * part));
				return true;
			}
			return false;
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class VolumeSlider {
		double x;
		double y;
		double xSize;
		double ySize;
		double zLevel;

		float value;

		ColorRGBA color = new ColorRGBA(0.65, 0.65, 0.65);
		ColorRGBA cursorColor = new ColorRGBA(0.2, 0.2, 0.2);

		public VolumeSlider() {
		}

		public VolumeSlider(double x, double y, double xSize, double ySize, double zLevel) {
			this.x = x;
			this.y = y;
			this.xSize = xSize;
			this.ySize = ySize;
			this.zLevel = zLevel;
		}

		public void render(MatrixStack matrixStack) {
			RenderUtils.drawColoredRect(matrixStack,color, x, y, xSize, ySize, zLevel);
			drawCursor(matrixStack,x + (xSize * value), y, 1, ySize, zLevel + 1);
		}

		public void drawCursor(MatrixStack matrixStack, double x, double y, double width, double height, double zLevel) {
			matrixStack.translate(0, 0, zLevel);
			AbstractGui.fill(matrixStack, (int)x, (int)y, (int)(x+width), (int)(y+height), this.cursorColor.toHex());
			matrixStack.translate(0, 0, -zLevel);
		}

		public void setValue(float value) {
			this.value = value;
		}

		public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
			if (mouseX >= x && mouseX <= x + xSize && mouseY >= y && mouseY <= y + ySize) {
				float part = (float) ((mouseX - x) / xSize);
				MP3Helper.audioVolume = part;
				MP3Helper.getAudioPlayer().setVolume(Math.abs(MP3Helper.audioVolume - 1));
				return true;
			}
			return false;
		}
	}

}