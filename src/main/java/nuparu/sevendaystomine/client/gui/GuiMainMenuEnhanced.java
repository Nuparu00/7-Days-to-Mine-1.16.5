package nuparu.sevendaystomine.client.gui;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import net.minecraft.client.MainWindow;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import nuparu.sevendaystomine.util.MathUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.util.concurrent.Runnables;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AccessibilityScreen;
import net.minecraft.client.gui.DialogTexts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.LanguageScreen;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.MultiplayerWarningScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WinGameScreen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.gui.toasts.SystemToast;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.realms.RealmsBridgeScreen;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.gen.settings.DimensionGeneratorSettings;
import net.minecraft.world.storage.SaveFormat;
import net.minecraft.world.storage.WorldSummary;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;

@OnlyIn(Dist.CLIENT)
public class GuiMainMenuEnhanced extends Screen {
	private static final Logger LOGGER = LogManager.getLogger();
	/*public static final RenderSkyboxCube CUBE_MAP = new RenderSkyboxCube(
			new ResourceLocation("textures/gui/title/background/panorama"));*/
	private static final ResourceLocation PANORAMA_OVERLAY = new ResourceLocation(
			"textures/gui/title/background/panorama_overlay.png");
	private static final ResourceLocation ACCESSIBILITY_TEXTURE = new ResourceLocation(
			"textures/gui/accessibility.png");
	private final boolean minceraftEasterEgg;
	@Nullable
	private String splash;
	private Button resetDemoButton;
	private static final ResourceLocation MINECRAFT_LOGO = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/logo.png");
	private static final ResourceLocation MINECRAFT_EDITION = new ResourceLocation("textures/gui/title/edition.png");
	private boolean realmsNotificationsInitialized;
	private Screen realmsNotificationsScreen;
	private int copyrightWidth;
	private int copyrightX;
	//private final RenderSkybox panorama = new RenderSkybox(CUBE_MAP);
	private final boolean fading;
	private long fadeInStart;
	private net.minecraftforge.client.gui.NotificationModUpdateScreen modUpdateNotification;

	/*
	7 Days to Mine part
	 */

	public static final ResourceLocation BGR_DAY = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_day.png");
	public static final ResourceLocation BGR_SNOW = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_snow.png");
	public static final ResourceLocation BGR_BLOODMOON = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_bloodmoon.png");
	public static final ResourceLocation BGR_NIGHT = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/gui/title/background/background_night.png");

	public static final int MINIMAL_DUST_PARTICLES = 300;
	public static final int NATURAL_MAXIMUM_DUST_PARTICLES = 256;
	public List<Dust> dusts = new ArrayList<Dust>();
	public List<Dust> dustsToAdd = new ArrayList<Dust>();
	public List<Dust> dustsToRemove = new ArrayList<Dust>();
	public boolean drawDustMode = true;
	public boolean drawing = false;

	private int mX = 0;
	private int mY = 0;

	public static ResourceLocation background = BGR_DAY;
	public int bgr = 0;
	private boolean bday = false;
	private Random random = new Random();

	public GuiMainMenuEnhanced() {
		this(false);
	}

	public GuiMainMenuEnhanced(boolean p_i51107_1_) {
		super(new TranslationTextComponent("narrator.screen.title"));
		this.fading = p_i51107_1_;
		this.minceraftEasterEgg = (double) (new Random()).nextFloat() < 1.0E-4D;

		if (MathUtils.getIntInRange(0, 50) == 0) {
			background = BGR_NIGHT;
			bgr = 3;
		}

	}

	private boolean realmsNotificationsEnabled() {
		return this.minecraft.options.realmsNotifications && this.realmsNotificationsScreen != null;
	}

	public void tick() {
		if (this.realmsNotificationsEnabled()) {
			this.realmsNotificationsScreen.tick();
		}

		dusts.addAll(dustsToAdd);
		dusts.removeAll(dustsToRemove);
		dustsToRemove.clear();
		dustsToAdd.clear();

		if ((dusts.size() + dustsToAdd.size()) < NATURAL_MAXIMUM_DUST_PARTICLES) {
			while ((dusts.size() + dustsToAdd.size()) < Math.min(
					ThreadLocalRandom.current().nextInt(MINIMAL_DUST_PARTICLES, MINIMAL_DUST_PARTICLES + 20),
					NATURAL_MAXIMUM_DUST_PARTICLES)) {
				dustsToAdd.add(summonDust(random, minecraft.getWindow()));
			}
		}

		if (drawing && drawDustMode) {
			for(int i = 0; i < (Screen.hasShiftDown() ? 5 : 1); i++) {
				Dust dust = summonDust(random, minecraft.getWindow());
				dust.x = mX;
				dust.y = mY;
				dusts.add(dust);
			}
		}

	}
/*
	public static CompletableFuture<Void> preloadResources(TextureManager p_213097_0_, Executor p_213097_1_) {
		return CompletableFuture.allOf(p_213097_0_.preload(MINECRAFT_LOGO, p_213097_1_),
				p_213097_0_.preload(MINECRAFT_EDITION, p_213097_1_), p_213097_0_.preload(PANORAMA_OVERLAY, p_213097_1_),
				CUBE_MAP.preload(p_213097_0_, p_213097_1_));
	}*/

	public boolean isPauseScreen() {
		return false;
	}

	public boolean shouldCloseOnEsc() {
		return false;
	}

	protected void init() {
		dusts.clear();

		if (this.splash == null) {
			this.splash = this.minecraft.getSplashManager().getSplash();
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int month = calendar.get(2) + 1;
		int day = calendar.get(5);

		if (month == 12 && day >= 24 && day <= 26) {
			background = BGR_SNOW;
			bgr = 1;
		}
		else if (month == 10 && day == 31) {
			background = BGR_BLOODMOON;
			bgr = 2;
		}
		if (month == 7 && day == 3) {
			bday = true;
		}

		this.copyrightWidth = this.font.width("Copyright Mojang AB. Do not distribute!");
		this.copyrightX = this.width - this.copyrightWidth - 2;
		int i = 24;
		int j = this.height / 4;
		Button modButton = null;
		if (this.minecraft.isDemo()) {
			this.createDemoMenuOptions(j, 24);
		} else {
			this.createNormalMenuOptions(j, 18);
			modButton = this.addButton(new ButtonTransparent(4, j + 18 * 3, this.font.width(SevenDaysToMine.proxy.localize("fml.menu.mods")), this.font.lineHeight,
					new TranslationTextComponent("fml.menu.mods"), button -> this.minecraft.setScreen(new net.minecraftforge.fml.client.gui.screen.ModListScreen(this))));
		}
		modUpdateNotification = getNotificationModUpdateScreen(this, modButton);

		this.addButton(new ImageButton(4,4, 20, 20, 0, 106, 20, Button.WIDGETS_LOCATION,
				256, 256, (p_213090_1_) -> this.minecraft.setScreen(
                        new LanguageScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), new TranslationTextComponent("narrator.button.language")));
		this.addButton(new ButtonTransparent(4, j + 18 *4, this.font.width(SevenDaysToMine.proxy.localize("menu.options")),
			this.font.lineHeight,
				new TranslationTextComponent("menu.options"), (p_213096_1_) -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options))));
		this.addButton(new ButtonTransparent(4, j + 18 *5,this.font.width(SevenDaysToMine.proxy.localize("menu.quit")),
				this.font.lineHeight,
				new TranslationTextComponent("menu.quit"), (p_213094_1_) -> this.minecraft.stop()));
		this.addButton(new ImageButton(28, 4, 20, 20, 0, 0, 20, ACCESSIBILITY_TEXTURE, 32,
				64, (p_213088_1_) -> this.minecraft.setScreen(new AccessibilityScreen(this, this.minecraft.options)), new TranslationTextComponent("narrator.button.accessibility")));
		this.minecraft.setConnectedToRealms(false);
		if (this.minecraft.options.realmsNotifications && !this.realmsNotificationsInitialized) {
			RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
			this.realmsNotificationsScreen = realmsbridgescreen.getNotificationScreen(this);
			this.realmsNotificationsInitialized = true;
		}

		if (this.realmsNotificationsEnabled()) {
			this.realmsNotificationsScreen.init(this.minecraft, this.width, this.height);
		}

	}

	private void createNormalMenuOptions(int p_73969_1_, int p_73969_2_) {
		this.addButton(new ButtonTransparent(4, p_73969_1_, this.font.width(SevenDaysToMine.proxy.localize("menu.singleplayer")), this.font.lineHeight,
				new TranslationTextComponent("menu.singleplayer"), (p_213089_1_) -> this.minecraft.setScreen(new WorldSelectionScreen(this))));
		boolean flag = this.minecraft.allowsMultiplayer();
		Button.ITooltip button$itooltip = flag ? Button.NO_TOOLTIP
				: (p_238659_1_, p_238659_2_, p_238659_3_, p_238659_4_) -> {
					if (!p_238659_1_.active) {
						this.renderTooltip(p_238659_2_,
								this.minecraft.font.split(new TranslationTextComponent("title.multiplayer.disabled"),
										Math.max(this.width / 2 - 43, 170)),
								p_238659_3_, p_238659_4_);
					}

				};
		(this.addButton(new ButtonTransparent(4, p_73969_1_ + p_73969_2_, this.font.width(SevenDaysToMine.proxy.localize("menu.multiplayer")), this.font.lineHeight,
				new TranslationTextComponent("menu.multiplayer"), (p_213095_1_) -> {
					Screen screen = this.minecraft.options.skipMultiplayerWarning
							? new MultiplayerScreen(this)
							: new MultiplayerWarningScreen(this);
					this.minecraft.setScreen(screen);
				}, button$itooltip))).active = flag;
		(this.addButton(new ButtonTransparent(4, p_73969_1_ + p_73969_2_ * 2, this.font.width(SevenDaysToMine.proxy.localize("menu.online")), this.font.lineHeight,
				new TranslationTextComponent("menu.online"), (p_238661_1_) -> this.realmsButtonClicked(), button$itooltip))).active = flag;
	}

	private void createDemoMenuOptions(int p_73972_1_, int p_73972_2_) {
		boolean flag = this.checkDemoWorldPresence();
		this.addButton(new ButtonTransparent(this.width / 2 - 100, p_73972_1_, 200, 20,
				new TranslationTextComponent("menu.playdemo"), (p_213091_2_) -> {
					if (flag) {
						this.minecraft.loadLevel("Demo_World");
					} else {
						DynamicRegistries.Impl dynamicregistries$impl = DynamicRegistries.builtin();
						this.minecraft.createLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, dynamicregistries$impl,
								DimensionGeneratorSettings.demoSettings(dynamicregistries$impl));
					}

				}));
		this.resetDemoButton = this.addButton(new ButtonTransparent(this.width / 2 - 100, p_73972_1_ + p_73972_2_,
				200, 20, new TranslationTextComponent("menu.resetdemo"), (p_238658_1_) -> {
					SaveFormat saveformat = this.minecraft.getLevelSource();

					try (SaveFormat.LevelSave saveformat$levelsave = saveformat.createAccess("Demo_World")) {
						WorldSummary worldsummary = saveformat$levelsave.getSummary();
						if (worldsummary != null) {
							this.minecraft.setScreen(new ConfirmScreen(this::confirmDemo,
									new TranslationTextComponent("selectWorld.deleteQuestion"),
									new TranslationTextComponent("selectWorld.deleteWarning",
											worldsummary.getLevelName()),
									new TranslationTextComponent("selectWorld.deleteButton"), DialogTexts.GUI_CANCEL));
						}
					} catch (IOException ioexception) {
						SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
						LOGGER.warn("Failed to access demo world", ioexception);
					}

				}));
		this.resetDemoButton.active = flag;
	}

	private boolean checkDemoWorldPresence() {
		try (SaveFormat.LevelSave saveformat$levelsave = this.minecraft.getLevelSource().createAccess("Demo_World")) {
			return saveformat$levelsave.getSummary() != null;
		} catch (IOException ioexception) {
			SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
			LOGGER.warn("Failed to read demo world data", ioexception);
			return false;
		}
	}

	private void realmsButtonClicked() {
		RealmsBridgeScreen realmsbridgescreen = new RealmsBridgeScreen();
		realmsbridgescreen.switchToRealms(this);
	}

	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {

		if (this.fadeInStart == 0L && this.fading) {
			this.fadeInStart = Util.getMillis();
		}

		float f = this.fading ? (float) (Util.getMillis() - this.fadeInStart) / 1000.0F : 1.0F;
		fill(matrixStack, 0, 0, this.width, this.height, -1);

		List<Dust> dustsClone = new ArrayList<Dust>();
		dustsClone.addAll(dusts);
		ListIterator<Dust> it = dustsClone.listIterator();
		while (it.hasNext()) {
			Dust dust = it.next();
			dust.update(mX, mY);
			if (dust.x - dust.scale > ((minecraft.getWindow().getGuiScaledWidth())) || dust.y - dust.scale > ((minecraft.getWindow().getGuiScaledHeight()))
					|| dust.x + dust.scale < 0 || dust.y + dust.scale < 0) {
				dust.opacity -= 0.05f;
			}
		}

		this.mX = mouseX;
		this.mY = mouseY;

		this.renderStaticBgr(this.minecraft,this.minecraft.getWindow(),matrixStack);

		Iterator<Dust> it2 = dusts.iterator();
		while (it2.hasNext()) {
			Dust dust = it2.next();
			dust.draw(matrixStack);
		}

		// this.panorama.render(partialTicks, MathHelper.clamp(f, 0.0F, 1.0F));
		int i = 274;
		int j = this.width / 2 - 137;
		int k = 30;
		this.minecraft.getTextureManager().bind(PANORAMA_OVERLAY);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F,
				this.fading ? (float) MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)) : 1.0F);
		blit(matrixStack, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float f1 = this.fading ? MathHelper.clamp(f - 1.0F, 0.0F, 1.0F) : 1.0F;
		int l = MathHelper.ceil(f1 * 255.0F) << 24;
		if ((l & -67108864) != 0) {
			this.minecraft.getTextureManager().bind(MINECRAFT_LOGO);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, f1);
			/*if (this.minceraftEasterEgg) {
				this.blitOutlineBlack(j, 30, (p_238660_2_, p_238660_3_) -> {
					this.blit(matrixStack, p_238660_2_ + 0, p_238660_3_, 0, 0, 99, 44);
					this.blit(matrixStack, p_238660_2_ + 99, p_238660_3_, 129, 0, 27, 44);
					this.blit(matrixStack, p_238660_2_ + 99 + 26, p_238660_3_, 126, 0, 3, 44);
					this.blit(matrixStack, p_238660_2_ + 99 + 26 + 3, p_238660_3_, 99, 0, 26, 44);
					this.blit(matrixStack, p_238660_2_ + 155, p_238660_3_, 0, 45, 155, 44);
				});
			} else {*/
				this.blitOutlineBlack(j, 15, (p_238657_2_, p_238657_3_) -> {
					this.blit(matrixStack, p_238657_2_, p_238657_3_, 0, 0, 155, 44);
					this.blit(matrixStack, p_238657_2_ + 155, p_238657_3_, 0, 45, 155, 44);
				});
			//}

			this.minecraft.getTextureManager().bind(MINECRAFT_EDITION);
			blit(matrixStack, j + 88, 67-15, 0.0F, 0.0F, 98, 14, 128, 16);
			/*
			 * net.minecraftforge.client.ForgeHooksClient.renderMainMenu(this, matrixStack,
			 * this.font, this.width, this.height, l);
			 */
			if (this.splash != null) {
				RenderSystem.pushMatrix();
				RenderSystem.translatef((float) (this.width / 2 + 90), 70.0F-15, 0.0F);
				RenderSystem.rotatef(-20.0F, 0.0F, 0.0F, 1.0F);
				float f2 = 1.8F - MathHelper.abs(
						MathHelper.sin((float) (Util.getMillis() % 1000L) / 1000.0F * ((float) Math.PI * 2F)) * 0.1F);
				f2 = f2 * 100.0F / (float) (this.font.width(this.splash) + 32);
				RenderSystem.scalef(f2, f2, f2);
				drawCenteredString(matrixStack, this.font, this.splash, 0, -8, 16776960 | l);
				RenderSystem.popMatrix();
			}

			String s = "Minecraft " + SharedConstants.getCurrentVersion().getName();
			if (this.minecraft.isDemo()) {
				s = s + " Demo";
			} else {
				s = s + ("release".equalsIgnoreCase(this.minecraft.getVersionType()) ? ""
						: "/" + this.minecraft.getVersionType());
			}

			if (this.minecraft.isProbablyModded()) {
				s = s + I18n.get("menu.modded");
			}

			net.minecraftforge.fml.BrandingControl.forEachLine(true, true, (brdline, brd) -> drawString(matrixStack,
					this.font, brd, 2, this.height - (10 + brdline * (this.font.lineHeight + 1)), 16777215 | l));

			net.minecraftforge.fml.BrandingControl.forEachAboveCopyrightLine(
					(brdline, brd) -> drawString(matrixStack, this.font, brd, this.width - font.width(brd),
							this.height - (10 + (brdline + 1) * (this.font.lineHeight + 1)), 16777215 | l));

			drawString(matrixStack, this.font, "Copyright Mojang AB. Do not distribute!", this.copyrightX,
					this.height - 10, 16777215 | l);
			if (mouseX > this.copyrightX && mouseX < this.copyrightX + this.copyrightWidth
					&& mouseY > this.height - 10 && mouseY < this.height) {
				fill(matrixStack, this.copyrightX, this.height - 1, this.copyrightX + this.copyrightWidth, this.height,
						16777215 | l);
			}

			for (Widget widget : this.buttons) {
				widget.setAlpha(f1);
			}

			super.render(matrixStack, mouseX, mouseY, partialTicks);
			if (this.realmsNotificationsEnabled() && f1 >= 1.0F) {
				this.realmsNotificationsScreen.render(matrixStack, mouseX, mouseY, partialTicks);
			}
			modUpdateNotification.render(matrixStack, mouseX, mouseY, partialTicks);

		}
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (drawDustMode && mouseButton == 0) {
			drawing = true;
		}

		if (super.mouseClicked(mouseX, mouseY, mouseButton)) {
			return true;
		} else if (this.realmsNotificationsEnabled()
				&& this.realmsNotificationsScreen.mouseClicked(mouseX, mouseY, mouseButton)) {
			return true;
		} else {
			if (mouseX > (double) this.copyrightX && mouseX < (double) (this.copyrightX + this.copyrightWidth)
					&& mouseY > (double) (this.height - 10) && mouseY < (double) this.height) {
				this.minecraft.setScreen(new WinGameScreen(false, Runnables.doNothing()));
			}

			return false;
		}
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		drawing = false;
		return super.mouseReleased(mouseX,mouseY,mouseButton);
	}

	public void removed() {
		if (this.realmsNotificationsScreen != null) {
			this.realmsNotificationsScreen.removed();
		}

	}

	private void confirmDemo(boolean p_213087_1_) {
		if (p_213087_1_) {
			try (SaveFormat.LevelSave saveformat$levelsave = this.minecraft.getLevelSource()
					.createAccess("Demo_World")) {
				saveformat$levelsave.deleteLevel();
			} catch (IOException ioexception) {
				SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
				LOGGER.warn("Failed to delete demo world", ioexception);
			}
		}

		this.minecraft.setScreen(this);
	}

	public static ModdedNotificationModUpdateScreen getNotificationModUpdateScreen(GuiMainMenuEnhanced guiMainMenu,
			Button modButton) {
		ModdedNotificationModUpdateScreen notificationModUpdateScreen = new ModdedNotificationModUpdateScreen(modButton);
		notificationModUpdateScreen.resize(guiMainMenu.getMinecraft(), guiMainMenu.width, guiMainMenu.height);
		notificationModUpdateScreen.init();
		return notificationModUpdateScreen;
	}

	public static class ButtonTransparent extends Button {

		public ButtonTransparent(int x, int y, int width, int height,
				ITextComponent p_i232255_5_, Button.IPressable p_i232255_6_) {
			super(x, y, width, height, p_i232255_5_, p_i232255_6_);
		}

		public ButtonTransparent(int x, int y, int width, int height,
				ITextComponent p_i232256_5_, Button.IPressable p_i232256_6_, Button.ITooltip p_i232256_7_) {
			super(x, y, width, height, p_i232256_5_, p_i232256_6_, p_i232256_7_);
		}

		@Override
		public void renderButton(MatrixStack p_230431_1_, int p_230431_2_, int p_230431_3_, float p_230431_4_) {
			Minecraft minecraft = Minecraft.getInstance();
			FontRenderer fontrenderer = minecraft.font;
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.enableDepthTest();
			int j = getFGColor();

			if (!this.active) {
				j = 10526880;
			}

			else if (this.isHovered()) {
				j = 16777120;
			}
			drawCenteredString(p_230431_1_, fontrenderer, this.getMessage(), this.x + this.width / 2,
					this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
		}

	}

	public void renderStaticBgr(Minecraft mc, MainWindow sr, MatrixStack stack) {

		stack.pushPose();
		RenderSystem.disableDepthTest();
		RenderSystem.enableBlend();
		RenderSystem.blendFuncSeparate(770, 771, 1, 0);
		RenderSystem.enableAlphaTest();
		RenderSystem.color4f(1,1,1,1);
		mc.getTextureManager().bind(background);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder worldrenderer = tessellator.getBuilder();
		worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
		worldrenderer.vertex(0.0D, (double) sr.getGuiScaledHeight(), -90.0D).uv(0.0f, 1.0f).endVertex();
		worldrenderer.vertex((double) sr.getGuiScaledWidth(), (double) sr.getGuiScaledHeight(), -90.0D)
				.uv(1.0f, 1.0f).endVertex();
		worldrenderer.vertex((double) sr.getGuiScaledWidth(), 0.0D, -90.0D).uv(1.0f, 0.0f).endVertex();
		worldrenderer.vertex(0.0D, 0.0D, -90.0D).uv(0.0f, 0.0f).endVertex();
		tessellator.end();
		RenderSystem.enableDepthTest();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1);
		RenderSystem.disableBlend();
		stack.popPose();
	}


	public Dust summonDust(Random rand, MainWindow sr) {
		float x = rand.nextFloat() * sr.getGuiScaledWidth();
		float y = MathUtils.getFloatInRange(0f, 0.75f) * sr.getGuiScaledHeight();
		float motionX = rand.nextFloat() * 0.6f - 0.15f;
		float motionY = rand.nextFloat()*0.66f;
		float[] rgb = new float[3];
		float opacity = MathUtils.getFloatInRange(0.1f, 0.5f);

		if (bday) {
			rgb[0] = rand.nextFloat();
			rgb[1] = rand.nextFloat();
			rgb[2] = rand.nextFloat();
		} else {
			rgb[0] = 0.941F;
			rgb[1] = 0.902F;
			rgb[2] = 0.549F;

			if (bgr == 1 || bgr == 3) {
				float c = (rgb[0] + rgb[1] + rgb[2]) / 3;
				if(bgr == 3) {
					opacity/=2;
					c*=0.7f;
				}
				rgb[0] = c;
				rgb[1] = c;
				rgb[2] = c;
			}
			else if(bgr == 2) {
				rgb[0] = rgb[0]/1.2f;
				rgb[1] = rgb[1]/3;
				rgb[2] = rgb[2]/5;
				opacity/=1.5;
			}

		}

		return new Dust(x, y, motionX, motionY, MathUtils.getFloatInRange(0.017528f, 0.80f),
				opacity, rgb, this);
	}

}
