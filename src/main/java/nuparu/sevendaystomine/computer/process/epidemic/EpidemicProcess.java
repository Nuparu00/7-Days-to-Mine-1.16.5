package nuparu.sevendaystomine.computer.process.epidemic;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import nuparu.sevendaystomine.SevenDaysToMine;
import nuparu.sevendaystomine.client.gui.monitor.MonitorScreen;
import nuparu.sevendaystomine.client.gui.monitor.elements.Button;
import nuparu.sevendaystomine.client.gui.monitor.elements.TextField;
import nuparu.sevendaystomine.client.util.RenderUtils;
import nuparu.sevendaystomine.computer.application.ApplicationRegistry;
import nuparu.sevendaystomine.computer.process.WindowedProcess;
import nuparu.sevendaystomine.util.ColorRGBA;

public class EpidemicProcess extends WindowedProcess {

	public final ResourceLocation TITLE = new ResourceLocation(SevenDaysToMine.MODID,
			"textures/apps/epidemic/title.png");

	/*
	 * Elements
	 */

	public Button buttonNewGame;
	public TextField fieldCreateName;
	public Button buttonStart;
	public Button buttonDisease;
	public Button buttonBack;
	public Button buttonCoughing;
	public Button buttonSneezing;
	public Button buttonVomiting;
	public Button buttonSeizures;
	public Button buttonNecrosis;
	public Button buttonOrganFailure;
	public Button buttonFaster;
	public Button buttonSlower;
	public Button buttonStop;
	public Button buttonWorld;
	public Button buttonShowInfected;
	public Button buttonMainMenu;

	/*
	 * Game Data
	 */

	public String diseaseName = "";
	public List<EpidemicCountry> countries = new ArrayList<EpidemicCountry>();
	public List<EnumUpgrade> upgrades = new ArrayList<EnumUpgrade>();

	public double day;
	public double speed;
	public int points;
	public int vaccineProgress;
	public boolean discovered;

	public static HashMap<String, BufferedImage> mapCache = new HashMap<String, BufferedImage>();

	public EnumGameState gameState = EnumGameState.MENU;

	public EpidemicProcess() {
		this(0, 0, 0, 0);
	}

	public EpidemicProcess(double x, double y, double width, double height) {
		super(x, y, width, height);
		this.application = ApplicationRegistry.INSTANCE.getByString("epidemic");
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void render(MatrixStack matrix,float partialTicks) {
		drawWindow(matrix,getTitle(), new ColorRGBA(0d, 0d, 0d), new ColorRGBA(0.8, 0.8, 0.8));

		FontRenderer font = Minecraft.getInstance().font;

		RenderSystem.pushMatrix();
		RenderSystem.translated(0, 0, zLevel);

		ResourceLocation bgr = gameState.background;

		RenderUtils.drawTexturedRect(matrix,bgr, x, y + (MonitorScreen.screen.ySize * title_bar_height), 0, 0, width,
				height - (MonitorScreen.screen.ySize * title_bar_height), width,
				height - (MonitorScreen.screen.ySize * title_bar_height), 1, 1);
		if (gameState == EnumGameState.MENU || gameState == EnumGameState.CREATE) {
			RenderSystem.enableAlphaTest();
			RenderSystem.enableBlend();
			RenderUtils.drawTexturedRect(matrix,TITLE, x + width / 2 - width / 8,
					y + (MonitorScreen.screen.ySize * title_bar_height) + 1, 0, 0, width / 4, width / 16, width / 4,
					width / 16, 1, 2);
			RenderSystem.disableBlend();
			RenderSystem.disableAlphaTest();
		} else if (gameState == EnumGameState.GAME || gameState == EnumGameState.SELECT_START) {
			RenderSystem.enableAlphaTest();
			RenderSystem.enableBlend();
			for (EpidemicCountry country : countries) {
				float g = ((float) country.getHealthy() / (float) country.population);
				float r = 1f - g;
				g *= ((float) country.getAlive() / country.population);
				r *= ((float) country.getAlive() / country.population);
				GL11.glColor3f(r, g, 0);
				RenderUtils.drawTexturedRect(matrix,country.texture, x, y + (MonitorScreen.screen.ySize * title_bar_height), 0, 0,
						width, height - (MonitorScreen.screen.ySize * title_bar_height), width,
						height - (MonitorScreen.screen.ySize * title_bar_height), 1, 2);
				GL11.glColor3f(1, 1, 1);
			}
			RenderSystem.disableBlend();
			RenderSystem.disableAlphaTest();

			RenderSystem.translated(0, 0, 2);
			RenderUtils.drawString(matrix,"Day " + (int) Math.floor(this.day) + " (" + speed + "x)", x + 2,
					y + (MonitorScreen.screen.ySize * title_bar_height) + 2, 0xffffff);

			String inf = formatNumber(this.getGlobalInfected()) + " Infected";
			String dead = formatNumber(this.getGlobalDead()) + " Dead";

			RenderUtils.drawString(matrix,inf, x + 2, y + height - font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,dead, x + width - font.width(dead),
					y + height - font.lineHeight, 0xffffff);
			RenderSystem.translated(0, 0, -2);

			EpidemicCountry country = getHoveredEpidemicCountry();
			if (country != null) {
				RenderSystem.translated(0, 0, 2);
				if(Screen.hasShiftDown()){
					this.renderTooltip(matrix,screen.mouseX, screen.mouseY, font, country.name,
							"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected),
							"Density " + country.getDensity(), "Area " + country.area);
					}
					else {
						this.renderTooltip(matrix,screen.mouseX, screen.mouseY, font, country.name,
								"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected));
					}
				RenderSystem.translated(0, 0, -2);
			}
		} else if (gameState == EnumGameState.DISEASE) {
			RenderSystem.translated(0, 0, 1);
			RenderUtils.drawCenteredString(matrix,diseaseName, x + width / 2, y + font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Infectivity", x + width / 2 - 1.5 * font.width("Infectivity"),
					y + 3 * font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Lethality", x + width / 2 + 0.5 * font.width("Lethality"),
					y + 3 * font.lineHeight, 0xffffff);
			RenderSystem.translated(0, 0, -1);
		} else if (gameState == EnumGameState.WORLD) {
			RenderSystem.translated(0, 0, 1);
			RenderUtils.drawCenteredString(matrix,"World", x + width / 2, y + font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Alive: " + formatNumber(this.getGlobalAlive()), x + 5,
					y + 3 * font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Healthy: " + formatNumber(this.getGlobalHealthy()), x + width / 2,
					y + 3 * font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Infected: " + formatNumber(this.getGlobalInfected()), x + 5,
					y + 4 * font.lineHeight, 0xffffff);
			RenderUtils.drawString(matrix,"Dead: " + formatNumber(this.getGlobalDead()), x + width / 2,
					y + 4 * font.lineHeight, 0xffffff);
			RenderUtils.drawCenteredString(matrix,"Vaccine Progress: " + vaccineProgress + "%", x + width / 2,
					y + 6 * font.lineHeight, 0xffffff);
			RenderSystem.translated(0, 0, -1);
		} else if (gameState == EnumGameState.INFECTED_MAP) {
			RenderSystem.enableAlphaTest();
			RenderSystem.enableBlend();
			for (EpidemicCountry country : countries) {

				if (country.getAlive() == 0) {
					GL11.glColor3f(0.1f, 0, 0);
				} else if (country.isInfected()) {
					GL11.glColor3f(0.75f, 0, 0);
				} else {
					GL11.glColor3f(0, 0.66f, 0.92f);
				}

				RenderUtils.drawTexturedRect(matrix,country.texture, x, y + (MonitorScreen.screen.ySize * title_bar_height), 0, 0,
						width, height - (MonitorScreen.screen.ySize * title_bar_height), width,
						height - (MonitorScreen.screen.ySize * title_bar_height), 1, 2);
				GL11.glColor3f(1, 1, 1);
			}
			RenderSystem.disableBlend();
			RenderSystem.disableAlphaTest();

			RenderSystem.translated(0, 0, 2);
			RenderSystem.translated(0, 0, -2);

			EpidemicCountry country = getHoveredEpidemicCountry();
			if (country != null) {
				RenderSystem.translated(0, 0, 2);
				if(Screen.hasShiftDown()){
				this.renderTooltip(matrix,screen.mouseX, screen.mouseY, font, country.name,
						"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected),
						"Density " + country.getDensity(), "Area " + country.area);
				}
				else {
					this.renderTooltip(matrix,screen.mouseX, screen.mouseY, font, country.name,
							"Pop " + formatNumber(country.getAlive()), "Inf " + formatNumber(country.infected));
				}
				RenderSystem.translated(0, 0, -2);
			}
		} else if (gameState == EnumGameState.END) {
			boolean win = this.getGlobalAlive() == 0;
			RenderSystem.translated(0, 0, 1);
			RenderUtils.drawCenteredString(matrix,win ? SevenDaysToMine.proxy.localize("computer.app.epidemic.win") : SevenDaysToMine.proxy.localize("computer.app.epidemic.lost") , x + width / 2, y + 2*font.lineHeight, 0xffffff);
			String epilog = "";
			String vaccine = SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.info.vaccine", this.vaccineProgress);
			
			if(win) {
				epilog = SevenDaysToMine.proxy.localize("computer.app.epidemic.win.epilog", this.diseaseName, Math.round(this.day));
				RenderUtils.drawCenteredString(matrix,vaccine, x + width/2,y + 6 * font.lineHeight, 0xffffff);
			}
			else {
				epilog = this.vaccineProgress == 100 ? SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.vaccine", this.diseaseName, Math.round(this.day)) : SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.died", this.diseaseName, Math.round(this.day));
				String killed = SevenDaysToMine.proxy.localize("computer.app.epidemic.lost.info.killed", this.getGlobalDead(), Math.round(((double)this.getGlobalDead()/this.getGlobalPopulation())*100));
				RenderUtils.drawCenteredString(matrix,killed, x + width/2,y + 6 * font.lineHeight, 0xffffff);
				RenderUtils.drawCenteredString(matrix,vaccine, x + width/2,y + 7 * font.lineHeight, 0xffffff);
			}
			RenderUtils.drawCenteredString(matrix,epilog, x + width/2,
					y + 4 * font.lineHeight, 0xffffff);
			RenderSystem.translated(0, 0, -1);

		}
		super.render(matrix,partialTicks);
		RenderSystem.translated(0, 0, -zLevel);
		RenderSystem.popMatrix();

	}

	public EpidemicCountry getHoveredEpidemicCountry() {
		if (isMouseInside()) {
			for (EpidemicCountry country : countries) {
				double rX = (screen.mouseX - x) / width;
				double rY = (screen.mouseY - (y + (MonitorScreen.screen.ySize * title_bar_height)))
						/ (height - (MonitorScreen.screen.ySize * title_bar_height));
				if (rX < 0 || rY < 0 || rX > 1 || rY > 1)
					continue;
				Color color = getColorAt(country.texture, rX, rY);

				if (color.getAlpha() != 0) {
					return country;
				}
			}
		}
		return null;
	}

	public String formatNumber(long l) {
		return NumberFormat.getNumberInstance(Locale.US).format(l);
	}

	@Override
	public void tick() {
		super.tick();

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientTick() {
		super.clientTick();
		if (gameState == EnumGameState.GAME) {
			double dayPrev = day;
			day += speed * 0.01;
			double delta = Math.floor(day) - Math.floor(dayPrev);
			if (delta > 0) {
				for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
					country.update(delta);
				}

				boolean dirty = false;

				if (discovered && getRand().nextInt(14) == 0 && ThreadLocalRandom.current().nextLong(Math.max(1,
						(long) ((getGlobalAlive() - 5 * getGlobalDead()) / (getLethality() * delta)))) == 0) {
					vaccineProgress++;
					dirty = true;
				}
				if (this.getGlobalAlive() == 0 || this.getGlobalInfected() == 0 || vaccineProgress == 100) {
					this.gameState = EnumGameState.END;
					dirty = true;
				}
				if (dirty) {
					sync();
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientInit() {
		super.clientInit();
		FontRenderer font = Minecraft.getInstance().font;

		buttonNewGame = new Button(x + width / 2 - 25, y + 30, 50, 10, screen, "computer.epidemic.new_game", 1) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.MENU;
			}

		};
		buttonNewGame.background = false;
		buttonNewGame.border = false;
		buttonNewGame.textNormal = 0xffffff;
		buttonNewGame.setFontSize(0.7);
		buttonNewGame.setProcess(this);
		elements.add(buttonNewGame);

		fieldCreateName = new TextField(x + width / 2 - 25, y + 30, 50, 9, screen) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) process).gameState != EnumGameState.CREATE;
			}

		};
		fieldCreateName.setDefaultText("Smallpox");
		fieldCreateName.setProcess(this);
		elements.add(fieldCreateName);

		buttonStart = new Button(x + width / 2 - 25, y + 40, 50, 10, screen, "computer.epidemic.start", 2) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.CREATE;
			}

		};
		buttonStart.background = false;
		buttonStart.border = false;
		buttonNewGame.textNormal = 0xffffff;
		buttonStart.setFontSize(0.7);
		buttonStart.setProcess(this);
		elements.add(buttonStart);

		buttonDisease = new Button(x + width / 2 - font.width(diseaseName + " (" + points + ")") / 2,
				y + height - (MonitorScreen.screen.ySize * title_bar_height) * 5 + font.lineHeight,
				font.width(diseaseName + " (" + points + ")"),
				Minecraft.getInstance().font.lineHeight, screen, diseaseName + " (" + points + ")", 3) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonDisease.background = false;
		buttonDisease.border = false;
		buttonDisease.textNormal = 0xffffff;
		buttonDisease.setFontSize(0.7);
		buttonDisease.setProcess(this);
		elements.add(buttonDisease);

		buttonBack = new Button(x + 2, y + height - font.lineHeight, font.width("Back"),
				font.lineHeight, screen, "Back", 4) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE && state != EnumGameState.WORLD
						&& state != EnumGameState.INFECTED_MAP;
			}

		};
		buttonBack.background = false;
		buttonBack.border = false;
		buttonBack.textNormal = 0xffffff;
		buttonBack.setFontSize(0.7);
		buttonBack.setProcess(this);
		elements.add(buttonBack);

		buttonCoughing = new Button(x + width / 2 - 1.5 * font.width("Infectivity"),
				y + 4.2 * font.lineHeight, font.width("Coughing"), font.lineHeight,
				screen, "Coughing", 5) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.COUGHING) ? 0x05ff00
						: (points < 10 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonCoughing.background = false;
		buttonCoughing.border = false;
		buttonCoughing.textNormal = 0xffffff;
		buttonCoughing.setFontSize(0.7);
		buttonCoughing.setProcess(this);
		elements.add(buttonCoughing);

		buttonSneezing = new Button(x + width / 2 - 1.5 * font.width("Infectivity"),
				y + 5.2 * font.lineHeight, font.width("Sneezing"), font.lineHeight,
				screen, "Sneezing", 6) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.SNEEZING) ? 0x05ff00
						: (points < 20 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonSneezing.background = false;
		buttonSneezing.border = false;
		buttonSneezing.textNormal = 0xffffff;
		buttonSneezing.setFontSize(0.7);
		buttonSneezing.setProcess(this);
		elements.add(buttonSneezing);

		buttonVomiting = new Button(x + width / 2 - 1.5 * font.width("Infectivity"),
				y + 6.2 * font.lineHeight, font.width("Vomiting"), font.lineHeight,
				screen, "Vomiting", 7) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.VOMITING) ? 0x05ff00
						: (points < 30 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonVomiting.background = false;
		buttonVomiting.border = false;
		buttonVomiting.textNormal = 0xffffff;
		buttonVomiting.setFontSize(0.7);
		buttonVomiting.setProcess(this);
		elements.add(buttonVomiting);

		buttonSeizures = new Button(x + width / 2 + 0.5 * font.width("Lethality"),
				y + 4.2 * font.lineHeight, font.width("Seizures"), font.lineHeight,
				screen, "Seizures", 8) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.SEIZURES) ? 0x05ff00
						: (points < 10 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonSeizures.background = false;
		buttonSeizures.border = false;
		buttonSeizures.textNormal = 0xffffff;
		buttonSeizures.setFontSize(0.7);
		buttonSeizures.setProcess(this);
		elements.add(buttonSeizures);

		buttonNecrosis = new Button(x + width / 2 + 0.5 * font.width("Lethality"),
				y + 5.2 * font.lineHeight, font.width("Necrosis"), font.lineHeight,
				screen, "Necrosis", 9) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.NECROSIS) ? 0x05ff00
						: (points < 20 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonNecrosis.background = false;
		buttonNecrosis.border = false;
		buttonNecrosis.textNormal = 0xffffff;
		buttonNecrosis.setFontSize(0.7);
		buttonNecrosis.setProcess(this);
		elements.add(buttonNecrosis);

		buttonOrganFailure = new Button(x + width / 2 + 0.5 * font.width("Lethality"),
				y + 6.2 * font.lineHeight, font.width("Organ Failure"),
				font.lineHeight, screen, "Organ Failure", 10) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.DISEASE;
			}

			@Override
			public int getTextColor(int mouseX, int mouseY) {
				return upgrades.contains(EnumUpgrade.ORGAN_FAILURE) ? 0x05ff00
						: (points < 30 ? 0xff0500 : super.getTextColor(mouseX, mouseY));
			}

		};
		buttonOrganFailure.background = false;
		buttonOrganFailure.border = false;
		buttonOrganFailure.textNormal = 0xffffff;
		buttonOrganFailure.setFontSize(0.7);
		buttonOrganFailure.setProcess(this);
		elements.add(buttonOrganFailure);

		buttonFaster = new Button(x + 4 + font.width("<<"),
				y + (MonitorScreen.screen.ySize * title_bar_height) + 2 + font.lineHeight,
				font.width(">>"), font.lineHeight, screen, ">>", 11) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonFaster.background = false;
		buttonFaster.border = false;
		buttonFaster.textNormal = 0xffffff;
		buttonFaster.setFontSize(0.7);
		buttonFaster.setProcess(this);
		elements.add(buttonFaster);

		buttonSlower = new Button(x + 2, y + (MonitorScreen.screen.ySize * title_bar_height) + 2 + font.lineHeight,
				font.width("<"), font.lineHeight, screen, "<<", 12) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonSlower.background = false;
		buttonSlower.border = false;
		buttonSlower.textNormal = 0xffffff;
		buttonSlower.setFontSize(0.7);
		buttonSlower.setProcess(this);
		elements.add(buttonSlower);

		buttonWorld = new Button(x + width - font.width("World"),
				y + (MonitorScreen.screen.ySize * title_bar_height) + 2, font.width("World"),
				font.lineHeight, screen, "World", 13) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.GAME && state != EnumGameState.SELECT_START;
			}

		};
		buttonWorld.background = false;
		buttonWorld.border = false;
		buttonWorld.textNormal = 0xffffff;
		buttonWorld.setFontSize(0.7);
		buttonWorld.setProcess(this);
		elements.add(buttonWorld);

		buttonShowInfected = new Button(x + width - font.width("Show Infected Countries"),
				y + height - font.lineHeight, font.width("Show Infected Countries"),
				font.lineHeight, screen, "Show Infected Countries", 14) {

			@Override
			public boolean isDisabled() {
				EnumGameState state = ((EpidemicProcess) tickingProcess).gameState;
				return state != EnumGameState.WORLD;
			}

		};
		buttonShowInfected.background = false;
		buttonShowInfected.border = false;
		buttonShowInfected.textNormal = 0xffffff;
		buttonShowInfected.setFontSize(0.7);
		buttonShowInfected.setProcess(this);
		elements.add(buttonShowInfected);
		
		buttonMainMenu = new Button(x + width / 2 - 25, y + height - 3*font.lineHeight, 50, 10, screen, "computer.epidemic.main_menu", 1) {

			@Override
			public boolean isDisabled() {
				return ((EpidemicProcess) tickingProcess).gameState != EnumGameState.END;
			}

		};
		buttonMainMenu.background = false;
		buttonMainMenu.border = false;
		buttonMainMenu.textNormal = 0xffffff;
		buttonMainMenu.setFontSize(0.7);
		buttonMainMenu.setProcess(this);
		elements.add(buttonMainMenu);

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initWindow() {
		super.initWindow();
		if (elements.size() < 2) {
			return;
		}

		FontRenderer font = Minecraft.getInstance().font;

		buttonNewGame.setX(x + width / 2 - 25);
		buttonNewGame.setY(y + 30);
		buttonNewGame.setWidth(50);
		buttonNewGame.setHeight(10);

		fieldCreateName.setX(x + width / 2 - 25);
		fieldCreateName.setY(y + 30);
		fieldCreateName.setWidth(50);
		fieldCreateName.setHeight(9);

		fieldCreateName.setX(x + width / 2 - 25);
		fieldCreateName.setY(y + 40);
		fieldCreateName.setWidth(50);
		fieldCreateName.setHeight(10);

		buttonDisease.setX(x + width / 2 - font.width(diseaseName + " (" + points + ")") / 2);
		buttonDisease.setY(y + height - (MonitorScreen.screen.ySize * title_bar_height) * 5 + font.lineHeight);
		buttonDisease.setWidth(font.width(diseaseName + " (" + points + ")"));
		buttonDisease.setHeight(font.lineHeight);

		buttonBack.setX(x + 2);
		buttonBack.setY(y + height - font.lineHeight);
		buttonBack.setWidth(font.width("Back"));
		buttonBack.setHeight(font.lineHeight);

		buttonCoughing.setX(x + width / 2 - 1.5 * font.width("Infectivity"));
		buttonCoughing.setY(y + 4.2 * font.lineHeight);
		buttonCoughing.setWidth(font.width("Coughing"));
		buttonCoughing.setHeight(font.lineHeight);

		buttonSneezing.setX(x + width / 2 - 1.5 * font.width("Infectivity"));
		buttonSneezing.setY(y + 5.2 * font.lineHeight);
		buttonSneezing.setWidth(font.width("Sneezing"));
		buttonSneezing.setHeight(font.lineHeight);

		buttonVomiting.setX(x + width / 2 - 1.5 * font.width("Infectivity"));
		buttonVomiting.setY(y + 5.2 * font.lineHeight);
		buttonVomiting.setWidth(font.width("Vomiting"));
		buttonVomiting.setHeight(font.lineHeight);

		buttonSeizures.setX(x + width / 2 + 0.5 * font.width("Lethality"));
		buttonSeizures.setY(y + 4.2 * font.lineHeight);
		buttonSeizures.setWidth(font.width("Seizures"));
		buttonSeizures.setHeight(font.lineHeight);

		buttonNecrosis.setX(x + width / 2 + 0.5 * font.width("Lethality"));
		buttonNecrosis.setY(y + 5.2 * font.lineHeight);
		buttonNecrosis.setWidth(font.width("Necrosis"));
		buttonNecrosis.setHeight(font.lineHeight);

		buttonOrganFailure.setX(x + width / 2 + 0.5 * font.width("Lethality"));
		buttonOrganFailure.setY(y + 5.2 * font.lineHeight);
		buttonOrganFailure.setWidth(font.width("Organ Failure"));
		buttonOrganFailure.setHeight(font.lineHeight);

		buttonFaster.setX(x + 4 + font.width("<<"));
		buttonFaster.setY(y + (MonitorScreen.screen.ySize * title_bar_height) + 2 + font.lineHeight);
		buttonFaster.setWidth(font.width(">>"));
		buttonFaster.setHeight(font.lineHeight);

		buttonSlower.setX(x + 2);
		buttonSlower.setY(y + (MonitorScreen.screen.ySize * title_bar_height) + 2 + font.lineHeight);
		buttonSlower.setWidth(font.width("<<"));
		buttonSlower.setHeight(font.lineHeight);

		buttonWorld.setX(x + width - font.width("World"));
		buttonWorld.setY(y + (MonitorScreen.screen.ySize * title_bar_height) + 2);
		buttonWorld.setWidth(font.width("World"));
		buttonWorld.setHeight(font.lineHeight);

		buttonShowInfected.setX(x + width - font.width("Show Infected Countries"));
		buttonShowInfected.setY(y + height - font.lineHeight);
		buttonShowInfected.setWidth(font.width("Show Infected Countries"));
		buttonShowInfected.setHeight(font.lineHeight);
		
		buttonNewGame.setX(x + width / 2 - 25);
		buttonNewGame.setY(y + height - 3*font.lineHeight);
		buttonNewGame.setWidth(50);
		buttonNewGame.setHeight(10);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onDragReleased() {
		super.onDragReleased();
	}

	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		super.save(nbt);
		nbt.putString("state", gameState.getName());
		nbt.putString("diseaseName", diseaseName);
		nbt.putDouble("day", day);
		nbt.putDouble("speed", speed);
		nbt.putInt("points", points);
		nbt.putBoolean("discovered", discovered);
		nbt.putInt("vaccineProgress", vaccineProgress);
		ListNBT list = new ListNBT();
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			list.add(country.save(new CompoundNBT()));
		}
		nbt.put("countries", list);
		list = new ListNBT();
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			list.add(StringNBT.valueOf(upgrade.name));
		}
		nbt.put("upgrades", list);
		return nbt;
	}

	@Override
	public void readFromNBT(CompoundNBT nbt) {
		super.readFromNBT(nbt);
		
		if (nbt.contains("state")) {
			gameState = EnumGameState.getByName(nbt.getString("state"));
		}
		if (nbt.contains("diseaseName")) {
			diseaseName = nbt.getString("diseaseName");
		}
		if (nbt.contains("day")) {
			day = nbt.getDouble("day");
		}
		if (nbt.contains("speed")) {
			speed = nbt.getDouble("speed");
		}
		if (nbt.contains("points")) {
			points = nbt.getInt("points");
		}
		if (nbt.contains("discovered")) {
			discovered = nbt.getBoolean("discovered");
		}
		if (nbt.contains("vaccineProgress")) {
			vaccineProgress = nbt.getInt("vaccineProgress");
		}
		if (nbt.contains("countries")) {
			this.countries.clear();
			ListNBT list = nbt.getList("countries", Constants.NBT.TAG_COMPOUND);
			Iterator<INBT> it = list.iterator();
			while (it.hasNext()) {
				INBT base = it.next();
				if (base instanceof CompoundNBT) {
					EpidemicCountry country = EpidemicCountry.fromNBT((CompoundNBT) base, this);
					countries.add(country);
				}
			}

			/*
			 * Makes connections between the countries
			 */
			for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
				for (String s : country.adjacentTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.adjacent.add(other);
							break;
						}
					}
				}
				for (String s : country.airTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.air.add(other);
							break;
						}
					}
				}
				for (String s : country.navalTemp) {
					for (EpidemicCountry other : new ArrayList<EpidemicCountry>(countries)) {
						if (other.name.equals(s)) {
							country.naval.add(other);
							break;
						}
					}
				}
			}
		}
		if (nbt.contains("upgrades")) {
			upgrades.clear();
			ListNBT list = nbt.getList("upgrades", Constants.NBT.TAG_STRING);
			Iterator<INBT> it = list.iterator();
			while (it.hasNext()) {
				INBT base = it.next();
				if (base instanceof StringNBT) {
					upgrades.add(EnumUpgrade.getByName(base.getAsString()));
				}
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void keyTyped(char typedChar, int keyCode) {
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void onButtonPressed(Button button, int mouseButton) {
		super.onButtonPressed(button, mouseButton);
		if (isMinimized())
			return;
		if (button == this.buttonNewGame) {
			this.gameState = EnumGameState.CREATE;
			this.sync();
		} else if (button == this.buttonStart) {
			this.diseaseName = this.fieldCreateName.getContentText();
			this.buttonDisease.setText(diseaseName);
			this.gameState = EnumGameState.SELECT_START;
			this.day = 0;
			this.speed = 1;
			this.points = 0;
			EpidemicHelper.initCountries(this);
			this.sync();
		} else if (button == this.buttonDisease) {
			this.gameState = EnumGameState.DISEASE;
			this.sync();
		} else if (button == this.buttonBack) {
			this.gameState = gameState == EnumGameState.INFECTED_MAP ? EnumGameState.WORLD : EnumGameState.GAME;
			this.sync();
		} else if (button == this.buttonCoughing) {
			if (this.points >= 10) {
				this.upgrades.add(EnumUpgrade.COUGHING);
				this.points -= 10;
				this.sync();
			}
			this.sync();
		}

		else if (button == this.buttonSneezing) {
			if (this.points >= 20) {
				this.upgrades.add(EnumUpgrade.SNEEZING);
				this.points -= 20;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonVomiting) {
			if (this.points >= 30) {
				this.upgrades.add(EnumUpgrade.VOMITING);
				this.points -= 30;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonSeizures) {
			if (this.points >= 10) {
				this.upgrades.add(EnumUpgrade.SEIZURES);
				this.points -= 10;
				this.sync();
			}
			this.sync();
		}

		else if (button == this.buttonNecrosis) {
			if (this.points >= 20) {
				this.upgrades.add(EnumUpgrade.NECROSIS);
				this.points -= 20;
				this.sync();
			}
			this.sync();
		} else if (button == this.buttonOrganFailure) {
			if (this.points >= 30) {
				this.upgrades.add(EnumUpgrade.ORGAN_FAILURE);
				this.points -= 30;
				this.sync();
			}
		} else if (button == this.buttonFaster) {
			if(++speed > 5) {
				speed = 5;
			}
			this.sync();
		} else if (button == this.buttonSlower) {
			if(--speed < 1) {
				speed = 1;
			}
			this.sync();
		} else if (button == this.buttonWorld) {
			this.gameState = EnumGameState.WORLD;
			this.sync();
		} else if (button == this.buttonShowInfected) {
			this.gameState = EnumGameState.INFECTED_MAP;
			this.sync();
		}else if (button == this.buttonMainMenu) {
			this.gameState = EnumGameState.MENU;
			this.day = 0;
			this.speed = 1;
			this.points = 0;
			this.countries.clear();
			this.upgrades.clear();
			this.sync();
		}

	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		if (gameState == EnumGameState.SELECT_START) {
			EpidemicCountry country = this.getHoveredEpidemicCountry();
			if (country != null) {
				country.infected = 1;
				this.gameState = EnumGameState.GAME;
				this.sync();
			}
		}
	}

	public double getInfectivity() {
		double infectivity = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			infectivity *= upgrade.infectivityModifer;
		}
		return infectivity;
	}

	public double getLethality() {
		double lethality = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			lethality *= upgrade.lethalityModifer;
		}
		return lethality - 1;
	}

	public double getDectibility() {
		double dectibility = 1;
		for (EnumUpgrade upgrade : new ArrayList<EnumUpgrade>(upgrades)) {
			dectibility *= upgrade.detectibilityModifer;
		}
		return dectibility;
	}

	public long getGlobalPopulation() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population;
		}
		return pop;
	}

	public long getGlobalInfected() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.infected;
		}
		return pop;
	}

	public long getGlobalDead() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.dead;
		}
		return pop;
	}

	public long getGlobalHealthy() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population - country.dead - country.infected;
		}
		return pop;
	}

	public long getGlobalAlive() {
		long pop = 0;
		for (EpidemicCountry country : new ArrayList<EpidemicCountry>(countries)) {
			pop += country.population - country.dead;
		}
		return pop;
	}

	public void setPoints(int points) {
		this.points = points;
		FontRenderer font = Minecraft.getInstance().font;

		buttonDisease.setText(diseaseName + " (" + this.points + ")");
		buttonDisease.setX(x + width / 2 - font.width(diseaseName + " (" + this.points + ")") / 2);
		buttonDisease.setY(y + height - (MonitorScreen.screen.ySize * title_bar_height) * 5 + font.lineHeight);
		buttonDisease.setWidth(font.width(diseaseName + " (" + this.points + ")"));
	}

	public void addPoints(int delta) {
		setPoints(points + delta);
	}

	public void consumePoints(int delta) {
		setPoints(points - delta);
	}

	public Random getRand() {
		return computerTE.getLevel().random;
	}

	/*
	 * A modified version of RenderUtils.getColorAt()
	 */
	public static Color getColorAt(ResourceLocation res, double relativeX, double relativeY) {

		if (mapCache.containsKey(res.toString())) {
			BufferedImage image = mapCache.get(res.toString());
			int x = Math.min(image.getWidth()-1,(int) Math.round(relativeX * image.getWidth()));
			int y = Math.min(image.getHeight()-1,(int) Math.round(relativeY * image.getHeight()));
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);
		}

		InputStream is = null;
		BufferedImage image;

		try {
			is = Minecraft.getInstance().getResourceManager().getResource(res).getInputStream();
			image = ImageIO.read(is);

			int x = (int) Math.round(relativeX * image.getWidth());
			int y = (int) Math.round(relativeY * image.getHeight());
			mapCache.put(res.toString(), image);
			int rgb = image.getRGB(x, y);
			return new Color(rgb, true);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;

	}
}
